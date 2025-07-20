package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientDeviceState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientMessagesState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.EndConnectionEvents
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothClientConnectArgs
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import com.ramcosta.composedestinations.generated.navArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val LOGGER = "BLUETOOTH_CLIENT_VIEW_MODEL"

class BTClientViewModel(
	private val connector: BluetoothClientConnector,
	private val bTSettings: BTSettingsDataSore,
	private val savedStateHandle: SavedStateHandle,
) : AppViewModel() {

	private val _messagesState = MutableStateFlow(BTClientMessagesState())
	val messagesState = _messagesState
		.onStart { readIncomingData() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(8_000L),
			initialValue = BTClientMessagesState()
		)

	// remote device need to emit something in order to be collected
	private val _connectedDevice = connector.remoteDevice.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Eagerly,
		initialValue = null
	)

	val clientState = combine(_connectedDevice, connector.connectionState) { device, connection ->
		BTClientDeviceState(
			device = device,
			connectionStatus = connection
		)
	}.onStart { startClientJob() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(8_000L),
			initialValue = BTClientDeviceState()
		)

	private val _showCloseDialog = MutableStateFlow(false)
	val showCloseDialog = _showCloseDialog.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	val btSettings = bTSettings.settingsFlow.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(2000),
		initialValue = BTSettingsModel()
	)

	private val clientConnect: BluetoothClientConnectArgs
		get() = savedStateHandle.navArgs()

	private var _connectAsClientJob: Job? = null

	private val _textFieldValueTrimmed: String
		get() = _messagesState.value.textFieldValue.trim()

	private val _localEcho = MutableSharedFlow<BluetoothMessage>()

	fun onCloseConnectionEvent(event: EndConnectionEvents) {
		when (event) {
			EndConnectionEvents.OnCancelAndCloseDialog -> toggleDialog(false)
			EndConnectionEvents.OnOpenDisconnectDialog -> toggleDialog(true)
			EndConnectionEvents.OnDisconnectAndNavigateBack -> onDisconnectAndClose()
		}
	}

	fun onClientConnectionEvents(event: BTClientRouteEvents) {
		when (event) {
			BTClientRouteEvents.OnReconnectClient -> startClientJob(true)
			BTClientRouteEvents.OnDisconnectClient -> closeConnection()
			BTClientRouteEvents.OnSendEvents -> sendText()
			is BTClientRouteEvents.OnSendFieldTextChanged -> _messagesState.update { state ->
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


	private fun startClientJob(showEvent: Boolean = false) {
		if (_connectAsClientJob?.isActive == true) {
			if (showEvent) {
				val event = UiEvents.ShowSnackBar("Trying to connect please wait")
				viewModelScope.launch { _uiEvents.emit(event) }
			}
			return
		}
		// create a client job to connect to the client
		_connectAsClientJob = viewModelScope.launch {
			val results = connector.connectClient(clientConnect.address, clientConnect.uuid)
			results.fold(
				onSuccess = {},
				onFailure = { err ->
					_uiEvents.emit(UiEvents.ShowSnackBar(message = err.message ?: ""))
				},
			)
		}
	}


	private fun sendText() = viewModelScope.launch {
		val settings = bTSettings.getSettings()

		val isClearTextField = settings.clearInputOnSend
		val isLocalEchoEnabled = settings.localEchoEnabled

		val message = _textFieldValueTrimmed.trim()
		if (message.isEmpty()) {
			_uiEvents.emit(UiEvents.ShowSnackBar("Cannot send empty message"))
			return@launch
		}
		val result = connector.sendData(message)

		result.fold(
			onSuccess = {
				if (isLocalEchoEnabled) {
					val selfMessage = BluetoothMessage(
						message = message,
						type = BluetoothMessageType.MESSAGE_FROM_SELF
					)
					_localEcho.emit(selfMessage)
				}
				if (isClearTextField) _messagesState.update { state -> state.copy(textFieldValue = "") }
			},
			onFailure = { err ->
				_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: "Failed to send message"))
			},
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	private fun readIncomingData() = bTSettings.settingsFlow
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
					message = "Unable to read incoming data",
					actionText = "Disconnect",
					action = ::closeConnection
				)
			)
		}
		.onEach { message ->
			_messagesState.update { state ->
				val updatedMessages = state.messages.add(message)
				state.copy(messages = updatedMessages)
			}
		}.launchIn(viewModelScope)


	private fun closeConnection(showToast: Boolean = false) {
		// cancels the job and close the connection
		_messagesState.update { state -> state.copy(textFieldValue = "") }
		_connectAsClientJob?.cancel()
		val result = connector.closeClient()

		result.onFailure {
			val event = if (showToast) UiEvents.ShowToast("Connection Closed")
			else UiEvents.ShowSnackBar("Connection Closed")
			viewModelScope.launch { _uiEvents.emit(event) }
		}
	}


	override fun onCleared() {
		// close the connection if the connection is active
		if (_connectAsClientJob?.isActive == true) {
			closeConnection()
		}
		// log viewmodel is cleared
		Log.d(LOGGER, "CLEAN UP IN CLIENT")
		super.onCleared()
	}

}