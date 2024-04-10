package com.eva.bluetoothterminalapp.presentation.feature_connect.state


sealed interface BTServerRouteEvents {

	data object StopServer : BTServerRouteEvents

	data object CloseDialogAndStopServer : BTServerRouteEvents

	data object OpenStopServerDialog : BTServerRouteEvents

	data object OnSendEvents : BTServerRouteEvents

	data class OnTextFieldValue(val message: String) : BTServerRouteEvents
}