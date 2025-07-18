package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state

import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel

data class BTServerDeviceState(
	val status: ServerConnectionState = ServerConnectionState.CONNECTION_INITIALIZING,
	val device: BluetoothDeviceModel? = null
) {
	val isAccepted: Boolean
		get() = status == ServerConnectionState.CONNECTION_ACCEPTED

	val isRunning: Boolean
		get() = status == ServerConnectionState.CONNECTION_LISTENING || status == ServerConnectionState.CONNECTION_ACCEPTED
}