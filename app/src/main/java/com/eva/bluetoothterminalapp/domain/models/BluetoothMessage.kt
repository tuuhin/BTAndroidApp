package com.eva.bluetoothterminalapp.domain.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class BluetoothMessage(
	val message: String,
	val type: BluetoothMessageType,
	val logTime: Instant = Clock.System.now(),
)