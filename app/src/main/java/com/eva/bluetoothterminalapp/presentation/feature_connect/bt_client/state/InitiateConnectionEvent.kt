package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

import java.util.UUID

interface InitiateConnectionEvent {

	data class OnSelectUUID(val uuid: UUID?) : InitiateConnectionEvent
	data object OnCancelAndNavigateBack : InitiateConnectionEvent
	data object OnAcceptConnection : InitiateConnectionEvent
}