package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEDeviceModel

fun ScanResult.toDomainModel(): BluetoothLEDeviceModel = BluetoothLEDeviceModel(
	deviceModel = device.toDomainModel(),
	deviceName = scanRecord?.deviceName ?: "unnamed",
	deviceUUIDs = scanRecord?.serviceUuids?.mapNotNull(ParcelUuid::getUuid)
		?: emptyList()
)