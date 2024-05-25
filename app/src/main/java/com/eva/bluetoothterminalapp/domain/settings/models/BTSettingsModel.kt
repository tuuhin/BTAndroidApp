package com.eva.bluetoothterminalapp.domain.settings.models

import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalCharSet
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalDisplayMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar

data class BTSettingsModel(
	val btTerminalCharSet: BTTerminalCharSet = BTTerminalCharSet.CHAR_SET_UTF_8,
	val showTimeStamp: Boolean = false,
	val displayMode: BTTerminalDisplayMode = BTTerminalDisplayMode.DISPLAY_MODE_TEXT,
	val newLineCharReceive: BTTerminalNewLineChar = BTTerminalNewLineChar.NEW_LINE_LF,
	val newLineCharSend: BTTerminalNewLineChar = BTTerminalNewLineChar.NEW_LINE_LF,
	val autoScrollEnabled: Boolean = false,
	val localEchoEnabled: Boolean = false,
	val clearInputOnSend: Boolean = false,
	val keepScreenOnWhenConnected: Boolean = false,
)
