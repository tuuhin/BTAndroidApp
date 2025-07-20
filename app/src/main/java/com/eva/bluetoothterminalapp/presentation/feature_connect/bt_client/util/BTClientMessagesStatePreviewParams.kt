package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientMessagesState
import kotlinx.collections.immutable.persistentListOf

class BTClientMessagesStatePreviewParams :
	CollectionPreviewParameterProvider<BTClientMessagesState>(
	listOf(
		BTClientMessagesState(
			messages = persistentListOf(
				BluetoothMessage(
					message = "Hello",
					type = BluetoothMessageType.MESSAGE_FROM_SELF
				),
				BluetoothMessage(
					message = "Hi",
					type = BluetoothMessageType.MESSAGE_FROM_OTHER,
				)
			)
		),
		BTClientMessagesState(
			messages = persistentListOf(
				BluetoothMessage("Hello", BluetoothMessageType.MESSAGE_FROM_SELF)
			),
		),
		BTClientMessagesState(
			messages = persistentListOf(
				BluetoothMessage(
					"Hello this is an long message please note this one that can effect the list styling",
					BluetoothMessageType.MESSAGE_FROM_SELF
				),
				BluetoothMessage(
					"Hello this is a small message",
					BluetoothMessageType.MESSAGE_FROM_SELF
				),
				BluetoothMessage(
					"Hello this is an long message please note this one that can effect the list styling",
					BluetoothMessageType.MESSAGE_FROM_SELF
				)
			)
		)
	)
)