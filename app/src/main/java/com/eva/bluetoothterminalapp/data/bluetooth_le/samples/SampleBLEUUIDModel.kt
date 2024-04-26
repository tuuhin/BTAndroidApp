package com.eva.bluetoothterminalapp.data.bluetooth_le.samples

import java.util.UUID

data class SampleBLEUUIDModel(
	val uuid: Int,
	val name: String,
	val id: String
) {
	val uuid128Bits: UUID
		get() = UUID.fromString(String.format("%08x-0000-1000-8000-00805f9b34fb", uuid))
}