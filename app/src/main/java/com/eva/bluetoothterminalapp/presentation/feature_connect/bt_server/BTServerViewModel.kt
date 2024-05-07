package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server

import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothServerConnector
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerRouteState
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

class BTServerViewModel(
	private val connector: BluetoothServerConnector,
) : AppViewModel() {

	private val _serverState = MutableStateFlow(BTServerRouteState())
	val state = _serverState.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	private var _startServer: Job? = null

	init {
		//start server
		startServer()
		//update connection state
		updateConnectionState()
		//read incomming state
		readIncomingData()
	}


	fun onEvent(event: BTServerRouteEvents) {
		when (event) {
			BTServerRouteEvents.StopServer -> stopServerAndClearResources()
			BTServerRouteEvents.OnSendEvents -> sendText(_serverState.value.textFieldValue)
			BTServerRouteEvents.CloseDialogAndStopServer -> closeAndStopServer()
			BTServerRouteEvents.OpenDisconnectDialog -> toggleDialog(true)
			BTServerRouteEvents.CloseDisconnectDialog -> toggleDialog(false)
			BTServerRouteEvents.RestartServer -> restartServer()
			is BTServerRouteEvents.OnTextFieldValue -> _serverState.update { state ->
				state.copy(textFieldValue = event.message)
			}
		}
	}

	private fun closeAndStopServer() {
		toggleDialog(false)
		stopServerAndClearResources()
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
		_startServer = viewModelScope.launch { connector.startServer() }
	}

	private fun sendText(message: String) = viewModelScope.launch {
		val messageAsByteArray = message.trim()
		val result = connector.sendData(messageAsByteArray)
		result.onFailure { err ->
			_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: "MESSAGE"))
		}
	}

	private fun updateConnectionState() = connector.connectMode
		.onEach { mode ->
			_serverState.update { state ->
				state.copy(connectionMode = mode)
			}
		}.launchIn(viewModelScope)


	private fun readIncomingData() = connector.readIncomingData
		.catch { err ->
			_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: ""))
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
		_startServer = null
		connector.closeServer()
	}

	override fun onCleared() {
		// stop server and clear resources
		stopServerAndClearResources()
		super.onCleared()
	}

}