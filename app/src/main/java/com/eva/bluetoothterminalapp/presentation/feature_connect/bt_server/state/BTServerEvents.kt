package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state


sealed interface BTServerEvents {
	/**
	 * Starts the server
	 */
	data object OnStartServer : BTServerEvents

	/**
	 * Cancels starting the server and navigate back
	 */
	data object OnStopServerAndNavigateBack : BTServerEvents

	/**
	 * Restarts the server
	 */
	data object OnRestartServer : BTServerEvents

	/**
	 * Stops the server
	 */
	data object OnStopServer : BTServerEvents

	/**
	 * Close disconnect dialog
	 */
	data object OnCloseDisconnectDialog : BTServerEvents

	/**
	 * Open disconnect dialog
	 */
	data object OnOpenDisconnectDialog : BTServerEvents

	/**
	 * On ime action send
	 */
	data object OnSendEvents : BTServerEvents

	/**
	 * On TextField value change
	 */
	data class OnTextFieldValue(val message: String) : BTServerEvents
}