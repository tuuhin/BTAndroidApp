package com.eva.bluetoothterminalapp.domain.bluetooth.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class BluetoothMessage(
	val message: String,
	val type: BluetoothMessageType,
) {
	val logTime: Instant
		get() = Clock.System.now()
}