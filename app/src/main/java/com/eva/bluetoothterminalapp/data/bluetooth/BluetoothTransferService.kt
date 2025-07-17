package com.eva.bluetoothterminalapp.data.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.eva.bluetoothterminalapp.data.bluetooth.util.readResponseFromStream
import com.eva.bluetoothterminalapp.data.bluetooth.util.replaceLineSeparatorFrom
import com.eva.bluetoothterminalapp.data.bluetooth.util.replaceLineSeparatorTo
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.domain.exceptions.BTSocketNotConnectedException
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

private const val TRANSFER_LOGGER = "TRANSFER_LOGGER"

class BluetoothTransferService(
	private val socket: BluetoothSocket,
	private val btSettings: BTSettingsDataSore,
) {
	private val _inputStream: InputStream
		get() = socket.inputStream
	private val _outputStream: OutputStream
		get() = socket.outputStream

	private val _buffer = ByteArray(4 * 1_024)

	fun readFromStream(canRead: Boolean): Flow<BluetoothMessage> {
		return flow {

			val config = btSettings.getSettings()
			val mode = config.displayMode
			val lineSeparator = config.newLineCharReceive

			try {
				while (canRead && socket.isConnected) {
					// throws cancellation exception
					currentCoroutineContext().ensureActive()
					// check if input stream is available otherwise continue
					if (_inputStream.available() == 0) continue
					// clear the buffer each time before reading
					_buffer.fill(0x0)
					// evaluate the message
					val message = _inputStream.readResponseFromStream(buffer = _buffer, mode = mode)

					val convertedMessage = message.replaceLineSeparatorFrom(current = lineSeparator)

					val btMessage = BluetoothMessage(
						message = convertedMessage,
						type = BluetoothMessageType.MESSAGE_FROM_OTHER
					)
					emit(btMessage)
				}
			} catch (e: IOException) {
				Log.e(TRANSFER_LOGGER, "ERROR OCCCURED", e)
				e.printStackTrace()
			}
		}
			.flowOn(Dispatchers.IO)
			.catch { err -> Log.e(TRANSFER_LOGGER, "ERROR", err) }
			.onCompletion {
				if (!canRead) return@onCompletion
				// ensures logs when the socket is disconnected
				Log.d(TRANSFER_LOGGER, "FINISHED READING THE FLOW")
			}
	}


	suspend fun writeToStream(value: String): Result<Boolean> {
		val config = btSettings.getSettings()
		val charset = config.btTerminalCharSet.charset
		val newlineChar = config.newLineCharSend

		val convertedValue = value.replaceLineSeparatorTo(to = newlineChar)
		val bytes = convertedValue.toByteArray(charset)

		return withContext(Dispatchers.IO) {
			try {
				// if the socket is not connected return Result failure
				if (!socket.isConnected)
					return@withContext Result.failure(BTSocketNotConnectedException())
				// else write the bytes to the output stream
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
