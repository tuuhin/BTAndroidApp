package com.eva.bluetoothterminalapp.data.bluetooth.util

import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar

fun String.replaceLineSperatorTo(to: BTTerminalNewLineChar): String {
	val previous = System.lineSeparator()
	val replacement = to.value.orEmpty()
	return replace(Regex(previous), replacement)
}

fun String.replaceLineSperator(current: BTTerminalNewLineChar): String {
	val previous = current.value ?: return this
	val replacement = System.lineSeparator()
	return replace(previous, replacement)
}