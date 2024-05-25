package com.eva.bluetoothterminalapp.presentation.util

import java.util.UUID

object BTConstants {

	const val SERVICE_NAME = "bt-communication"
	val SERVICE_UUID: UUID = UUID.fromString("3fe6c764-029f-48f0-a2d0-a43d9b1df5c8")
	val CLIENT_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
}