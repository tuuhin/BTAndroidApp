package com.eva.bluetoothterminalapp.domain.settings.models

import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalCharSet
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalDisplayMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar

data class BTSettingsModel(
	val charset: BTTerminalCharSet = BTTerminalCharSet.CHAR_SET_UTF_8,
	val showTimeStamp: Boolean = false,
	val displayMode: BTTerminalDisplayMode = BTTerminalDisplayMode.DISPLAY_MODE_TEXT,
	val newLineChar: BTTerminalNewLineChar = BTTerminalNewLineChar.NEW_LINE_CR_LF,
)
