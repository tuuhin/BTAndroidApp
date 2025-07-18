package com.eva.bluetoothterminalapp.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.data.utils.hasBTConnectPermission
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothServerConnector
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import com.eva.bluetoothterminalapp.presentation.util.BTConstants
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.IOException

private const val SERVER_LOGGER = "SERVER_LOGGER"

@SuppressLint("MissingPermission")
class AndroidBTServerConnector(
	private val context: Context,
	private val settings: BTSettingsDataSore,
) : BluetoothServerConnector {

	private val _btManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _btAdapter: BluetoothAdapter?
		get() = _btManager?.adapter

	private val _hasBtPermission: Boolean
		get() = context.hasBTConnectPermission

	private val _connectMode = MutableStateFlow(ServerConnectionState.CONNECTION_INITIALIZING)
	override val connectMode: StateFlow<ServerConnectionState>
		get() = _connectMode.asStateFlow()

	private val _remoteDevice = MutableStateFlow<BluetoothDeviceModel?>(null)
	override val remoteDevice: Flow<BluetoothDeviceModel>
		get() = _remoteDevice.filterNotNull()

	private var _serverSocket: BluetoothServerSocket? = null
	private var _clientSocket: BluetoothSocket? = null
	private var _transferService: BluetoothTransferService? = null

	override suspend fun startServer(secure: Boolean) {
		// if no permission provided return
		if (!_hasBtPermission) return

		withContext(Dispatchers.IO) {
			try {
				//creating socket based on the choice
				_serverSocket = if (secure) _btAdapter?.listenUsingRfcommWithServiceRecord(
					BTConstants.SERVICE_NAME,
					BTConstants.SERVICE_UUID
				)
				else _btAdapter?.listenUsingInsecureRfcommWithServiceRecord(
					BTConstants.SERVICE_NAME,
					BTConstants.SERVICE_UUID
				)


				Log.d(SERVER_LOGGER, "SOCKET CREATED LISTENING FOR CONNECTION")
				_connectMode.update { ServerConnectionState.CONNECTION_LISTENING }

				do {
					// ensure the coroutine is active
					ensureActive()
					// loop is required as multiple clients can try to join
					// the server, but we only take one and the first successful request
					_clientSocket = try {
						// block until a connection is accepted
						_serverSocket?.accept()
					} catch (e: IOException) {
						// this connection is rejected waiting for the next one
						Log.d(SERVER_LOGGER, "WAITING FOR ACCEPTANCE", e)
						null
					}
					// socket is accepted
					val clientSocket = _clientSocket ?: continue
					_transferService = BluetoothTransferService(clientSocket, settings)

					_connectMode.update { ServerConnectionState.CONNECTION_ACCEPTED }
					_remoteDevice.update { clientSocket.remoteDevice.toDomainModel() }

					Log.d(SERVER_LOGGER, "NEW DEVICE CONNECTED")
					// as only one client can be connected once thus closing the server socket
					_serverSocket?.close()
					_serverSocket = null
				} while (_clientSocket == null)
			} catch (e: Exception) {
				if (e is CancellationException) {
					Log.d(SERVER_LOGGER, "COROUTINE IS CANCELLED !!")
					throw e
				} else if (e is IOException) {
					Log.e(SERVER_LOGGER, "BLUETOOTH CONNECTION EXCEPTION", e)
				}
			}
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override val readIncomingData: Flow<BluetoothMessage>
		get() = _connectMode.flatMapLatest { status ->
			// start reading from the flow is status is connected otherwise it
			// will return an empty flow
			val canRead = status == ServerConnectionState.CONNECTION_ACCEPTED
			_transferService?.readFromStream(canRead = canRead) ?: emptyFlow()
		}.flowOn(Dispatchers.IO)


	override suspend fun sendData(data: String, trimData: Boolean): Result<Boolean> {
		val valueString = if (trimData) data.trim() else data
		return _transferService?.writeToStream(valueString)
			?: Result.success(false)
	}


	override fun closeServer() {
		try {
			_clientSocket?.close()
			_serverSocket?.close()
			_clientSocket = null
			_serverSocket = null
			_remoteDevice.update { null }
			_connectMode.update { ServerConnectionState.CONNECTION_DISCONNECTED }
			Log.d(SERVER_LOGGER, "CLOSED SERVER SUCCESSFULLY")
		} catch (e: IOException) {
			Log.e(SERVER_LOGGER, "CANNOT CLOSE SERVER SOCKET", e)
		}
	}
}