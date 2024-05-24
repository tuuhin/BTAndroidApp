package com.eva.bluetoothterminalapp.domain.settings.enums

import java.nio.charset.Charset

enum class BTTerminalCharSet(val charset: Charset) {
	CHAR_SET_UTF_8(Charsets.UTF_8),
	CHAR_SET_UTF_16(Charsets.UTF_16),
	CHAR_SET_UTF_32(Charsets.UTF_32),
	CHAR_SET_US_ASCII(Charsets.US_ASCII),
	CHAR_SET_ISO_8859_1(Charsets.ISO_8859_1)
}
