package com.eva.bluetoothterminalapp.data.mapper

import com.eva.bluetoothterminalapp.data.datastore.BT_CharSet
import com.eva.bluetoothterminalapp.data.datastore.BT_Display_Mode
import com.eva.bluetoothterminalapp.data.datastore.BT_NewLine_Char
import com.eva.bluetoothterminalapp.data.datastore.BluetoothClassicSettings
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalCharSet
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalDisplayMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
fun BluetoothClassicSettings.toModel(): BTSettingsModel = BTSettingsModel(
	showTimeStamp = showTimeStamp,
	newLineChar = when (newLine) {
		BT_NewLine_Char.NEW_LINE_CR_LF -> BTTerminalNewLineChar.NEW_LINE_CR_LF
		BT_NewLine_Char.NEW_LINE_CR -> BTTerminalNewLineChar.NEW_LINE_CR
		BT_NewLine_Char.NEW_LINE_LF -> BTTerminalNewLineChar.NEW_LINE_LF
		BT_NewLine_Char.NEW_LINE_END_OF_TEXT -> BTTerminalNewLineChar.NEW_LINE_END_OF_TEXT
		BT_NewLine_Char.NEW_LINE_NULL_CHR -> BTTerminalNewLineChar.NEW_LINE_NULL_CHR
		BT_NewLine_Char.NEW_LINE_NONE -> BTTerminalNewLineChar.NEW_LINE_NONE
		BT_NewLine_Char.UNRECOGNIZED -> BTTerminalNewLineChar.NEW_LINE_CR_LF
	},
	charset = when (charset) {
		BT_CharSet.CHAR_SET_UTF_8 -> BTTerminalCharSet.CHAR_SET_UTF_8
		BT_CharSet.CHAR_SET_UTF_16 -> BTTerminalCharSet.CHAR_SET_UTF_16
		BT_CharSet.CHAR_SET_UTF_32 -> BTTerminalCharSet.CHAR_SET_UTF_32
		BT_CharSet.CHAR_SET_US_ASCII -> BTTerminalCharSet.CHAR_SET_US_ASCII
		BT_CharSet.CHAR_SET_ISO_8859_1 -> BTTerminalCharSet.CHAR_SET_ISO_8859_1
		BT_CharSet.UNRECOGNIZED -> BTTerminalCharSet.CHAR_SET_UTF_8
	},
	displayMode = when (displayMode) {
		BT_Display_Mode.DISPLAY_MODE_TEXT -> BTTerminalDisplayMode.DISPLAY_MODE_TEXT
		BT_Display_Mode.DISPLAY_MODE_HEX -> BTTerminalDisplayMode.DISPLAY_MODE_HEX
		BT_Display_Mode.UNRECOGNIZED -> BTTerminalDisplayMode.DISPLAY_MODE_TEXT
	},
)

val BTTerminalDisplayMode.toProto: BT_Display_Mode
	get() = when (this) {
		BTTerminalDisplayMode.DISPLAY_MODE_TEXT -> BT_Display_Mode.DISPLAY_MODE_TEXT
		BTTerminalDisplayMode.DISPLAY_MODE_HEX -> BT_Display_Mode.DISPLAY_MODE_HEX
	}

val BTTerminalCharSet.toProto: BT_CharSet
	get() = when (this) {
		BTTerminalCharSet.CHAR_SET_UTF_8 -> BT_CharSet.CHAR_SET_UTF_8
		BTTerminalCharSet.CHAR_SET_UTF_16 -> BT_CharSet.CHAR_SET_UTF_16
		BTTerminalCharSet.CHAR_SET_UTF_32 -> BT_CharSet.CHAR_SET_UTF_32
		BTTerminalCharSet.CHAR_SET_US_ASCII -> BT_CharSet.CHAR_SET_US_ASCII
		BTTerminalCharSet.CHAR_SET_ISO_8859_1 -> BT_CharSet.CHAR_SET_ISO_8859_1
	}

val BTTerminalNewLineChar.toProto: BT_NewLine_Char
	get() = when (this) {
		BTTerminalNewLineChar.NEW_LINE_CR_LF -> BT_NewLine_Char.NEW_LINE_CR_LF
		BTTerminalNewLineChar.NEW_LINE_CR -> BT_NewLine_Char.NEW_LINE_CR
		BTTerminalNewLineChar.NEW_LINE_LF -> BT_NewLine_Char.NEW_LINE_LF
		BTTerminalNewLineChar.NEW_LINE_END_OF_TEXT -> BT_NewLine_Char.NEW_LINE_END_OF_TEXT
		BTTerminalNewLineChar.NEW_LINE_NULL_CHR -> BT_NewLine_Char.NEW_LINE_NULL_CHR
		BTTerminalNewLineChar.NEW_LINE_NONE -> BT_NewLine_Char.NEW_LINE_NONE
	}
