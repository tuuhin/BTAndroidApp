package com.eva.bluetoothterminalapp.domain.models

enum class ClientConnectionState {
	/**
	 * Starting the connection with the other device
	 */
	CONNECTION_INITIALIZING,

	/**
	 * Remote device is recognized by the device
	 */
	CONNECTION_DEVICE_FOUND,

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