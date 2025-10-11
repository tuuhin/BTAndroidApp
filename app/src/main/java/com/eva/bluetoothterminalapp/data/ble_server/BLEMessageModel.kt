package com.eva.bluetoothterminalapp.data.ble_server

data class BLEMessageModel(
	val message: String = "",
	val isNotifyEnabled: Boolean = false,
)