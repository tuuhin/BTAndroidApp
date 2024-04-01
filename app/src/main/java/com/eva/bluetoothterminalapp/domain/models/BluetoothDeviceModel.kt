package com.eva.bluetoothterminalapp.domain.models

data class BluetoothDeviceModel(
	val name: String,
	val address: String,
	val mode: BluetoothMode,
	val type: BluetoothDeviceType? = null,
)
