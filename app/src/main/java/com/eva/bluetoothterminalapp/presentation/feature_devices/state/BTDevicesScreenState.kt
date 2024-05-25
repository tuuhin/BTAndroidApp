package com.eva.bluetoothterminalapp.presentation.feature_devices.state

import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class BTDevicesScreenState(
	val pairedDevices: ImmutableList<BluetoothDeviceModel> = persistentListOf(),
	val availableDevices: ImmutableList<BluetoothDeviceModel> = persistentListOf(),
	val leDevices: ImmutableList<BluetoothLEDeviceModel> = persistentListOf()
)
