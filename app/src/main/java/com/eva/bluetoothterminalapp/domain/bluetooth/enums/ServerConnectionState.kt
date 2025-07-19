package com.eva.bluetoothterminalapp.domain.bluetooth.enums

enum class ServerConnectionState {

	/**
	 * Creating the server use this to initiate the flow
	 */
	SERVER_STARTING,

	/**
	 * Socket created and the socket is listening for incomming connections
	 */
	SERVER_LISTENING,

	/**
	 * Some peer had accepted the connection
	 */
	PEER_CONNECTION_ACCEPTED,

	/**
	 * Server is stopped need to start again to continue
	 */
	SERVER_STOPPED
}