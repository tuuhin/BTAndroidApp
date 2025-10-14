package com.eva.bluetoothterminalapp.data.ble_server

sealed class BLENotificationModel(val type: BLENotification) {

	abstract val isEnabled: Boolean

	data class BLEBatteryNotification(
		override val isEnabled: Boolean,
	) : BLENotificationModel(type = BLENotification.BATTERY)

	data class BLEIlluminanceNotification(
		override val isEnabled: Boolean,
	) : BLENotificationModel(type = BLENotification.ILLUMINANCE)

	enum class BLENotification {
		BATTERY,
		ILLUMINANCE
	}
}
