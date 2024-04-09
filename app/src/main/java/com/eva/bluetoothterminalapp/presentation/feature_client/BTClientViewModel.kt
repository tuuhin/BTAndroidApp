package com.eva.bluetoothterminalapp.presentation.feature_client

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.presentation.feature_client.state.BTClientRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_client.state.BTClientRouteState
import com.eva.bluetoothterminalapp.presentation.navigation.args.ConnectionRouteArgs
import com.eva.bluetoothterminalapp.presentation.navigation.screens.navArgs
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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

class BTClientViewModel(
	private val connector: BluetoothClientConnector,
	private val savedStateHandle: SavedStateHandle,
) : AppViewModel() {

	private val _clientState = MutableStateFlow(BTClientRouteState())
	val clientState = _clientState.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	private val connectionDevice: ConnectionRouteArgs
		get() = savedStateHandle.navArgs()

	private var _connectAsClientJob: Job? = null

	init {
		//start the client
		startClientJob()
		// reads for incoming messages
		readIncomingData()
		// update connection mode
		updateConnectionMode()
	}

	fun onEvent(event: BTClientRouteEvents) {
		when (event) {
			BTClientRouteEvents.ConnectClient -> startClientJob()
			BTClientRouteEvents.DisConnectClient -> closeConnection()
			BTClientRouteEvents.ClearTerminal -> clearTerminal()
			BTClientRouteEvents.OnSendEvents -> sendText(_clientState.value.textFieldValue)
			is BTClientRouteEvents.OnTextFieldValue -> _clientState.update { state ->
				state.copy(textFieldValue = event.message)
			}

			BTClientRouteEvents.CloseDisconnectDialog -> toggleDialog(false)
			BTClientRouteEvents.OpenDisconnectDialog -> toggleDialog(true)
		}
	}

	private fun clearTerminal() = _clientState.update { state ->
		state.copy(messages = persistentListOf())
	}

	private fun toggleDialog(isOpen: Boolean) = _clientState.update { state ->
		state.copy(showDisconnectDialog = isOpen)
	}


	private fun startClientJob() {
		_connectAsClientJob = viewModelScope.launch {
			connector.connectClient(address = connectionDevice.address, secure = true)
		}
	}

	private fun updateConnectionMode() = connector.isConnected.onEach { status ->
		_clientState.update { state -> state.copy(connectionMode = status) }
	}.launchIn(viewModelScope)

	private fun sendText(message: String) = viewModelScope.launch {
		val messageAsByteArray = message.trim().encodeToByteArray()
		val result = connector.sendData(messageAsByteArray)
		result.onFailure { err ->
			_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: "MESSAGE"))
		}
	}


	private fun readIncomingData() = connector.readIncomingData
		.catch { err ->
			_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: ""))
			closeConnection()
		}
		.onEach { message ->
			_clientState.update { state ->
				state.copy(messages = (state.messages + message).toPersistentList())
			}
		}.launchIn(viewModelScope)

	private fun closeConnection() {
		connector.closeClient()
		_connectAsClientJob?.cancel()
	}


	override fun onCleared() {
		connector.releaseResources()
		super.onCleared()
	}

}