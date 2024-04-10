package com.eva.bluetoothterminalapp.presentation.feature_connect.state

sealed interface BTClientRouteEvents {

	/**
	 * Disconnects the client
	 */
	data object DisConnectClient : BTClientRouteEvents

	/**
	 * Connects the client
	 */
	data object ConnectClient : BTClientRouteEvents


	data object ClearTerminal : BTClientRouteEvents

	data object CloseDisconnectDialog : BTClientRouteEvents

	data object OpenDisconnectDialog : BTClientRouteEvents

	data object OnSendEvents : BTClientRouteEvents

	data class OnTextFieldValue(val message: String) : BTClientRouteEvents

}