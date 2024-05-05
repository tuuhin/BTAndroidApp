package com.eva.bluetoothterminalapp.data.samples

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SampleBLEUUIDModel(
	@SerialName("name") val name: String,
	@SerialName("code") val id: String
) {
	private val baseUUIDString = "%08x-0000-1000-8000-00805f9b34fb"

	val uuid128Bits: UUID
		get() = UUID.fromString(String.format(baseUUIDString, Integer.decode(id)))

}

