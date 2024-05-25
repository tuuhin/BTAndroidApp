package com.eva.bluetoothterminalapp.domain.settings.repository

import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalCharSet
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalDisplayMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import kotlinx.coroutines.flow.Flow

interface BTSettingsDataSore {

	val settingsFlow: Flow<BTSettingsModel>

	val settings: BTSettingsModel

	suspend fun onCharsetChange(charSet: BTTerminalCharSet)

	suspend fun onShowTimestampChange(isChange: Boolean)

	suspend fun onDisplayModeChange(mode: BTTerminalDisplayMode)

	suspend fun onNewLineCharChangeForReceive(newLineChar: BTTerminalNewLineChar)

	suspend fun onNewLineCharChangeForSend(newLineChar: BTTerminalNewLineChar)

	suspend fun onLocalEchoValueChange(isLocalEcho: Boolean)

	suspend fun onClearInputOnSendValueChange(canClear: Boolean)

	suspend fun onKeepScreenOnConnectedValueChange(isKeepScreenOn: Boolean)

	suspend fun onAutoScrollValueChange(isEnabled: Boolean)

}