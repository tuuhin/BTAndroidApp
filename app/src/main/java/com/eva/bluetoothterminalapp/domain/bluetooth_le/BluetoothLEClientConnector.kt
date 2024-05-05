package com.eva.bluetoothterminalapp.domain.bluetooth_le

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.coroutines.flow.StateFlow

interface BluetoothLEClientConnector {

	/**
	 * A flow which determines the device connection state
	 * @see [BLEConnectionState]
	 */
	val connectionState: StateFlow<BLEConnectionState>

	/**
	 * A flow determining the signal screenshot of the device
	 */
	val deviceRssi: StateFlow<Int>


	val bleServices: StateFlow<List<BLEServiceModel>>

	val connectedDevice: BluetoothDeviceModel?

	fun connect(address: String): Result<Boolean>

	fun reconnect(): Result<Boolean>

	fun disconnect(): Result<Unit>

	fun close()

}