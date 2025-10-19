package com.eva.bluetoothterminalapp.data.ble_server

sealed class BLEMessageModel(open val message: String) {

	abstract val isNotifyEnabled: Boolean

	data class EchoMessage(
		override val message: String,
		override val isNotifyEnabled: Boolean
	) : BLEMessageModel(message = message)

	data class NUSMessage(
		val rxMessage: String,
		override val isNotifyEnabled: Boolean
	) : BLEMessageModel(message = rxMessage) {

		val txMessage: String
			get() = "TX:$message"
	}
}