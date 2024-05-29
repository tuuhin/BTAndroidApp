package com.eva.bluetoothterminalapp.presentation.feature_settings.util

import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalCharSet
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalDisplayMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar

sealed interface BTSettingsEvent {

	data class OnCharsetChange(val charSet: BTTerminalCharSet) : BTSettingsEvent

	data class OnShowTimeStampValueChanged(val isChange: Boolean) : BTSettingsEvent

	data class OnDisplayModeChange(val mode: BTTerminalDisplayMode) : BTSettingsEvent

	data class OnReceiveNewLineCharChanged(val newlineChar: BTTerminalNewLineChar) :
		BTSettingsEvent

	data class OnSendNewLineCharChanged(val newlineChar: BTTerminalNewLineChar) : BTSettingsEvent

	data class OnLocalEchoValueChange(val isAllowed: Boolean) : BTSettingsEvent

	data class OnClearInputValueChange(val canClear: Boolean) : BTSettingsEvent

	data class OnKeepScreenOnValueChange(val isKeepScreenOn: Boolean) : BTSettingsEvent

	data class OnAutoScrollValueChanged(val isEnabled: Boolean) : BTSettingsEvent

}