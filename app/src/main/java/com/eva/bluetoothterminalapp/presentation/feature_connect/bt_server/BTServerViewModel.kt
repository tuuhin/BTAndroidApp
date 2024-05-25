package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server

import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothServerConnector
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerRouteState
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

class BTServerViewModel(
	private val btConnector: BluetoothServerConnector,
	private val settings: BTSettingsDataSore,
) : AppViewModel() {

	private val _serverState = MutableStateFlow(BTServerRouteState())
	val state = _serverState.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	val btSettings = settings.settingsFlow.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(2000),
		initialValue = BTSettingsModel()
	)

	private val _textFieldText: String
		get() = _serverState.value.textFieldValue.trim()

	private val _localEcho = MutableSharedFlow<BluetoothMessage>()
	private var _startServer: Job? = null

	init {
		//update connection state
		updateConnectionState()
		//read incomming state
		readIncommingOrOutGoingData()
	}


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
		_serverState.update { state -> state.copy(showStartServerDialog = false) }
		_uiEvents.emit(UiEvents.NavigateBack)
	}


	private fun restartServer() {
		// stops and clear resources
		stopServerAndClearResources()
		//starts the server
		startServer()
	}

	private fun toggleDialog(isOpen: Boolean) = _serverState.update { state ->
		state.copy(showDisconnectDialog = isOpen)
	}


	private fun startServer() {
		_startServer = viewModelScope.launch { btConnector.startServer() }
		_serverState.update { state -> state.copy(showStartServerDialog = false) }
	}

	private fun sendText() = viewModelScope.launch {

		val isClearTextField = settings.settings.clearInputOnSend
		val message = _textFieldText.trim()

		if (message.isEmpty()) {
			_uiEvents.emit(UiEvents.ShowToast("Cannot send empty message"))
			return@launch
		}
		val result = btConnector.sendData(message)

		result.fold(
			onSuccess = {
				val addedMessage = BluetoothMessage(
					message = message,
					type = BluetoothMessageType.MESSAGE_FROM_SELF
				)
				_localEcho.emit(addedMessage)

				if (isClearTextField)
					_serverState.update { state -> state.copy(textFieldValue = "") }
			},
			onFailure = { err ->
				_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: "Failed to send message"))
			},
		)
	}

	private fun updateConnectionState() = btConnector.connectMode.onEach { mode ->
		_serverState.update { state ->
			state.copy(connectionMode = mode)
		}
	}.launchIn(viewModelScope)


	@OptIn(ExperimentalCoroutinesApi::class)
	private fun readIncommingOrOutGoingData() = settings.settingsFlow
		.map { settings -> settings.localEchoEnabled }
		.distinctUntilChanged()
		.flatMapConcat { enabled ->
			// if local echo enabled then check both flows
			if (enabled) merge(btConnector.readIncomingData, _localEcho)
			// otherwise only the incomming flow
			else btConnector.readIncomingData
		}
		.catch { err ->
			_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: ""))
			// if there is an error stop the server
			stopServerAndClearResources()
		}
		.onEach { message ->
			_serverState.update { state ->
				val updatedMessages = state.messages.add(message)
				state.copy(messages = updatedMessages)
			}
		}.launchIn(viewModelScope)

	private fun stopServerAndClearResources() {
		_startServer?.cancel()
		btConnector.closeServer()
		_startServer = null
	}

	override fun onCleared() {
		// stop server and clear resources
		stopServerAndClearResources()
		super.onCleared()
	}

}