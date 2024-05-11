package com.eva.bluetoothterminalapp.domain.bluetooth_le

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.CharacteristicsReadValue
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.coroutines.flow.Flow
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


	val bleServices: Flow<List<BLEServiceModel>>

	val connectedDevice: BluetoothDeviceModel?

	val readValue: StateFlow<CharacteristicsReadValue>

	fun connect(address: String): Result<Boolean>

	fun read(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel
	): Result<Boolean>

	fun write(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		value: String
	): Result<Boolean>

	fun setNotification(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		enable: Boolean,
	): Result<Boolean>

	/**
	 * Re-evalutae the value of rssi
	 */
	fun checkRssi(): Result<Boolean>

	/**
	 * Reconnects with the deivce if its disconnected
	 */
	fun reconnect(): Result<Boolean>

	/**
	 * Disconnects with the deivce
	 */
	fun disconnect(): Result<Unit>

	/**
	 * Close the gatt connection
	 */
	fun close()

}