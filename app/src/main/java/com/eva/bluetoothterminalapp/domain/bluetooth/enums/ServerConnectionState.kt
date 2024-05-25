package com.eva.bluetoothterminalapp.domain.bluetooth.enums

enum class ServerConnectionState {

	/**
	 * Creating the server use this to initiate the flow
	 */
	CONNECTION_INITIALIZING,

	/**
	 * Socket created and the socket is listening for incomming connections
	 */
	CONNECTION_LISTENING,

	/**
	 * Connection is accepted and 2 way communication can occur now
	 */
	CONNECTION_ACCEPTED,

	/**
	 * Connection is disconnected
	 */
	CONNECTION_DISCONNECTED
}