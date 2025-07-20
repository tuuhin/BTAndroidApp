package com.eva.bluetoothterminalapp.domain.bluetooth.enums

enum class ClientConnectionState {
	// start state
	CONNECTION_INITIALIZING,

	// connecting to the device
	CONNECTION_PEER_FOUND,
	CONNECTION_DENIED,

	// client connection
	CONNECTION_CONNECTED,
	CONNECTION_DISCONNECTED,

	// pairing connection
	CONNECTION_PAIRING,
	CONNECTION_PAIRED;


	fun checkCorrectNextState(nextState: ClientConnectionState): Boolean {
		val probableState = when (this) {
			CONNECTION_INITIALIZING -> setOf(CONNECTION_DENIED, CONNECTION_PEER_FOUND)

			CONNECTION_PEER_FOUND -> setOf(
				CONNECTION_PAIRING,
				CONNECTION_DENIED,
				CONNECTION_CONNECTED,
			)

			CONNECTION_DENIED -> setOf(CONNECTION_PEER_FOUND)
			CONNECTION_CONNECTED -> setOf(CONNECTION_DISCONNECTED)
			CONNECTION_DISCONNECTED -> setOf(CONNECTION_CONNECTED)
			CONNECTION_PAIRING -> setOf(CONNECTION_PAIRED)
			CONNECTION_PAIRED -> setOf(CONNECTION_CONNECTED)
		}

		return probableState.any { it == nextState }
	}
}