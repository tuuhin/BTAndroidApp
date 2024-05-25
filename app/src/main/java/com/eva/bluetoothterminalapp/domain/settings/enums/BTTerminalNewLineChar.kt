package com.eva.bluetoothterminalapp.domain.settings.enums

enum class BTTerminalNewLineChar(val value: String?) {
	NEW_LINE_CR_LF("\r\n"),
	NEW_LINE_CR("\r"),
	NEW_LINE_LF("\n"),
	NEW_LINE_END_OF_TEXT("\u0002\u0003"),
	NEW_LINE_NULL_CHR(null),
	NEW_LINE_NONE(null);
}