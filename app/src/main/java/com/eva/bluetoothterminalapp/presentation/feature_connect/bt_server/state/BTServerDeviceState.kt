package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state

import com.eva.bluetoothterminalapp.domain.bluetooth.enums.PeerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel

data class BTServerDeviceState(
	val serverState: ServerConnectionState = ServerConnectionState.SERVER_STARTING,
	val peerState: PeerConnectionState = PeerConnectionState.PEER_NOT_FOUND,
	val device: BluetoothDeviceModel? = null
) {
	val isAccepted: Boolean
		get() = peerState == PeerConnectionState.PEER_CONNECTED

	val isRunning: Boolean
		get() = serverState == ServerConnectionState.SERVER_LISTENING || peerState == PeerConnectionState.PEER_CONNECTED
}