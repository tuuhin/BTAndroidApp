package com.eva.bluetoothterminalapp.domain.bluetooth_le

import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import java.util.UUID

data class BluetoothLEDeviceModel(
	val deviceModel: BluetoothDeviceModel,
	val deviceName: String,
	val deviceUUIDs: List<UUID> = emptyList(),
)
