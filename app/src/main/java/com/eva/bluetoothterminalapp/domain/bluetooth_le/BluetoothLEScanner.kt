package com.eva.bluetoothterminalapp.domain.bluetooth_le

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


interface BluetoothLEScanner {

	val leDevices: StateFlow<List<BluetoothLEDeviceModel>>

	val isScanning: StateFlow<Boolean>

	val hasBTLEFeature: Boolean

	val scanErrorCode: Flow<ScanError>

	/**
	 * Start the scan for device discovery
	 */
	suspend fun startDiscovery(duration: Duration = 12.seconds)

	/**
	 * Stops scan
	 */
	fun stopDiscovery()

	fun clearResources()
}