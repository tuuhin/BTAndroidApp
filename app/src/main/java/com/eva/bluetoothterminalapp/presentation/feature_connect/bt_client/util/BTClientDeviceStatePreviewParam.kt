package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientDeviceState
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes

class BTClientDeviceStatePreviewParam : CollectionPreviewParameterProvider<BTClientDeviceState>(
	listOf(
		BTClientDeviceState(),
		BTClientDeviceState(
			connectionStatus = ClientConnectionState.CONNECTION_CONNECTED,
			device = PreviewFakes.FAKE_DEVICE_MODEL
		),
		BTClientDeviceState(
			connectionStatus = ClientConnectionState.CONNECTION_DISCONNECTED,
			device = PreviewFakes.FAKE_DEVICE_MODEL
		)
	)
)