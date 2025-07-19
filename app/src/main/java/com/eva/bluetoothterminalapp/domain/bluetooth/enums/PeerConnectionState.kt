package com.eva.bluetoothterminalapp.domain.bluetooth.enums

/**
 * A simplified version of [ServerConnectionState] or [ClientConnectionState]
 * this only provide the info if the peer is connected or disconnected
 */
enum class PeerConnectionState {
	PEER_NOT_FOUND,
	PEER_CONNECTED,
	PEER_DISCONNECTED
}