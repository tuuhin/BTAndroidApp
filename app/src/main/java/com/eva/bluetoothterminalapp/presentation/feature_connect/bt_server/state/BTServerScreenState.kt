package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class BTServerScreenState(
	val messages: PersistentList<BluetoothMessage> = persistentListOf(),
	val textFieldState: TextFieldState = TextFieldState(),
	val showServerTerminal: Boolean = false,
	val showExitDialog: Boolean = false,
)