package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.ClientTypeState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.CloseConnectionEvent
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.StartConnectionEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util.ClientConnectType
import com.eva.bluetoothterminalapp.presentation.navigation.args.ConnectionRouteArgs
import com.eva.bluetoothterminalapp.presentation.navigation.screens.navArgs
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val LOGGER = "BLUETOOTH_CLIENT_VIEW_MODEL"

class BTClientViewModel(
	private val connector: BluetoothClientConnector,
	private val savedStateHandle: SavedStateHandle,
) : AppViewModel() {

	private val _clientState = MutableStateFlow(BTClientRouteState())
	val clientState = _clientState.asStateFlow()

	private val _showCloseDialog = MutableStateFlow(false)
	val showCloseDialog = _showCloseDialog.asStateFlow()

	private val _connectionType = MutableStateFlow(ClientTypeState())
	val connectionType = _connectionType.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	private val connectionDevice: ConnectionRouteArgs
		get() = savedStateHandle.navArgs()

	private var _connectAsClientJob: Job? = null

	private val _textFieldValue: String
		get() = _clientState.value.textFieldValue

	private val _isConnectAsClient: Boolean
		get() = _connectionType.value.connectType == ClientConnectType.CONNECT_TO_SERVER

	init {
		// reads for incoming messages
		readIncomingData()
		// update connection mode
		updateConnectionMode()
	}

	fun onCloseConnectionEvent(event: CloseConnectionEvent) {
		when (event) {
			CloseConnectionEvent.OnCancelAndCloseDialog -> toggleDialog(false)
			CloseConnectionEvent.OnOpenDisconnectDialog -> toggleDialog(true)
			CloseConnectionEvent.OnDisconnectAndNavigateBack -> onDisconnectAndClose()
		}
	}

	fun onStartConnectionEvents(event: StartConnectionEvents) {
		when (event) {
			is StartConnectionEvents.OnConnectTypeChanged -> _connectionType.update { state ->
				state.copy(connectType = event.type)
			}

			StartConnectionEvents.OnAcceptConnection -> onConnectWithConnectionType()
			StartConnectionEvents.OnCancelAndNavigateBack -> onDisconnectAndClose()
		}
	}

	fun onClientConnectionEvents(event: BTClientRouteEvents) {
		when (event) {
			BTClientRouteEvents.OnReconnectClient -> startClientJob()
			BTClientRouteEvents.OnDisconnectClient -> closeConnection()
			BTClientRouteEvents.OnSendEvents -> sendText()
			is BTClientRouteEvents.OnSendFieldTextChanged -> _clientState.update { state ->
				state.copy(textFieldValue = event.text)
			}
		}
	}


	private fun onConnectWithConnectionType() {
		_connectionType.update { state -> state.copy(showConnectDialog = false) }
		startClientJob()
	}


	private fun onDisconnectAndClose() = viewModelScope.launch {
		//close the dialog
		_showCloseDialog.update { false }
		// close the connection
		closeConnection(showToast = true)
		//navigate back
		_uiEvents.emit(UiEvents.NavigateBack)
	}

	private fun toggleDialog(isOpen: Boolean) = _showCloseDialog.update { isOpen }


	private fun startClientJob() {
		if (_connectAsClientJob?.isActive == true) return
		// create a client job to connect to the client
		_connectAsClientJob = viewModelScope.launch {
			val connectionResults = connector.connectClient(
				address = connectionDevice.address,
				connectAsClient = _isConnectAsClient
			)
			connectionResults.onFailure { err ->
				_uiEvents.emit(UiEvents.ShowSnackBar(message = err.message ?: ""))
			}
		}
	}

	private fun updateConnectionMode() = connector.isConnected
		.onEach { status ->
			_clientState.update { state -> state.copy(connectionMode = status) }
		}.launchIn(viewModelScope)

	private fun sendText() = viewModelScope.launch {

		val message = _textFieldValue.trim()
		if (message.isEmpty()) {
			_uiEvents.emit(UiEvents.ShowSnackBar("Cannot send empty message"))
			return@launch
		}
		val result = connector.sendData(message)

		result.fold(
			onSuccess = {
				val addedMessage = BluetoothMessage(
					message = message,
					type = BluetoothMessageType.MESSAGE_FROM_CLIENT
				)
				_clientState.update { state ->
					val updatedList = state.messages.add(addedMessage)
					state.copy(messages = updatedList, textFieldValue = "")
				}
			},
			onFailure = { err ->
				_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: "Failed to send message"))
			},
		)


	}


	private fun readIncomingData() = connector.readIncomingData
		.catch { err ->
			Log.e(LOGGER, "SOME_ERROR_OCCURRED", err)
			_uiEvents.emit(
				UiEvents.ShowSnackBarWithActions(
					message = err.message ?: "",
					actionText = "Disconnect",
					action = ::closeConnection
				)
			)
		}
		.onEach { message ->
			_clientState.update { state ->
				val updatedMessages = state.messages.add(message)
				state.copy(messages = updatedMessages)
			}
		}.launchIn(viewModelScope)

	private fun closeConnection(showToast: Boolean = false) = viewModelScope.launch {
		// cancels the job and close the connection
		_clientState.update { state -> state.copy(textFieldValue = "") }
		_connectAsClientJob?.cancel()
		val result = connector.closeClient()

		result.onFailure {
			if (showToast) _uiEvents.emit(UiEvents.ShowToast("Connection Closed"))
			else _uiEvents.emit(UiEvents.ShowSnackBar("Connection Closed"))
		}
	}


	override fun onCleared() {
		// close the connection
		closeConnection()
		// unregister the receivers
		connector.releaseResources()
		// log viewmodel is cleared
		Log.d(LOGGER, "CLEARED CLIENT")
		super.onCleared()
	}

}