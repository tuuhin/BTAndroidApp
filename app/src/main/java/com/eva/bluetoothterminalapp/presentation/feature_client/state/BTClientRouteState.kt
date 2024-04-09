package com.eva.bluetoothterminalapp.presentation.feature_client.state

import com.eva.bluetoothterminalapp.domain.models.BTClientStatus
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class BTClientRouteState(
	val connectionMode: BTClientStatus = BTClientStatus.CONNECTION_INITIALIZING,
	val messages: PersistentList<BluetoothMessage> = persistentListOf(),
	val textFieldValue: String = "",
	val showDisconnectDialog: Boolean = false,
)