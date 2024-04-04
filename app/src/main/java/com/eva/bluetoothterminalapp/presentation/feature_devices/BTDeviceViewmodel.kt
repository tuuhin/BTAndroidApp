package com.eva.bluetoothterminalapp.presentation.feature_devices

import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothScanner
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenState
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.DeviceScreenEvents
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BTDeviceViewmodel(
	private val bluetoothScanner: BluetoothScanner
) : AppViewModel() {

	private val _screenState = BTDevicesScreenState()

	val screenState = combine(
		bluetoothScanner.isBluetoothActive,
		bluetoothScanner.pairedDevices,
		bluetoothScanner.availableDevices, bluetoothScanner.isScanRunning
	) { btActive, paired, available, isScanning ->
		_screenState.copy(
			isScanning = isScanning,
			isBtActive = btActive,
			pairedDevices = paired.toPersistentList(),
			availableDevices = available.toPersistentList()
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = BTDevicesScreenState()
	)

	private val _uiEvents = MutableSharedFlow<UiEvents>()

	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	init {
		val status = bluetoothScanner.findPairedDevices()
		if (status.isFailure) {
			val exp = status.exceptionOrNull()
			viewModelScope.launch {
				_uiEvents.emit(UiEvents.ShowSnackBar(exp?.message ?: "NO PERMISSION PROVIDED"))
			}
		}
	}

	fun onEvents(event: DeviceScreenEvents) {
		when (event) {
			DeviceScreenEvents.StartScan -> startDeviceScanner()
			DeviceScreenEvents.StopScan -> stopDeviceScanner()
		}
	}

	private fun startDeviceScanner() {
		val status = bluetoothScanner.startScan()
		viewModelScope.launch {
			if (status.isFailure) {
				val exception = status.exceptionOrNull()
				_uiEvents.emit(
					UiEvents.ShowSnackBar(exception?.message ?: "SOME ERROR OCCURRED")
				)
			} else {
				val isStated = status.getOrDefault(false)
				if (isStated) _uiEvents.emit(UiEvents.ShowToast("Scan started"))
				else _uiEvents.emit(UiEvents.ShowToast("Scan cannot be stated"))
			}
		}
	}

	private fun stopDeviceScanner() {
		val status = bluetoothScanner.stopScan()
		viewModelScope.launch {
			if (status.isFailure) {
				val exception = status.exceptionOrNull()
				_uiEvents.emit(
					UiEvents.ShowSnackBar(exception?.message ?: "SOME ERROR OCCURRED")
				)
			} else {
				val isStated = status.getOrDefault(false)
				if (isStated) _uiEvents.emit(UiEvents.ShowToast("Scan stopped"))
				else _uiEvents.emit(UiEvents.ShowToast("Scan cannot be stopped"))
			}
		}
	}

}