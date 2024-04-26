package com.eva.bluetoothterminalapp.domain.bluetooth_le.samples

import java.util.UUID

interface SampleUUIDReader {

	/**
	 * Reads from a source and provides the probable service name from 128 bits [uuid]
	 * @param uuid UUID to check for
	 * @return A probable name for the service
	 */
	suspend fun findNameForUUID(uuid: UUID): String?
}