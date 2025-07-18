package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state

import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class BTServerScreenState(
	val messages: PersistentList<BluetoothMessage> = persistentListOf(),
	val textFieldValue: String = "",
	val showServerTerminal: Boolean = false,
	val showExitDialog: Boolean = false,
)