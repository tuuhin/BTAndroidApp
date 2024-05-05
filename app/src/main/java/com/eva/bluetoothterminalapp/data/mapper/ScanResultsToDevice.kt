package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.le.ScanResult
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel

fun ScanResult.toDomainModel(): BluetoothLEDeviceModel = BluetoothLEDeviceModel(
	deviceModel = device.toDomainModel(),
	deviceName = scanRecord?.deviceName ?: BluetoothDeviceModel.UNNAMED_DEVICE_NAME,
	rssi = rssi,
)