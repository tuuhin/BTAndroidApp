package com.eva.bluetoothterminalapp.domain.bluetooth.models

import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class BluetoothMessage(
	val message: String,
	val type: BluetoothMessageType,
	val uuid: Uuid = Uuid.random(),
) {
	val logTime: Instant
		get() = Clock.System.now()
}