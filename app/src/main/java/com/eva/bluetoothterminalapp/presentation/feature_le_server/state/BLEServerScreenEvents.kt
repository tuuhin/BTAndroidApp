package com.eva.bluetoothterminalapp.presentation.feature_le_server.state

sealed interface BLEServerScreenEvents {

	data object OnStartServer : BLEServerScreenEvents
	data object OnStopServer : BLEServerScreenEvents

	data object OnShowServerRunningDialog : BLEServerScreenEvents
	data object OnCloseServerRunningDialog : BLEServerScreenEvents
}