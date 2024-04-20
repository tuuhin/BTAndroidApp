package com.eva.bluetoothterminalapp.presentation.feature_devices

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothScanner
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEScanner
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenEvents
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenState
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BTDeviceViewmodel(
	private val bluetoothScanner: BluetoothScanner,
	private val bLEScanner: BluetoothLEScanner,
) : AppViewModel() {

	private val isBLEScanningRunning: Boolean
		get() = bLEScanner.isScanning.value

	val isBTActive = bluetoothScanner.isBluetoothActive.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Eagerly,
		initialValue = false
	)

	val isScanning = merge(
		bluetoothScanner.isScanRunning,
		bLEScanner.isScanning
	).stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(200),
		initialValue = false
	)

	val screenState = combine(
		bluetoothScanner.pairedDevices,
		bluetoothScanner.availableDevices,
		bLEScanner.leDevices,
	) { paired, available, leDevices ->
		BTDevicesScreenState(
			pairedDevices = paired.toPersistentList(),
			availableDevices = available.toPersistentList(),
			leDevices = leDevices.toPersistentList()
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
		//read errors
		checkLEScanFailedReasons()
	}

	fun onEvents(event: BTDevicesScreenEvents) {
		when (event) {
			BTDevicesScreenEvents.StartScan -> startNormalScan()
			BTDevicesScreenEvents.StopScan -> stopNormalScan()
			is BTDevicesScreenEvents.OnStopAnyRunningScan -> onStopAnyRunningScan()
			BTDevicesScreenEvents.StartLEDeviceScan -> startLEScan()
			BTDevicesScreenEvents.StopLEDevicesScan -> stopLEScan()
		}
	}

	private fun onStopAnyRunningScan() {
		// stop running scan
		when {
			// stop normal scan if its running
			bluetoothScanner.isBTDiscovering -> stopNormalScan()
			// stop le scan if its running
			isBLEScanningRunning -> stopLEScan()
		}
	}


	private fun startLEScan() = viewModelScope.launch {
		bLEScanner.startDiscovery()
	}

	private fun stopLEScan() = bLEScanner.stopDiscovery()

	private fun checkLEScanFailedReasons() = viewModelScope.launch {
		bLEScanner.scanErrorCode.onEach { error ->
			_uiEvents.emit(UiEvents.ShowSnackBar(message = error.name))
		}.launchIn(this)
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

	private fun startNormalScan() = viewModelScope.launch {
		// then start normal scan
		val status = bluetoothScanner.startScan()
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


	private fun stopNormalScan() = viewModelScope.launch {
		val status = bluetoothScanner.stopScan()
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


	override fun onCleared() {
		// release resources when done
		bLEScanner.clearResources()
		bluetoothScanner.releaseResources()
		Log.d("VIEWMODEL", "CLEARED")
		super.onCleared()
	}

}