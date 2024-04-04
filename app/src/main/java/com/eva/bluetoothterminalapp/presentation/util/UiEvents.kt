package com.eva.bluetoothterminalapp.presentation.util

sealed interface UiEvents {
	data class ShowSnackBar(val message: String) : UiEvents

	data class ShowToast(val message: String) : UiEvents
}