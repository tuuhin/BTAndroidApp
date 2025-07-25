package com.eva.bluetoothterminalapp.presentation.feature_devices

import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothScanner
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEScanner
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenEvents
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenState
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BTDeviceViewmodel(
	private val bluetoothScanner: BluetoothScanner,
	private val bLEScanner: BluetoothLEScanner,
) : AppViewModel() {

	private val _isPairedDevicesReady = MutableStateFlow(false)

	val isBTActive = bluetoothScanner.isBluetoothActive
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = false
		)

	val isScanning = merge(bluetoothScanner.isScanRunning, bLEScanner.isScanning)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = false
		)

	val screenState = combine(
		bluetoothScanner.pairedDevices,
		bluetoothScanner.availableDevices,
		bLEScanner.leDevices,
		_isPairedDevicesReady,
	) { paired, available, leDevices, pairedDevicesLoaded ->
		BTDevicesScreenState(
			pairedDevices = paired.toPersistentList(),
			isPairedDevicesLoaded = pairedDevicesLoaded,
			availableDevices = available.toPersistentList(),
			leDevices = leDevices.toPersistentList()
		)
	}.onStart {
		// load paired devices
		setPairedDevices()
		//read errors
		checkLEScanFailedReasons()
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(2000),
		initialValue = BTDevicesScreenState()
	)

	private val _hasBtPermission = MutableStateFlow(bluetoothScanner.hasBTPermissions)

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()


	fun onEvents(event: BTDevicesScreenEvents) {
		when (event) {
			BTDevicesScreenEvents.StartScan -> startBTClassicScan()
			BTDevicesScreenEvents.StopScan -> stopClassicScan()
			BTDevicesScreenEvents.OnStopAnyRunningScan -> onStopAnyRunningScan()
			BTDevicesScreenEvents.StartLEDeviceScan -> startLEScan()
			BTDevicesScreenEvents.StopLEDevicesScan -> stopLEScan()
			is BTDevicesScreenEvents.OnBTPermissionChanged -> _hasBtPermission.update { event.isGranted }
			is BTDevicesScreenEvents.OnLocationPermissionChanged -> {
				// nothing to be look for
			}
		}
	}

	private fun onStopAnyRunningScan() {
		// stop running scan
		when {
			// stop normal scan if its running
			bluetoothScanner.isBTDiscovering -> stopClassicScan()
			// stop le scan if its running
			bLEScanner.isScanning.value -> stopLEScan()
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
		merge(bluetoothScanner.isBluetoothActive, _hasBtPermission).onEach { canCheck ->
			if (!canCheck) return@onEach
			val status = bluetoothScanner.findPairedDevices()
			status.fold(
				onSuccess = { _isPairedDevicesReady.update { true } },
				onFailure = { exp ->
					val message = exp.message ?: "Some issues in loading paired devices"
					viewModelScope.launch {
						_uiEvents.emit(UiEvents.ShowSnackBar(message))
					}
				},
			)
		}.launchIn(viewModelScope)
	}

	private fun startBTClassicScan() = viewModelScope.launch {
		// then start normal scan
		val status = bluetoothScanner.startScan()
		status.fold(
			onSuccess = { hasStarted ->
				val message = if (hasStarted) "Scan started" else "Scan cannot be stated"
				_uiEvents.emit(UiEvents.ShowToast(message))
			},
			onFailure = { exception ->
				_uiEvents.emit(
					UiEvents.ShowSnackBar(exception.message ?: "SOME ERROR OCCURRED")
				)
			},
		)
	}


	private fun stopClassicScan() = viewModelScope.launch {
		val status = bluetoothScanner.stopScan()
		status.fold(
			onSuccess = { stopped ->
				val message = if (stopped) "Scan stopped" else "Scan cannot be stoped"
				_uiEvents.emit(UiEvents.ShowToast(message))
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
		super.onCleared()
	}
}