package com.eva.bluetoothterminalapp.presentation.feature_le_server.state

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServerServices

sealed interface BLEServerScreenEvents {

	data object OnStartServer : BLEServerScreenEvents
	data object OnStopServer : BLEServerScreenEvents

	data object OnShowServerRunningDialog : BLEServerScreenEvents
	data object OnCloseServerRunningDialog : BLEServerScreenEvents

	data class UpdateServiceOptions(val option: BLEServerServices) : BLEServerScreenEvents

	data object OnToggleServiceSelector : BLEServerScreenEvents
}