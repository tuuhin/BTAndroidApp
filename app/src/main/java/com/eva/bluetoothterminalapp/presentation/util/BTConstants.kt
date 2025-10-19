package com.eva.bluetoothterminalapp.presentation.util

import java.util.UUID

object BTConstants {

	const val SERVICE_NAME = "bt-communication"
	val SERVICE_UUID: UUID = UUID.fromString("3fe6c764-029f-48f0-a2d0-a43d9b1df5c8")

	const val BLE_ASSIGNED_NUMBERS_WEBSITE =
		"https://www.bluetooth.com/specifications/assigned-numbers"

	const val SOURCE_CODE_REPO = "https://github.com/tuuhin/BTAndroidApp"
}