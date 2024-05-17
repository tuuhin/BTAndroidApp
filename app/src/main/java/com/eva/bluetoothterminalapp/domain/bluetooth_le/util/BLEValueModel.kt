package com.eva.bluetoothterminalapp.domain.bluetooth_le.util

import java.nio.charset.Charset

abstract class BLEValueModel(private val value: ByteArray) {

	private val charset: Charset
		get() = Charsets.US_ASCII

	val valueAsString: String?
		get() = value.decodeToString().let { decoded ->
			if (!charset.newEncoder().canEncode(decoded)) null
			else if (value.all { it.toInt() == 0x0 }) null
			else decoded
		}


	val valueHexString: String
		get() = if (value.isEmpty()) ""
		else value.joinToString(separator = "-", prefix = "0x-") { byte ->
			String.format("%02x", byte)
		}
}