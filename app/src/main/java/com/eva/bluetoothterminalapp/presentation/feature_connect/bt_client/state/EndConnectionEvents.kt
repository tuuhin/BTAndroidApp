package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

interface EndConnectionEvents {
	/**
	 * Cancel and close the dialog
	 */
	data object OnCancelAndCloseDialog : EndConnectionEvents

	/**
	 * Opens the disconnect dialog
	 */
	data object OnOpenDisconnectDialog : EndConnectionEvents

	/**
	 * Disconnects and navigate back
	 */
	data object OnDisconnectAndNavigateBack : EndConnectionEvents

}