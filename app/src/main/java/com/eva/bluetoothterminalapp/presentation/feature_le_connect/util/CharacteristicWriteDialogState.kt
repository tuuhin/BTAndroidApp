package com.eva.bluetoothterminalapp.presentation.feature_le_connect.util

data class CharacteristicWriteDialogState(
	val showWriteDialog: Boolean = false,
	val writeTextFieldValue: String = "",
	val error: String? = null,
)