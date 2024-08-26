package com.eva.bluetoothterminalapp.domain.bluetooth.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID

data class BluetoothMessage(
	val message: String,
	val type: BluetoothMessageType,
	val uuid: UUID = UUID.randomUUID(),
) {
	val logTime: Instant
		get() = Clock.System.now()
}