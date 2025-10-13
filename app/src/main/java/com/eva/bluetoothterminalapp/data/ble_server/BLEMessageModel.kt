package com.eva.bluetoothterminalapp.data.ble_server

sealed class BLEMessageModel(
	open val message: String = "",
	open val isNotifyEnabled: Boolean = false,
) {

	data class EchoMessage(
		override val message: String,
		override val isNotifyEnabled: Boolean
	) : BLEMessageModel(message = message, isNotifyEnabled = isNotifyEnabled)

	data class NUSMessage(
		val rxMessage: String,
		override val isNotifyEnabled: Boolean
	) : BLEMessageModel(message = rxMessage, isNotifyEnabled = isNotifyEnabled) {

		val txMessage: String
			get() = "TX:$message"
	}
}