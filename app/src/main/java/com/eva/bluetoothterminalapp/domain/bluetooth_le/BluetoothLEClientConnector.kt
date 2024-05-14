package com.eva.bluetoothterminalapp.domain.bluetooth_le

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothLEClientConnector {

	/**
	 * Checks the connection status of the device
	 * @see [BLEConnectionState]
	 */
	val connectionState: StateFlow<BLEConnectionState>

	/**
	 * Receive signal strenght of the device
	 */
	val deviceRssi: StateFlow<Int>

	/**
	 * Available [BLEServiceModel] with the current selected service
	 */
	val bleServices: Flow<List<BLEServiceModel>>

	val connectedDevice: BluetoothDeviceModel?

	/**
	 * Read value for [BLECharacteristicsModel]
	 */
	val readForCharacteristic: Flow<BLECharacteristicsModel?>

	/**
	 * Initiate a connection with a device
	 * @param address Unique address of the deivce
	 * @param autoConnect Whether the deivce should autoconnect if it comes inside the range
	 */
	fun connect(address: String, autoConnect: Boolean = false): Result<Boolean>

	fun read(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel
	): Result<Boolean>

	fun write(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		value: String
	): Result<Boolean>

	fun readDescriptor(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		descriptor: BLEDescriptorModel,
	): Result<Boolean>

	fun setIndicationOrNotification(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		enable: Boolean,
	): Result<Boolean>


	fun discoverServices(): Result<Boolean>

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