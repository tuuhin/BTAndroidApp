package com.eva.bluetoothterminalapp.data.services

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.eva.bluetoothterminalapp.data.bluetooth.FakeBluetoothClientConnector
import com.eva.bluetoothterminalapp.data.utils.TestPlatformDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class)
class SPPConnectionSessionTest {

	@Test
	fun `given active session when sending again then reuses connection`() = runTest {
		val connector = FakeBluetoothClientConnector()
		val session = SPPConnectionSession(
			connector,
			TestPlatformDispatcher(UnconfinedTestDispatcher(testScheduler))
		)

		session.send(ADDRESS, UUID_VALUE, byteArrayOf(1))
		session.send(ADDRESS, UUID_VALUE, byteArrayOf(2))

		advanceUntilIdle()

		assertThat(connector.connectCount).isEqualTo(1)
		assertThat(connector.sendCount).isEqualTo(2)
	}

	@Test
	fun `given active session when disconnected then next send reconnects`() = runTest {
		val connector = FakeBluetoothClientConnector()
		val session = SPPConnectionSession(
			connector,
			TestPlatformDispatcher(UnconfinedTestDispatcher(testScheduler))
		)
		session.send(ADDRESS, UUID_VALUE, byteArrayOf(1))

		session.close()
		session.send(ADDRESS, UUID_VALUE, byteArrayOf(2))

		advanceUntilIdle()

		assertThat(connector.connectCount).isEqualTo(2)
		assertThat(connector.closeCount).isEqualTo(1)
	}

	@Test
	fun `given different device when sending then requires disconnect`() = runTest {
		val connector = FakeBluetoothClientConnector()
		val session = SPPConnectionSession(
			connector,
			TestPlatformDispatcher(UnconfinedTestDispatcher(testScheduler))
		)
		session.send(ADDRESS, UUID_VALUE, byteArrayOf(1))

		advanceUntilIdle()
		assertFailsWith<IllegalStateException> {
			session.send("11:23:45:67:89:AB", UUID_VALUE, byteArrayOf(2))
		}
		assertEquals(1, connector.connectCount)
	}

	@Test
	fun `given send failure when sending then closes stale session and reconnects next time`() =
		runTest {
			val connector = FakeBluetoothClientConnector().apply { failSend = true }
			val session = SPPConnectionSession(
				connector,
				TestPlatformDispatcher(UnconfinedTestDispatcher(testScheduler))
			)
			advanceUntilIdle()
			assertFailsWith<IllegalStateException> {
				session.send(ADDRESS, UUID_VALUE, byteArrayOf(1))
			}
			connector.failSend = false
			session.send(ADDRESS, UUID_VALUE, byteArrayOf(2))

			advanceUntilIdle()
			assertEquals(2, connector.connectCount)
			assertEquals(1, connector.closeCount)
		}


	private companion object {
		const val ADDRESS = "01:23:45:67:89:AB"
		val UUID_VALUE = Uuid.parse("00001101-0000-1000-8000-00805f9b34fb")
	}
}