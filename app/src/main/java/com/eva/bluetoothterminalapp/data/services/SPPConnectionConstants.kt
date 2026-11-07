package com.eva.bluetoothterminalapp.data.services

object SPPConnectionConstants {
	const val ACTION_SPP_SEND = "com.eva.bluetoothterminalapp.action.SPP_SEND"
	const val EXTRA_DEVICE_ADDRESS = "device_address"
	const val EXTRA_SPP_UUID = "spp_uuid"
	const val EXTRA_PAYLOAD = "bytes_payload"
	const val EXTRA_PAYLOAD_HEX = "hex_payload"


	fun hexToByteArray(hex: String): ByteArray {
		require(hex.isNotEmpty() && hex.length % 2 == 0 && hex.all { it.digitToIntOrNull(16) != null }) {
			"Hex string must be non-empty, even-length hexadecimal"
		}
		return hex.chunked(2)
			.map { it.toInt(16).toByte() }
			.toByteArray()
	}
}