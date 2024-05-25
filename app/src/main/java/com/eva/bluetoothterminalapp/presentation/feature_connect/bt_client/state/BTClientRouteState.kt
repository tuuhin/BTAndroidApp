package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class BTClientRouteState(
	val connectionMode: ClientConnectionState = ClientConnectionState.CONNECTION_INITIALIZING,
	val messages: PersistentList<BluetoothMessage> = persistentListOf(),
	val textFieldValue: String = "",
)