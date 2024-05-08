package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

sealed interface BTClientRouteEvents {

	/**
	 * Normally Disconnects the client
	 */
	data object OnDisconnectClient : BTClientRouteEvents

	/**
	 * Trying Reconnecting with the client
	 */
	data object OnReconnectClient : BTClientRouteEvents

	/**
	 * On Send Text Event ie, when the text collected is send
	 */
	data object OnSendEvents : BTClientRouteEvents


	/**
	 * Send certain string value to the server
	 */
	data class OnSendFieldTextChanged(val text: String) : BTClientRouteEvents

}