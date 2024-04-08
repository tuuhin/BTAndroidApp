package com.eva.bluetoothterminalapp.data.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.eva.bluetoothterminalapp.domain.exceptions.BTSocketNotConnectedException
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.IOException

private const val TRANSFER_LOGGER = "TRANSFER_LOGGER"

class BluetoothTransferService(
	private val socket: BluetoothSocket
) {
	private val _inputStream = socket.inputStream
	private val _outputStream = socket.outputStream

	private val _buffer = ByteArray(512)

	fun readFromStream(canRead: Boolean) = flow {
		while (canRead && socket.isConnected && currentCoroutineContext().isActive) {
			// clear the buffer each time
			_buffer.fill(element = 0, fromIndex = 0, toIndex = _buffer.size)
			try {
				val byteCount = _inputStream.read(_buffer)
				// byte count is lessen than 0 thus stream closed
				if (byteCount <= 0) continue
				val message = _buffer.decodeToString(endIndex = byteCount)
				// skip if a message is empty
				if (message.isEmpty() || message.isBlank()) continue

				val btMessage = BluetoothMessage(
					message = message,
					type = BluetoothMessageType.MESSAGE_FROM_SERVER
				)
				emit(btMessage)
			} catch (e: IOException) {
				Log.e(TRANSFER_LOGGER, "ERROR OCCCURED  ${e.message ?: ""}")
				e.printStackTrace()
				break
			}
		}
	}.flowOn(Dispatchers.IO)


	suspend fun writeToStream(bytes: ByteArray): Result<Boolean> {
		return withContext(Dispatchers.IO) {
			try {
				if (!socket.isConnected)
					return@withContext Result.failure(BTSocketNotConnectedException())
				_outputStream.write(bytes)
				Log.d(TRANSFER_LOGGER, "WRITTEN TO STREAM")
				Result.success(true)
			} catch (e: Exception) {
				e.printStackTrace()
				Result.failure(e)
			}
		}
	}
}