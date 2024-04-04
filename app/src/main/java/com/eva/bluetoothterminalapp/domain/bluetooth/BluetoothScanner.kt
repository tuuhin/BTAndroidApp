package com.eva.bluetoothterminalapp.domain.bluetooth

import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothScanner {

	/**
	 * A flow of list of bounded devices to the bluetooth adapter,all the previously
	 * bounded devices are show here
	 */
	val pairedDevices: StateFlow<List<BluetoothDeviceModel>>

	/**
	 * A flow of list of available devices, on [BluetoothScanner.startScan] the new
	 * devices are returned from this flow
	 */
	val availableDevices: StateFlow<List<BluetoothDeviceModel>>

	/**
	 * A flow to determine if bluetooth is active
	 */
	val isBluetoothActive: Flow<Boolean>

	/**
	 * A flow to determine if scanning is running
	 */
	val isScanRunning: Flow<Boolean>

	/**
	 * Sets the [BluetoothScanner.pairedDevices]
	 * @return [Result.failure] if no permissions is provided
	 */
	fun findPairedDevices(): Result<Unit>

	/**
	 * Starts device scan
	 * @return [Result] which determines is scan has successfully started
	 */
	fun startScan(): Result<Boolean>

	/**
	 * Stops the device scan
	 * @return [Result] determining whether if scan is stopped successfully
	 */
	fun stopScan(): Result<Boolean>
}