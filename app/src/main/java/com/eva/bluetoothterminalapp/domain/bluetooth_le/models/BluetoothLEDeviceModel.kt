package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel

/**
 * Bluetooth Low energy Device Model represents the base device model, that will be mainly
 * used for scan results, though the results only shows [BluetoothDeviceModel] for now
 * if in future others field are added its created
 * @param deviceModel Represents the [BluetoothDeviceModel]
 * @param deviceName Represents the Device name
 * @see [BluetoothDeviceModel]
 */
data class BluetoothLEDeviceModel(
	val deviceModel: BluetoothDeviceModel,
	val deviceName: String,
	val rssi: Int = 0,
)
