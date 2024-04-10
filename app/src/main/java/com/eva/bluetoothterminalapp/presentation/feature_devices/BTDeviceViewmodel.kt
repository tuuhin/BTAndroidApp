package com.eva.bluetoothterminalapp.presentation.feature_devices

import android.util.Log
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BTDeviceViewmodel(
	private val bluetoothScanner: BluetoothScanner
) : AppViewModel() {

	val screenState = combine(
		bluetoothScanner.isBluetoothActive,
		bluetoothScanner.pairedDevices,
		bluetoothScanner.availableDevices, bluetoothScanner.isScanRunning
	) { btActive, paired, available, isScanning ->
		BTDevicesScreenState(
			isScanning = isScanning,
			isBtActive = btActive,
			pairedDevices = paired.toPersistentList(),
			availableDevices = available.toPersistentList()
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(2000),
		initialValue = BTDevicesScreenState()
	)

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	init {
		// load paired devices
		setPairedDevices()
	}

	fun onEvents(event: DeviceScreenEvents) {
		when (event) {
			DeviceScreenEvents.StartScan -> startDeviceScanner()
			DeviceScreenEvents.StopScan -> stopDeviceScanner()
		}
	}

	private fun setPairedDevices() {
		bluetoothScanner.isBluetoothActive.onEach { isActive ->
			if (!isActive) return@onEach
			val status = bluetoothScanner.findPairedDevices()
			status.onFailure { exp ->
				viewModelScope.launch {
					_uiEvents.emit(UiEvents.ShowSnackBar(exp.message ?: "NO PERMISSION PROVIDED"))
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun startDeviceScanner() {
		val status = bluetoothScanner.startScan()
		viewModelScope.launch {
			status.fold(
				onSuccess = { hasStarted ->
					if (hasStarted) _uiEvents.emit(UiEvents.ShowToast("Scan started"))
					else _uiEvents.emit(UiEvents.ShowToast("Scan cannot be stated"))
				},
				onFailure = { exception ->
					_uiEvents.emit(
						UiEvents.ShowSnackBar(exception.message ?: "SOME ERROR OCCURRED")
					)
				},
			)
		}
	}

	private fun stopDeviceScanner() {
		val status = bluetoothScanner.stopScan()
		viewModelScope.launch {
			status.fold(
				onSuccess = { isStarted ->
					if (isStarted) _uiEvents.emit(UiEvents.ShowToast("Scan stopped"))
					else _uiEvents.emit(UiEvents.ShowToast("Scan cannot be stopped"))
				},
				onFailure = { exception ->
					_uiEvents.emit(
						UiEvents.ShowSnackBar(exception.message ?: "SOME ERROR OCCURRED")
					)
				},
			)
		}
	}

	override fun onCleared() {
		// release resources when done
		bluetoothScanner.releaseResources()
		Log.d("VIEWMODEL", "CLEARED")
		super.onCleared()
	}

}