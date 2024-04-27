package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

interface EndConnectionEvents {

	data object OnCancelAndCloseDialog : EndConnectionEvents
	data object OnOpenDisconnectDialog : EndConnectionEvents
	data object OnDisconnectAndNavigateBack : EndConnectionEvents

}