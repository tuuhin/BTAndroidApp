package com.eva.bluetoothterminalapp.domain.settings.enums

enum class BTTerminalNewLineChar(val endBytes: ByteArray?) {
	NEW_LINE_CR_LF(byteArrayOf(0x0D, 0x0A)),
	NEW_LINE_CR(byteArrayOf(0xD)),
	NEW_LINE_LF(byteArrayOf(0x0A)),
	NEW_LINE_END_OF_TEXT(byteArrayOf(0x02, 0x03)),
	NEW_LINE_NULL_CHR(byteArrayOf(0x0)),
	NEW_LINE_NONE(null);
}