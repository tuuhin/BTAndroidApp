package com.eva.bluetoothterminalapp.presentation.feature_settings.util

import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalCharSet
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalDisplayMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar

sealed interface BTSettingsEvent {

	data class OnCharsetChange(val charSet: BTTerminalCharSet) : BTSettingsEvent

	data class OnShowTimestampChange(val isChange: Boolean) : BTSettingsEvent

	data class OnDisplayModeChange(val mode: BTTerminalDisplayMode) : BTSettingsEvent

	data class OnNewLineCharChange(val newLineChar: BTTerminalNewLineChar) : BTSettingsEvent

}