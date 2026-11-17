package com.eva.bluetoothterminalapp.data.services

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import kotlin.test.assertFailsWith

class SPPConnectionConstantsTest {

	@Test
	fun `given valid command when validated then accepts raw payload`() {
		SPPConnectionConstants.validateArgs(
			"01:23:45:67:89:AB",
			"00001101-0000-1000-8000-00805f9b34fb",
			byteArrayOf(0x01, 0x02),
			null
		)
	}

	@Test
	fun `given hex payload when decoded then preserves bytes`() {
		val rawBytes = byteArrayOf(0x48, 0x69)
		assertThat(SPPConnectionConstants.hexToByteArray("4869")).isEqualTo(rawBytes)
	}

	@Test
	fun `given malformed address when validated then rejects command`() {
		assertFailsWith<IllegalArgumentException> {
			SPPConnectionConstants.validateArgs(
				"01:23:45:67:89",
				"00001101-0000-1000-8000-00805f9b34fb",
				byteArrayOf(0x01),
				null
			)
		}
	}

	@Test
	fun `given non colon separated address when validated then rejects command`() {
		assertFailsWith<IllegalArgumentException> {
			SPPConnectionConstants.validateArgs(
				"01-23:45-67:89-AB",
				"00001101-0000-1000-8000-00805f9b34fb",
				byteArrayOf(0x01),
				null
			)
		}
	}

	@Test
	fun `given missing payload when validated then rejects command`() {
		assertFailsWith<IllegalArgumentException> {
			SPPConnectionConstants.validateArgs(
				"01:23:45:67:89:AB",
				"00001101-0000-1000-8000-00805f9b34fb",
				null,
				null
			)
		}
	}

	@Test
	fun `given empty raw payload when validated then rejects command`() {
		assertFailsWith<IllegalArgumentException> {
			SPPConnectionConstants.validateArgs(
				"01:23:45:67:89:AB",
				"00001101-0000-1000-8000-00805f9b34fb",
				byteArrayOf(),
				null
			)
		}
	}
}