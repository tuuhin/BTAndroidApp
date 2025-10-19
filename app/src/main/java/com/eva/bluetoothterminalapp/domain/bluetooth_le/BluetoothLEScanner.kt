package com.eva.bluetoothterminalapp.domain.bluetooth_le

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.ScanError
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

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
	 */
	suspend fun startDiscovery()

	/**
	 * Stops the running discovery
	 */
	fun stopDiscovery()

	/**
	 * Dispose function for the [BluetoothLEScanner]
	 */
	fun clearResources()
}