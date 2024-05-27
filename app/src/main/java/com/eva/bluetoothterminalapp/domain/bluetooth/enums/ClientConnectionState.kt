package com.eva.bluetoothterminalapp.domain.bluetooth.enums

enum class ClientConnectionState {
	/**
	 * Starting the connection with the other device
	 */
	CONNECTION_INITIALIZING,

	/**
	 * Remote device is recognized by the device
	 */
	CONNECTION_DEVICE_CONNECTED,

	/**
	 * Remote Device connection is accepted
	 */
	CONNECTION_ACCEPTED,

	/**
	 * Remote device is Denied
	 */
	CONNECTION_DENIED,

	/**
	 * Remote device is disconnected
	 */
	CONNECTION_DISCONNECTED,

	/**
	 * Pairing started for remote device
	 */
	CONNECTION_BONDING,

	/**
	 * New Device Paired
	 */
	CONNECTION_BONDED,
}

fun checkIfStateChangeAllowed(
	previous: ClientConnectionState,
	next: ClientConnectionState
): Boolean {
	val newStates = when (previous) {
		ClientConnectionState.CONNECTION_INITIALIZING -> setOf(
			ClientConnectionState.CONNECTION_DISCONNECTED,
			ClientConnectionState.CONNECTION_DEVICE_CONNECTED
		)

		ClientConnectionState.CONNECTION_DEVICE_CONNECTED -> setOf(
			ClientConnectionState.CONNECTION_BONDING,
			ClientConnectionState.CONNECTION_DENIED,
			ClientConnectionState.CONNECTION_ACCEPTED,
			ClientConnectionState.CONNECTION_DISCONNECTED
		)

		ClientConnectionState.CONNECTION_ACCEPTED -> setOf(ClientConnectionState.CONNECTION_DISCONNECTED)
		ClientConnectionState.CONNECTION_DENIED -> setOf(ClientConnectionState.CONNECTION_DEVICE_CONNECTED)
		ClientConnectionState.CONNECTION_DISCONNECTED -> setOf(ClientConnectionState.CONNECTION_DEVICE_CONNECTED)
		ClientConnectionState.CONNECTION_BONDING -> setOf(ClientConnectionState.CONNECTION_BONDED)
		ClientConnectionState.CONNECTION_BONDED -> setOf(ClientConnectionState.CONNECTION_ACCEPTED)
	}

	return newStates.any { it == next }
}