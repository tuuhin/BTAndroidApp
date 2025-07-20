package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server

import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothServerConnector
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.PeerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerDeviceState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerScreenState
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
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

class BTServerViewModel(
	private val serverConnector: BluetoothServerConnector,
	private val bTSettings: BTSettingsDataSore,
) : AppViewModel() {

	private val _serverState = MutableStateFlow(BTServerScreenState())
	val serverState = _serverState.onStart { readIncommingOrOutGoingData() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(8_000L),
			initialValue = BTServerScreenState()
		)

	// remote device need to emit something in order to be collected
	private val _remoteDevice = serverConnector.remoteDevice.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Eagerly,
		initialValue = null
	)

	val connectedDevice = combine(
		_remoteDevice,
		serverConnector.peerConnectionState,
		serverConnector.serverState
	) { device, peerState, serverState ->
		BTServerDeviceState(device = device, peerState = peerState, serverState = serverState)
	}.onStart { checkClientDisconnect() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(8_000L),
			initialValue = BTServerDeviceState()
		)

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	val btSettings = bTSettings.settingsFlow.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(2000),
		initialValue = BTSettingsModel()
	)

	private val _localEcho = MutableSharedFlow<BluetoothMessage>()
	private var _startServerJob: Job? = null


	fun onEvent(event: BTServerEvents) {
		when (event) {
			BTServerEvents.OnStartServer -> startServer()
			BTServerEvents.OnStopServer -> stopServerAndClearResources()
			BTServerEvents.OnSendEvents -> sendText()
			BTServerEvents.OnOpenDisconnectDialog -> toggleDialog(true)
			BTServerEvents.OnCloseDisconnectDialog -> toggleDialog(false)
			BTServerEvents.OnRestartServer -> restartServer()
			BTServerEvents.OnStopServerAndNavigateBack -> cancelServerStartAndPopBack()
			is BTServerEvents.OnTextFieldValue -> _serverState.update { state ->
				state.copy(textFieldValue = event.message)
			}
		}
	}


	private fun cancelServerStartAndPopBack() = viewModelScope.launch {
		_serverState.update { state -> state.copy(showServerTerminal = false) }
		_uiEvents.emit(UiEvents.NavigateBack)
	}


	private fun restartServer() {
		// stops and clear resources
		stopServerAndClearResources()
		//starts the server
		startServer()
		// clear the messages
		_serverState.update { state ->
			val messages = state.messages.clear()
			state.copy(messages = messages)
		}
	}

	private fun checkClientDisconnect() {
		connectedDevice.map { it.peerState to it.serverState }.distinctUntilChanged()
			.onEach { (peerState, serverState) ->
				if (serverState != ServerConnectionState.PEER_CONNECTION_ACCEPTED) return@onEach
				// there was a peer connection but not the peer has disconnected
				if (peerState == PeerConnectionState.PEER_DISCONNECTED) {
					val event = UiEvents.ShowSnackBarWithActions(
						message = "Client Disconnected",
						"Restart",
						::restartServer
					)
					_uiEvents.emit(event)
				}
			}.launchIn(viewModelScope)
	}

	private fun toggleDialog(isOpen: Boolean) = _serverState.update { state ->
		state.copy(showExitDialog = isOpen)
	}


	private fun startServer() {
		_startServerJob = viewModelScope.launch { serverConnector.startServer() }
		_serverState.update { state -> state.copy(showServerTerminal = true) }
	}

	private fun sendText() = viewModelScope.launch {

		val settings = bTSettings.getSettings()

		val isClearTextField = settings.clearInputOnSend
		val isLocalEchoEnabled = settings.localEchoEnabled

		val message = _serverState.value.textFieldValue.trim()
		if (message.isEmpty()) {
			_uiEvents.emit(UiEvents.ShowSnackBar("Cannot send empty message"))
			return@launch
		}
		val result = serverConnector.sendData(message)

		result.fold(
			onSuccess = {
				if (isLocalEchoEnabled) {
					val selfMessage = BluetoothMessage(
						message = message,
						type = BluetoothMessageType.MESSAGE_FROM_SELF
					)
					_localEcho.emit(selfMessage)
				}
				if (isClearTextField) _serverState.update { state -> state.copy(textFieldValue = "") }
			},
			onFailure = { err ->
				_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: "Failed to send message"))
			},
		)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	private fun readIncommingOrOutGoingData() = bTSettings.settingsFlow
		.map { settings -> settings.localEchoEnabled }
		.distinctUntilChanged()
		.flatMapConcat { enabled ->
			// if local echo enabled then check both flows
			if (enabled) merge(serverConnector.readIncomingData, _localEcho)
			// otherwise only the incomming flow
			else serverConnector.readIncomingData
		}
		.catch { err ->
			_uiEvents.emit(
				UiEvents.ShowSnackBarWithActions(
					message = "Unable to read incoming data",
					actionText = "Disconnect",
					action = ::stopServerAndClearResources
				)
			)
		}
		.onEach { message ->
			_serverState.update { state ->
				val updatedMessages = state.messages.add(message)
				state.copy(messages = updatedMessages)
			}
		}.launchIn(viewModelScope)


	private fun stopServerAndClearResources() {
		_startServerJob?.cancel()
		_startServerJob = null
		serverConnector.closeServer()
	}

	override fun onCleared() {
		// stop server and clear resources
		stopServerAndClearResources()
		super.onCleared()
	}

}