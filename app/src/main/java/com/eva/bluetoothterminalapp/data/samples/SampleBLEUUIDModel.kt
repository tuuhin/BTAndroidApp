package com.eva.bluetoothterminalapp.data.samples

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SampleBLEUUIDModel(
	@SerialName("uuid") val uuid: Int,
	@SerialName("name") val name: String,
	@SerialName("id") val id: String
) {
	val uuid128Bits: UUID
		get() = UUID.fromString(String.format("%08x-0000-1000-8000-00805f9b34fb", uuid))
}

