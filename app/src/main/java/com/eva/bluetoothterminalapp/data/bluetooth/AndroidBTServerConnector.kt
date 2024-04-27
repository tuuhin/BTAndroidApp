package com.eva.bluetoothterminalapp.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothServerConnector
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.models.ServerConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.IOException

private const val SERVER_LOGGER = "SERVER_LOGGER"

@SuppressLint("MissingPermission")
class AndroidBTServerConnector(
	private val context: Context
) : BluetoothServerConnector {

	private val _btManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _btAdapter: BluetoothAdapter?
		get() = _btManager?.adapter

	private val _hasBtPermission: Boolean
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.BLUETOOTH_CONNECT
			) == PermissionChecker.PERMISSION_GRANTED
		else true

	private val _connectMode = MutableStateFlow(ServerConnectionState.CONNECTION_INITIALIZING)
	override val connectMode: StateFlow<ServerConnectionState>
		get() = _connectMode.asStateFlow()

	private var _serverSocket: BluetoothServerSocket? = null
	private var _clientSocket: BluetoothSocket? = null
	private var _transferService: BluetoothTransferService? = null

	override suspend fun startServer(secure: Boolean) = withContext(Dispatchers.IO) {
		// if no permission provided return
		if (!_hasBtPermission) return@withContext


		//creating socket based on the choice
		_serverSocket = if (secure)
			_btAdapter?.listenUsingRfcommWithServiceRecord(
				BTConstants.SERVICE_NAME,
				BTConstants.SERVICE_UUID
			)
		else
			_btAdapter?.listenUsingRfcommWithServiceRecord(
				BTConstants.SERVICE_NAME,
				BTConstants.SERVICE_UUID
			)

		Log.d(SERVER_LOGGER, "SOCKET CREATED LISTENING FOR CONNECTION")
		_connectMode.update { ServerConnectionState.CONNECTION_LISTENING }

		// loop is required as multiple clients can try to join
		// the server, but we only take one successful request
		do {
			_clientSocket = try {
				// block until a connection is accepted
				_serverSocket?.accept()
			} catch (e: IOException) {
				// this connection is rejected waiting for the next one
				e.printStackTrace()
				null
			}
			_clientSocket?.let { socket ->
				_transferService = BluetoothTransferService(socket)
				_connectMode.update { ServerConnectionState.CONNECTION_ACCEPTED }
				Log.d(SERVER_LOGGER, "CONNECTED")
				// as only one client can be connected once thus closing the
				_serverSocket?.close()
				_serverSocket = null
			}
		} while (_clientSocket == null && isActive)
	}


	@OptIn(ExperimentalCoroutinesApi::class)
	override val readIncomingData: Flow<BluetoothMessage>
		get() = _connectMode.flatMapLatest { status ->
			// start reading from the flow is status is connected otherwise it
			// will return an empty flow
			val canRead = status == ServerConnectionState.CONNECTION_ACCEPTED
			_transferService?.readFromStream(canRead = canRead) ?: emptyFlow()
		}.flowOn(Dispatchers.IO)


	override suspend fun sendData(data: String): Result<Boolean> {
		val infoAsByteArray = data.trim().encodeToByteArray()
		return _transferService?.writeToStream(infoAsByteArray)
			?: Result.success(false)
	}


	override fun closeServer() {
		try {
			_clientSocket?.close()
			_serverSocket?.close()
			_connectMode.update { ServerConnectionState.CONNECTION_DISCONNECTED }
			Log.d(SERVER_LOGGER, "CLOSED SERVER SUCCESSFULLY")
		} catch (e: IOException) {
			Log.e(SERVER_LOGGER, "CANNOT CLOSE SERVER SOCKET", e)
		}
	}
}