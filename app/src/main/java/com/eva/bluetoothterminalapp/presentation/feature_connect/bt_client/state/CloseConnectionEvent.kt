package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

interface CloseConnectionEvent {

	data object OnCancelAndCloseDialog : CloseConnectionEvent
	data object OnOpenDisconnectDialog : CloseConnectionEvent
	data object OnDisconnectAndNavigateBack : CloseConnectionEvent

}