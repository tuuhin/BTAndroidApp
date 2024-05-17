package com.eva.bluetoothterminalapp.domain.bluetooth_le.enums


/**
 * Properties available with a certain bluetooth low energy characteristic
 *  @see <a href="https://novelbits.io/bluetooth-gatt-services-characteristics/">bluetooth-gatt-services-characteristics</a>
 */
enum class BLEPropertyTypes {
	/**
	 * Permits broadcast of a characteristic value using a server characteristic configuration
	 * descriptor
	 */
	PROPERTY_BROADCAST,

	/**
	 * Permits read of a characteristic value
	 */
	PROPERTY_READ,

	/**
	 * Permit write without any response
	 */
	PROPERTY_WRITE_NO_RESPONSE,

	/**
	 * Permits write with response
	 */
	PROPERTY_WRITE,

	/**
	 * Permits notification of a characteristic value without any acknowledgement
	 * Client Characteristic Configuration Descriptor should exist (0x2902)
	 */
	PROPERTY_NOTIFY,

	/**
	 * Permits notification of a characteristic value with acknowledgement
	 * Client Characteristic Configuration Descriptor should exist (0x2902)
	 */
	PROPERTY_INDICATE,

	/**
	 * Permits signed write to a characteristic write.
	 */
	PROPERTY_SIGNED_WRITE,

	/**
	 * Additional Characteristic property are defined in the characteristic Extended properties Descriptor
	 * If set Characteristic Extended Properties descriptor should be set (0x2900)
	 */
	PROPERTY_EXTENDED_PROPS,

	/**
	 * If an unknonw match of a property has been found
	 */
	UNKNOWN,

}