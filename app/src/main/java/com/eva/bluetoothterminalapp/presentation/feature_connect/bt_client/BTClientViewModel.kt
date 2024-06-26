package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.EndConnectionEvents
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothClientConnectArgs
import com.eva.bluetoothterminalapp.presentation.navigation.screens.navArgs
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val LOGGER = "BLUETOOTH_CLIENT_VIEW_MODEL"

class BTClientViewModel(
	private val connector: BluetoothClientConnector,
	private val settings: BTSettingsDataSore,
	private val savedStateHandle: SavedStateHandle,
) : AppViewModel() {

	private val _clientState = MutableStateFlow(BTClientRouteState())
	val clientState = _clientState.asStateFlow()

	private val _showCloseDialog = MutableStateFlow(false)
	val showCloseDialog = _showCloseDialog.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	val btSettings = settings.settingsFlow.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(2000),
		initialValue = BTSettingsModel()
	)

	private val clientConnect: BluetoothClientConnectArgs
		get() = savedStateHandle.navArgs()

	private var _connectAsClientJob: Job? = null

	private val _textFieldValueTrimmed: String
		get() = _clientState.value.textFieldValue.trim()

	private val _localEcho = MutableSharedFlow<BluetoothMessage>()


	init {
		// reads for incoming messages
		readIncomingData()
//		// update connection mode
		updateConnectionMode()
//		// start client connection
		startClientJob()
	}

	fun onCloseConnectionEvent(event: EndConnectionEvents) {
		when (event) {
			EndConnectionEvents.OnCancelAndCloseDialog -> toggleDialog(false)
			EndConnectionEvents.OnOpenDisconnectDialog -> toggleDialog(true)
			EndConnectionEvents.OnDisconnectAndNavigateBack -> onDisconnectAndClose()
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
				address = clientConnect.address,
				connectUUID = clientConnect.uuid
			)
			connectionResults.onFailure { err ->
				_uiEvents.emit(UiEvents.ShowSnackBar(message = err.message ?: ""))
			}
		}
	}

	private fun updateConnectionMode() = connector.connectionState
		.onEach { status ->
			_clientState.update { state -> state.copy(connectionMode = status) }
		}.launchIn(viewModelScope)

	private fun sendText() = viewModelScope.launch {

		val isClearTextField = settings.settings.clearInputOnSend

		val message = _textFieldValueTrimmed.trim()
		if (message.isEmpty()) {
			_uiEvents.emit(UiEvents.ShowToast("Cannot send empty message"))
			return@launch
		}
		val result = connector.sendData(message)

		result.fold(
			onSuccess = {
				val addedMessage = BluetoothMessage(
					message = message,
					type = BluetoothMessageType.MESSAGE_FROM_SELF
				)
				_localEcho.emit(addedMessage)
				if (isClearTextField) _clientState.update { state -> state.copy(textFieldValue = "") }
			},
			onFailure = { err ->
				_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: "Failed to send message"))
			},
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	private fun readIncomingData() = settings.settingsFlow
		.map { settings -> settings.localEchoEnabled }
		.distinctUntilChanged()
		.flatMapConcat { enabled ->
			// if local echo enabled then check both flows
			if (enabled) merge(connector.readIncomingData, _localEcho)
			// otherwise only the incomming flow
			else connector.readIncomingData
		}
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
		// close the connection if the connection is active
		if (_connectAsClientJob?.isActive == true) {
			closeConnection()
		}
		// unregister the receivers
		connector.releaseResources()
		// log viewmodel is cleared
		Log.d(LOGGER, "CLEARED CLIENT")
		super.onCleared()
	}

}