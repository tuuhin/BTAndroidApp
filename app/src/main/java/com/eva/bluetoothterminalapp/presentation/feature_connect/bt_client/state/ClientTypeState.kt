package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util.ClientConnectType

data class ClientTypeState(
	val showConnectDialog: Boolean = true,
	val connectType: ClientConnectType = ClientConnectType.CONNECT_TO_DEVICE,
)