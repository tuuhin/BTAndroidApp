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
	CONNECTION_BONDED;

	fun checkCorrectNextState(nextState: ClientConnectionState): Boolean {
		val newStates = when (this) {
			CONNECTION_INITIALIZING -> setOf(
				CONNECTION_DISCONNECTED,
				CONNECTION_DEVICE_CONNECTED
			)

			CONNECTION_DEVICE_CONNECTED -> setOf(
				CONNECTION_BONDING,
				CONNECTION_DENIED,
				CONNECTION_ACCEPTED,
				CONNECTION_DISCONNECTED
			)

			CONNECTION_ACCEPTED -> setOf(CONNECTION_DISCONNECTED)
			CONNECTION_DENIED -> setOf(CONNECTION_DEVICE_CONNECTED)
			CONNECTION_DISCONNECTED -> setOf(CONNECTION_DEVICE_CONNECTED)
			CONNECTION_BONDING -> setOf(CONNECTION_BONDED)
			CONNECTION_BONDED -> setOf(CONNECTION_ACCEPTED)
		}

		return newStates.any { it == nextState }
	}
}