package com.eva.bluetoothterminalapp.presentation.feature_le_connect

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileState
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.screens.navArgs
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BLEConnectionViewmodel(
	private val bleConnector: BluetoothLEClientConnector,
	private val savedStateHandle: SavedStateHandle,
) : AppViewModel() {

	val bLEProfile = combine(
		bleConnector.deviceRssi,
		bleConnector.bleServices,
		bleConnector.connectionState,
	) { deviceRssi, services, state ->
		BLEDeviceProfileState(
			connectionState = state,
			device = bleConnector.connectedDevice,
			signalStrength = deviceRssi,
			services = services.toImmutableList()
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(200),
		initialValue = BLEDeviceProfileState()
	)

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	private val navArgs: BluetoothDeviceArgs?
		get() = savedStateHandle.navArgs()

	init {
		navArgs?.address?.let { address ->
			bleConnector.connect(address)
		}
	}


	override fun onCleared() {
		// close the ble connection
		bleConnector.close()
		// clear the viewmodel
		super.onCleared()
	}
}