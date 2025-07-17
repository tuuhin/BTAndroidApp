package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel

data class BTClientDeviceState(
	val connectionStatus: ClientConnectionState = ClientConnectionState.CONNECTION_INITIALIZING,
	val device: BluetoothDeviceModel? = null
) {
	val isConnected: Boolean
		get() = connectionStatus == ClientConnectionState.CONNECTION_ACCEPTED
}
