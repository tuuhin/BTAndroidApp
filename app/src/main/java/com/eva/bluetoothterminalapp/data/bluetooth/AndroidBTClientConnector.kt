package com.eva.bluetoothterminalapp.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.exceptions.BluetoothPermissionNotProvided
import com.eva.bluetoothterminalapp.domain.exceptions.InvalidBluetoothAddressException
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

private const val CLIENT_LOGGER = "CLIENT_LOGGER"

@SuppressLint("MissingPermission")
class AndroidBTClientConnector(
	private val context: Context
) : BluetoothClientConnector {

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	private val _bTManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _btAdapter: BluetoothAdapter?
		get() = _bTManager?.adapter

	private val _hasBtPermission: Boolean
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.BLUETOOTH_CONNECT
			) == PermissionChecker.PERMISSION_GRANTED
		else true


	private val _connectState = MutableStateFlow(ClientConnectionState.CONNECTION_INITIALIZING)
	override val connectionState: StateFlow<ClientConnectionState>
		get() = _connectState.asStateFlow()


	private var _transferService: BluetoothTransferService? = null
	private var _btClientSocket: BluetoothSocket? = null

	private var _isConnectReceiverRegistered = false

	/**
	 * [BroadcastReceiver] to listen to connection events like bonding ,connect
	 * disconnect etc
	 */
	private val remoteConnectInfoReceiver = RemoteConnectionReceiver(
		onResults = { connected, _ ->
			_connectState.update { connected }
		},
	)

	override suspend fun connectClient(
		address: String,
		connectUUID: UUID,
		secure: Boolean
	): Result<Unit> = withContext(Dispatchers.IO) {
		// if no permission don't do a thing
		if (!_hasBtPermission)
			return@withContext Result.failure(BluetoothPermissionNotProvided())
		// get the remote device
		val device = _btAdapter?.getRemoteDevice(address)
			?: return@withContext Result.failure(InvalidBluetoothAddressException())

		// remote connection receiver
		setRemoteConnectionReceiver()

		_btClientSocket = if (secure) device.createRfcommSocketToServiceRecord(connectUUID)
		else device.createInsecureRfcommSocketToServiceRecord(connectUUID)

		Log.d(CLIENT_LOGGER, "CREATED_SOCKET SECURE:$secure SPECIFIED UUID: $connectUUID")

		//  stop if any discovery is running
		if (_btAdapter?.isDiscovering == true)
			_btAdapter?.cancelDiscovery()

		try {
			_btClientSocket?.let { socket ->
				// blocks the thread until a connection is found
				socket.connect()
				Log.d(CLIENT_LOGGER, "CLIENT CONNECTED")
				// set socket
				_transferService = BluetoothTransferService(socket)
				// set connection mode to accepted
				_connectState.update { ClientConnectionState.CONNECTION_ACCEPTED }
			}
			Result.success(Unit)
		} catch (e: IOException) {
			e.printStackTrace()
			// if any exception occurred it's a denied connection
			_connectState.update { ClientConnectionState.CONNECTION_DENIED }
			Result.failure(e)
		}
	}


	override fun fetchUUIDs(address: String): Flow<List<UUID>> = callbackFlow {
		val device = _btAdapter?.getRemoteDevice(address)

		val remoteDeviceUUIDReceiver = RemoteDeviceUUIDReceiver(
			onReceivedUUIDs = { uuids ->
				val uuidSet = uuids.toSet().toList()
				Log.d(CLIENT_LOGGER, "FOUND UUIDS: $uuids")
				scope.launch { send(uuidSet) }
			},
		)

		ContextCompat.registerReceiver(
			context,
			remoteDeviceUUIDReceiver,
			IntentFilter(BluetoothDevice.ACTION_UUID),
			ContextCompat.RECEIVER_EXPORTED
		)
		// fetch uuids using service discovery protocol
		val isOk = device?.fetchUuidsWithSdp()
		Log.d(CLIENT_LOGGER, "DEVICE UUID FETCH STATUS $isOk")

		awaitClose {
			context.unregisterReceiver(remoteDeviceUUIDReceiver)
			scope.cancel()
			Log.d(CLIENT_LOGGER, "RECEIVER_FOR_UUID_REMOVED")
		}
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override val readIncomingData: Flow<BluetoothMessage>
		get() = _connectState.flatMapLatest { status ->
			// start reading from the flow is status is connected otherwise it
			// will return an empty flow
			val canRead = status == ClientConnectionState.CONNECTION_ACCEPTED
			_transferService?.readFromStream(canRead = canRead) ?: emptyFlow()
		}.flowOn(Dispatchers.IO)


	override suspend fun sendData(data: String): Result<Boolean> {
		val infoAsByteArray = data.trim().encodeToByteArray()
		return _transferService?.writeToStream(infoAsByteArray)
			?: Result.success(false)
	}


	override fun closeClient(): Result<Unit> {
		if (_btClientSocket == null) return Result.success(Unit)
		return try {
			Log.d(CLIENT_LOGGER, "CLOSING CONNECTION")
			//close the client socket
			_btClientSocket?.close()
			//update the status
			_connectState.update { ClientConnectionState.CONNECTION_DISCONNECTED }
			// set socket and service to null
			_btClientSocket = null
			_transferService = null
			Result.success(Unit)
		} catch (e: IOException) {
			Log.d(CLIENT_LOGGER, "CANNOT CLOSE CONNECTION")
			e.printStackTrace()
			Result.failure(e)
		}
	}

	private fun setRemoteConnectionReceiver() {

		if (_isConnectReceiverRegistered) return

		val filters = IntentFilter().apply {
			// bonded devices changed
			addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
			// connection establish with a remote device
			addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
			// disconnect a remote device
			addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
		}

		ContextCompat.registerReceiver(
			context,
			remoteConnectInfoReceiver,
			filters,
			ContextCompat.RECEIVER_EXPORTED
		)

	}

	override fun releaseResources() {
		try {
			// cancel the scope
			Log.d(CLIENT_LOGGER, "CLOSING CLIENT INFO RECEIVER")
			context.unregisterReceiver(remoteConnectInfoReceiver)
			_isConnectReceiverRegistered = false

		} catch (e: Exception) {
			Log.d(CLIENT_LOGGER, "CANNOT REMOVE RECEIVER")
		}
	}

}