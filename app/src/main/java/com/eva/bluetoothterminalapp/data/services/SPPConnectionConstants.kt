package com.eva.bluetoothterminalapp.data.services

import kotlin.uuid.Uuid

object SPPConnectionConstants {

	const val ACTION_SPP_SEND = "com.eva.bluetoothterminalapp.action.SPP_SEND"
	const val ACTION_SPP_DISCONNECT = "com.eva.bluetoothterminalapp.action.SPP_DISCONNECT"
	const val EXTRA_DEVICE_ADDRESS = "device_address"
	const val EXTRA_SPP_UUID = "spp_uuid"
	const val EXTRA_PAYLOAD = "bytes_payload"
	const val EXTRA_PAYLOAD_HEX = "hex_payload"


	fun validateArgs(address: String?, uuid: String?, payload: ByteArray?, payloadHex: String?) {

		if (address.isNullOrBlank()) throw IllegalArgumentException("Address cannot be null or blank")
		if (!macHex.matches(address)) throw IllegalArgumentException("Invalid mac address")

		// check valid spp uuid
		try {
			if (uuid.isNullOrBlank()) throw IllegalArgumentException("Cannot parse empty uuid")
			Uuid.parse(uuid)
		} catch (e: IllegalArgumentException) {
			throw e
		}

		// check the payload
		val decodedPayload = when {
			payload != null -> payload
			!payloadHex.isNullOrBlank() -> hexToByteArray(payloadHex)
			else -> throw IllegalArgumentException("Missing payload")
		}

		require(decodedPayload.isNotEmpty()) { "Payload cannot be empty" }
	}


	fun hexToByteArray(hex: String): ByteArray {

		if (hex.isEmpty()) throw IllegalArgumentException("Hex bytes are empty")
		if (hex.length % 2 != 0) throw IllegalArgumentException("Invalids hex input should be multiple of 2")
		if (hex.any { it.digitToIntOrNull(16) == null }) throw IllegalArgumentException("Invalid payload should be hexadecimal")

		return hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
	}

	private val macHex = Regex("^([0-9A-Fa-f]{2}:){5}([0-9A-Fa-f]{2})$")

}