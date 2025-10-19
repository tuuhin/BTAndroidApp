package com.eva.bluetoothterminalapp.presentation.feature_le_server

import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BLEServerConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServerServices
import com.eva.bluetoothterminalapp.presentation.feature_le_server.state.BLEServerScreenEvents
import com.eva.bluetoothterminalapp.presentation.feature_le_server.state.BLEServerScreenState
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BLEServerViewModel(
	private val connector: BLEServerConnector
) : AppViewModel() {

	private val _showServerRunningDialog = MutableStateFlow(false)
	private val _showServicesSelector = MutableStateFlow(false)
	private val _selectedServiceOptions = MutableStateFlow(BLEServerServices.entries.toSet())

	val screenState = combine(
		_showServerRunningDialog,
		_showServicesSelector,
		_selectedServiceOptions,
		connector.isServerRunning
	) { showServerRunning, showSelector, selected, isServerRunning ->
		BLEServerScreenState(
			showServerRunningDialog = showServerRunning,
			showServiceSelector = showSelector,
			isServerRunning = isServerRunning,
			serverServices = selected.toImmutableSet()
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000L),
		initialValue = BLEServerScreenState()
	)

	val connectedClients = connector.connectedDevices
		.map { it.toImmutableList() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000L),
			initialValue = persistentListOf()
		)

	val connectedServices = connector.services
		.map { it.toImmutableList() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000L),
			initialValue = persistentListOf()
		)

	private val _uiEvent = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvent.onStart { readConnectionErrors() }
			.shareIn(viewModelScope, SharingStarted.Eagerly)

	fun onEvent(event: BLEServerScreenEvents) {
		when (event) {
			BLEServerScreenEvents.OnStartServer -> onConnectToServer()
			BLEServerScreenEvents.OnStopServer -> connector.onStopServer()
			is BLEServerScreenEvents.UpdateServiceOptions -> onUpdatedSeverOptionSet(event.option)
			BLEServerScreenEvents.OnCloseServerRunningDialog -> _showServerRunningDialog.update { false }
			BLEServerScreenEvents.OnShowServerRunningDialog -> _showServerRunningDialog.update { true }
			BLEServerScreenEvents.OnToggleServiceSelector -> _showServicesSelector.update { !it }
		}
	}

	private fun onUpdatedSeverOptionSet(option: BLEServerServices) {
		_selectedServiceOptions.update { previous ->
			if (option in previous) previous.filter { it != option }.toSet()
			else previous + option
		}
	}

	private fun onConnectToServer() = viewModelScope.launch {
		val selectedOptions = _selectedServiceOptions.value
		val result = connector.onStartServer(selectedOptions)
		result.fold(
			onSuccess = { _uiEvent.emit(UiEvents.ShowToast("Server Started")) },
			onFailure = { err ->
				_uiEvent.emit(UiEvents.ShowSnackBar(err.message ?: "Error"))
			},
		)
	}

	private fun readConnectionErrors() {
		connector.errorsFlow.onEach { exp ->
			_uiEvent.emit(UiEvents.ShowSnackBar(message = exp.message ?: "Some error"))
		}.launchIn(viewModelScope)
	}

	override fun onCleared() {
		connector.cleanUp()
		super.onCleared()
	}
}