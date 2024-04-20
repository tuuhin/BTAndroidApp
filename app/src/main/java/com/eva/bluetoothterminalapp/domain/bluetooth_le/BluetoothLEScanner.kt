package com.eva.bluetoothterminalapp.domain.bluetooth_le

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Bluetooth Low Energy Scanner
 */
interface BluetoothLEScanner {

	/**
	 * A [StateFlow] of [BluetoothLEDeviceModel]'s which are discovered by the scan results
	 */
	val leDevices: StateFlow<List<BluetoothLEDeviceModel>>

	/**
	 * A [StateFlow] to check is scanning is running
	 */
	val isScanning: StateFlow<Boolean>

	/**
	 * Check if bluetooth low feature is available in the device
	 */
	val hasBTLEFeature: Boolean

	/**
	 * A [Flow] of [ScanError],scan error are the error codes which can occur during a scan.
	 * @see [ScanError]
	 */
	val scanErrorCode: Flow<ScanError>

	/**
	 * Start the scan for device discovery
	 * @param duration Determines the duration for which the scan should run
	 */
	suspend fun startDiscovery(duration: Duration = 8.seconds)

	/**
	 * Stops the running discovery
	 */
	fun stopDiscovery()

	/**
	 * Dispose function for the [BluetoothLEScanner]
	 */
	fun clearResources()
}