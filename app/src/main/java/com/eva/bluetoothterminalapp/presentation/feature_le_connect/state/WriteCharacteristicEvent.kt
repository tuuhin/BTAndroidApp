package com.eva.bluetoothterminalapp.presentation.feature_le_connect.state

sealed interface WriteCharacteristicEvent {

	data object OpenDialog : WriteCharacteristicEvent
	data object CloseDialog : WriteCharacteristicEvent
	data class OnTextFieldValueChange(val value: String) : WriteCharacteristicEvent
	data object WriteCharacteristicValue : WriteCharacteristicEvent

}