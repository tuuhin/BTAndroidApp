package com.eva.bluetoothterminalapp.presentation.feature_devices.state

import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class BTDevicesScreenState(
	val isBtActive: Boolean = false,
	val isScanning: Boolean = false,
	val pairedDevices: ImmutableList<BluetoothDeviceModel> = persistentListOf(),
	val availableDevices: ImmutableList<BluetoothDeviceModel> = persistentListOf()
)
