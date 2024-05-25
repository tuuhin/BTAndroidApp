package com.eva.bluetoothterminalapp.domain.bluetooth.models

import com.eva.bluetoothterminalapp.domain.bluetooth.enums.BluetoothDeviceType
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.BluetoothMode

data class BluetoothDeviceModel(
	val name: String,
	val address: String,
	val mode: BluetoothMode,
	val type: BluetoothDeviceType? = null,
) {
	companion object {
		const val UNNAMED_DEVICE_NAME = "unnamed"
		const val RSSI_UNIT = "dbM"
	}
}
