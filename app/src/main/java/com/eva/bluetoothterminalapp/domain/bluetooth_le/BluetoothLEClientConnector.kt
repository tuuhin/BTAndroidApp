package com.eva.bluetoothterminalapp.domain.bluetooth_le

import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.exceptions.BLECharacteristicDontHaveIndicateOrNotifyProperties
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

	/**
	 * [BluetoothDeviceModel] for the connected device
	 */
	val connectedDevice: BluetoothDeviceModel?

	/**
	 * Read value for [BLECharacteristicsModel]
	 */
	val readForCharacteristic: Flow<BLECharacteristicsModel?>

	/**
	 * A value indication if [BluetoothLEClientConnector.startIndicationOrNotification] is running
	 * for a characteristic or not
	 */
	val isNotifyOrIndicationRunning: StateFlow<Boolean>


	/**
	 * Initiate a connection with a device
	 * @param address Unique address of the deivce
	 * @param autoConnect Whether the deivce should autoconnect if it comes inside the range
	 */
	fun connect(
		address: String,
		autoConnect: Boolean = false
	): Result<Boolean>


	/**
	 * Reads the charactetistic value for the [BLECharacteristicsModel]
	 * @param service Service in which the characteristic is present
	 * @param characteristic The characteristic to be read for
	 * @return [Result] if the operation is successfully started
	 */
	fun read(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel
	): Result<Boolean>


	/**
	 * Writes the value to a [BLECharacteristicsModel]
	 * @param service Service in which the characteristic is present
	 * @param characteristic The characteristic to write to
	 * @param value The [String] value to be written
	 * @return [Result] indicating operation has successfully started
	 */
	fun write(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		value: String
	): Result<Boolean>


	/**
	 * Reads the value of a descriptor
	 * @param service Service containing the characteristic
	 * @param characteristic Characteristic containing the descriptor
	 * @param descriptor [BLEDescriptorModel] descriptor to read from
	 * @return [Result] Is the operation has started successfully
	 */
	fun readDescriptor(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		descriptor: BLEDescriptorModel,
	): Result<Boolean>


	/**
	 * Indication or Notification for a characteristics
	 * @param service Service containing the characteristic
	 * @param characteristic Characteristic containing property of
	 * either [BLEPropertyTypes.PROPERTY_INDICATE] or [BLEPropertyTypes.PROPERTY_NOTIFY] otherwise
	 * reults will Result failure of  [BLECharacteristicDontHaveIndicateOrNotifyProperties]
	 * @param enable Set [true] to start listening to characteristic changes and [false] to stop
	 * listening.
	 * @return [Result] indicating operation has started successfully
	 */
	fun startIndicationOrNotification(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		enable: Boolean,
	): Result<Boolean>


	/**
	 * Rediscover available services for the device
	 * @return [Result] If the operation done correctly
	 */
	fun discoverServices(): Result<Boolean>


	/**
	 * Re-evalutae the value of rssi
	 * @return [Result] If the operation done correctly
	 */
	fun checkRssi(): Result<Boolean>


	/**
	 * Reconnects with the deivce if its disconnected
	 * @return [Result] If the operation done correctly
	 */
	fun reconnect(): Result<Boolean>


	/**
	 * Disconnects with the deivce
	 * @return [Result] If the operation done correctly
	 */
	fun disconnect(): Result<Unit>


	/**
	 * Close the gatt connection
	 */
	fun close()

}