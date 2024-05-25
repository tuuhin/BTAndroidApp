package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state

import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class BTServerRouteState(
	val connectionMode: ServerConnectionState = ServerConnectionState.CONNECTION_INITIALIZING,
	val messages: PersistentList<BluetoothMessage> = persistentListOf(),
	val textFieldValue: String = "",
	val showStartServerDialog: Boolean = true,
	val showDisconnectDialog: Boolean = false,
)