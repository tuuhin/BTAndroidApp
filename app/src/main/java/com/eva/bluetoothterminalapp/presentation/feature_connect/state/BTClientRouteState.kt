package com.eva.bluetoothterminalapp.presentation.feature_connect.state

import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class BTClientRouteState(
	val connectionMode: ClientConnectionState = ClientConnectionState.CONNECTION_INITIALIZING,
	val messages: PersistentList<BluetoothMessage> = persistentListOf(),
	val textFieldValue: String = "",
	val showDisconnectDialog: Boolean = false,
)