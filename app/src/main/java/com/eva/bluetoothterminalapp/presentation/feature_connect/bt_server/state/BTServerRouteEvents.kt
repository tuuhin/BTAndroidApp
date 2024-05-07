package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state


sealed interface BTServerRouteEvents {
	data object RestartServer : BTServerRouteEvents

	data object StopServer : BTServerRouteEvents

	data object CloseDisconnectDialog : BTServerRouteEvents

	data object OpenDisconnectDialog : BTServerRouteEvents

	data object CloseDialogAndStopServer : BTServerRouteEvents

	data object OnSendEvents : BTServerRouteEvents

	data class OnTextFieldValue(val message: String) : BTServerRouteEvents
}