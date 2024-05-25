package com.eva.bluetoothterminalapp.data.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.domain.exceptions.BTSocketNotConnectedException
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalDisplayMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream

private const val TRANSFER_LOGGER = "TRANSFER_LOGGER"

class BluetoothTransferService(
	private val socket: BluetoothSocket,
	private val btSettings: BTSettingsDataSore,
) {
	private val _inputStream = socket.inputStream
	private val _outputStream = socket.outputStream

	private val _buffer = ByteArray(4 * 1_024)

	private val _btConfig: BTSettingsModel
		get() = btSettings.settings

	fun readFromStream(canRead: Boolean): Flow<BluetoothMessage> {
		val mode = _btConfig.displayMode
		val seperator = _btConfig.newLineCharReceive

		return flow {
			while (canRead && socket.isConnected && currentCoroutineContext().isActive) {
				try {
					// check if input stream is available otherwise continue
					if (_inputStream.available() == 0) continue
					// clear the buffer each time before reading
					_buffer.fill(0x0)
					// evalute the message
					val message = readStringFromStream(_inputStream, _buffer, mode)

					val convertedMessage = message.replaceLineSperator(seperator)

					val btMessage = BluetoothMessage(
						message = convertedMessage,
						type = BluetoothMessageType.MESSAGE_FROM_OTHER
					)

					emit(btMessage)
				} catch (e: IOException) {
					Log.e(TRANSFER_LOGGER, "ERROR OCCCURED", e)
					e.printStackTrace()
					break
				}
			}
		}.onCompletion {
			if (!canRead) return@onCompletion
			// ensures logs when the socket is disconnected
			Log.d(TRANSFER_LOGGER, "FINSIHED READING THE FLOW")
		}
			.catch { err -> Log.e(TRANSFER_LOGGER, "ERROR", err) }
			.flowOn(Dispatchers.IO)
	}


	suspend fun writeToStream(value: String): Result<Boolean> {

		val charset = _btConfig.btTerminalCharSet.charset
		val newlineChar = _btConfig.newLineCharSend

		val convertedValue = value.replaceLineSperatorTo(newlineChar)
		val bytes = convertedValue.toByteArray(charset)

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

	@OptIn(ExperimentalStdlibApi::class)
	private fun readStringFromStream(
		stream: InputStream,
		buffer: ByteArray = ByteArray(1024),
		mode: BTTerminalDisplayMode = BTTerminalDisplayMode.DISPLAY_MODE_TEXT
	): String {
		return buildString {
			var bytesRead: Int

			do {
				// read the bytes
				bytesRead = stream.read(buffer)
				// if eof or nothing to read
				if (bytesRead <= 0) break
				val message = when (mode) {
					BTTerminalDisplayMode.DISPLAY_MODE_TEXT -> buffer.decodeToString(endIndex = bytesRead)
					BTTerminalDisplayMode.DISPLAY_MODE_HEX -> buffer.toHexString(endIndex = bytesRead)
				}
				append(message)
			} while (stream.available() != 0 && bytesRead > 0)
		}
	}
}

private fun String.replaceLineSperatorTo(to: BTTerminalNewLineChar): String {
	val previous = System.lineSeparator()
	val replacement = to.value.orEmpty()
	return replace(Regex(previous), replacement)
}

private fun String.replaceLineSperator(present: BTTerminalNewLineChar): String {
	val previous = present.value ?: return this
	val replacement = System.lineSeparator()
	return replace(previous, replacement)
}