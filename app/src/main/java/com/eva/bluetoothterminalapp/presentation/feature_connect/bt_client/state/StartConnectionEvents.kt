package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util.ClientConnectType

interface StartConnectionEvents {

	data class OnConnectTypeChanged(val type: ClientConnectType) : StartConnectionEvents
	data object OnCancelAndNavigateBack : StartConnectionEvents
	data object OnAcceptConnection : StartConnectionEvents
}