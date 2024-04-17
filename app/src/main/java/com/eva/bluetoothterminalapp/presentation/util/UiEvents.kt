package com.eva.bluetoothterminalapp.presentation.util

sealed interface UiEvents {
	data class ShowSnackBar(val message: String) : UiEvents

	data class ShowSnackBarWithActions(
		val message: String,
		val actionText: String,
		val action: () -> Unit
	) : UiEvents

	data class ShowToast(val message: String) : UiEvents

	data object NavigateBack : UiEvents

}