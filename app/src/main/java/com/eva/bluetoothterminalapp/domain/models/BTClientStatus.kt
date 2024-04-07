package com.eva.bluetoothterminalapp.domain.models

enum class BTClientStatus {
	/**
	 * Base Connection Mode
	 */
	CONNECTION_INITIALIZED,

	/**
	 * Remote device is connected
	 */
	CONNECTION_CONNECTED,

	/**
	 * Remote device is accepted
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