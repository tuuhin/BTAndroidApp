package com.eva.bluetoothterminalapp.presentation.navigation.args

import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel

fun BluetoothDeviceModel.toArgs() = BluetoothDeviceArgs(address = address)

fun BluetoothLEDeviceModel.toArgs() =
	BluetoothDeviceArgs(address = deviceModel.address, name = deviceName)