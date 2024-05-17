package com.eva.bluetoothterminalapp.domain.bluetooth_le.enums

enum class BLEWriteTypes {

	/**
	 * Write type for property [BLEPropertyTypes.PROPERTY_WRITE],
	 * Its the default write type
	 * @see BLEPropertyTypes.PROPERTY_WRITE
	 */
	TYPE_DEFAULT,

	/**
	 * Write type for property [BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE]
	 * @see BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE
	 */
	TYPE_NO_RESPONSE,

	/**
	 * Write type for signed writes ie, [BLEPropertyTypes.PROPERTY_SIGNED_WRITE]
	 * @see BLEPropertyTypes.PROPERTY_SIGNED_WRITE
	 */
	TYPE_SIGNED,

	/**
	 * If some unkown write type is found
	 */
	TYPE_UNKNOWN,
}