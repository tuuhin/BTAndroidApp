package com.eva.bluetoothterminalapp.data.services

import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.utils.PlatformCoroutineDispatchers
import kotlinx.coroutines.withContext
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

internal class SPPConnectionSession(
	private val connector: BluetoothClientConnector,
	private val dispatcher: PlatformCoroutineDispatchers,
) {

	private var _address: String? = null
	private var _sppUuid: Uuid? = null

	suspend fun send(address: String, uuid: Uuid, payload: ByteArray) {

		if (this._address != null && (this._address != address || this._sppUuid != uuid)) {
			throw IllegalStateException("Disconnect the active SPP session before changing device or UUID")
		}

		if (_address == null) {
			val connResult = connector.connectClient(
				address = address,
				connectUUID = uuid.toJavaUuid(),
				secure = true
			)

			if (connResult.isFailure) {
				close()
				throw connResult.exceptionOrNull()
					?: IllegalStateException("Unable to connect to Bluetooth device")
			}
			// if connection success then update the values
			this._address = address
			this._sppUuid = uuid
		}

		val sendResult = connector.sendBytes(payload)
		val isFailed = sendResult.isFailure || !sendResult.getOrDefault(false)

		if (isFailed) {
			val error = sendResult.exceptionOrNull() ?: IllegalStateException("Unable to send data")
			close()
			throw error
		}
	}

	suspend fun close() = withContext(dispatcher.io) {
		connector.closeClient()
		_address = null
		_sppUuid = null
	}

	fun closeBlocking() {
		connector.closeClient()
		_address = null
		_sppUuid = null
	}
}