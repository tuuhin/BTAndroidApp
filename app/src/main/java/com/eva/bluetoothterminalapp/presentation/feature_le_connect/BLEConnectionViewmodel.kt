package com.eva.bluetoothterminalapp.presentation.feature_le_connect

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileState
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.screens.navArgs
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class BLEConnectionViewmodel(
	private val bleConnector: BluetoothLEClientConnector,
	private val savedStateHandle: SavedStateHandle,
) : AppViewModel() {

	private val _selectedCharacteristic = MutableStateFlow<BLECharacteristicsModel?>(null)
	val selectedCharacteric = _selectedCharacteristic.asStateFlow()

	val bLEProfile = combine(
		bleConnector.deviceRssi,
		bleConnector.bleServices,
		bleConnector.connectionState,
	) { deviceRssi, services, connectState ->
		BLEDeviceProfileState(
			connectionState = connectState,
			device = bleConnector.connectedDevice,
			signalStrength = deviceRssi,
			services = services.toImmutableList()
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Eagerly,
		initialValue = BLEDeviceProfileState()
	)

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	private val navArgs: BluetoothDeviceArgs?
		get() = savedStateHandle.navArgs()

	init {
		navArgs?.address?.let(bleConnector::connect)
	}

	fun onEvent(event: BLEDeviceProfileEvent) {
		when (event) {
			BLEDeviceProfileEvent.OnCharcteristicsConfirmed -> {}
			is BLEDeviceProfileEvent.OnSelectCharacteristic -> _selectedCharacteristic
				.update { event.characteristics }
		}
	}


	override fun onCleared() {
		// close the ble connection
		bleConnector.close()
		// clear the viewmodel
		super.onCleared()
	}
}