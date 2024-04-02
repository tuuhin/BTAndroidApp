package com.eva.bluetoothterminalapp.domain.bluetooth

import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothScanner {

	/**
	 * Provides a list of previously bounded devices
	 */
	val pairedDevices: StateFlow<List<BluetoothDeviceModel>>

	/**
	 * Provides a list of available devices
	 */
	val availableDevices: StateFlow<List<BluetoothDeviceModel>>

	val isBluetoothActive: Flow<Boolean>

	fun findPairedDevices()

	fun startScan()

	fun stopScan()
}