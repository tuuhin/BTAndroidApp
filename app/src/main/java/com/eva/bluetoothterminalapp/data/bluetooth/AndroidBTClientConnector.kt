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
import com.eva.bluetoothterminalapp.domain.models.BTClientStatus
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

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


	private val _connectMode = MutableStateFlow(BTClientStatus.CONNECTION_INITIALIZING)
	override val isConnected: StateFlow<BTClientStatus>
		get() = _connectMode.asStateFlow()


	private val _otherDeviceUUID = MutableSharedFlow<UUID>()

	private var _transferService: BluetoothTransferService? = null
	private var _btClientSocket: BluetoothSocket? = null

	/**
	 * [BroadcastReceiver] to listen to connection events like bonding ,connect
	 * disconnect etc
	 */
	private val remoteConnectInfoReceiver = RemoteConnectionReceiver { connected, _ ->
		_connectMode.update { connected }
	}

	/**
	 * [BroadcastReceiver] for listening to remote uuid of the device
	 */
	private val remoteDeviceUUIDReceiver = RemoteDeviceUUIDReceiver { uuids ->
		// don't update uuid if its null
		//Log.d(CLIENT_LOGGER,"UUID FOUND : $uuids")
		val receivedUUID = uuids.firstOrNull() ?: return@RemoteDeviceUUIDReceiver
		scope.launch { _otherDeviceUUID.emit(receivedUUID) }
		Log.d(CLIENT_LOGGER, "REMOTE DEVICE UUID $receivedUUID")
	}

	override suspend fun connectClient(
		address: String,
		connectAsClient: Boolean,
		secure: Boolean
	) {

		// remote connection receiver
		setRemoteConnectionReceiver()

		withContext(Dispatchers.IO) {
			// if no permission don't do a thing
			if (!_hasBtPermission) return@withContext
			// get the remote device
			val device = _btAdapter?.getRemoteDevice(address)

			_btClientSocket = if (connectAsClient)
				connectWithSpecifiedUUID(secure = secure, device = device)
			else connectWithDeviceUUID(secure = secure, device = device)

			Log.d(CLIENT_LOGGER, "CREATED_SOCKET SECURE:$secure SPECIFIED UUID: $connectAsClient")

			//  stop if any discovery is running
			if (_btAdapter?.isDiscovering == true) _btAdapter?.cancelDiscovery()

			try {
				_btClientSocket?.let { socket ->
					// blocks the thread until a connection is found
					socket.connect()
					Log.d(CLIENT_LOGGER, "CLIENT CONNECTED")
					// set socket
					_transferService = BluetoothTransferService(socket)
					// set connection mode to accepted
					_connectMode.update { BTClientStatus.CONNECTION_ACCEPTED }
				}
			} catch (e: IOException) {
				e.printStackTrace()
				// if any exception occurred it's a denied connection
				_connectMode.update { BTClientStatus.CONNECTION_DENIED }
			}
		}
	}

	private suspend fun connectWithDeviceUUID(
		secure: Boolean,
		device: BluetoothDevice?
	): BluetoothSocket? {
		// fetch uuids using service discovery protocol
		device?.fetchUuidsWithSdp()
		// register a receiver to get uuid
		ContextCompat.registerReceiver(
			context,
			remoteDeviceUUIDReceiver,
			IntentFilter(BluetoothDevice.ACTION_UUID),
			ContextCompat.RECEIVER_EXPORTED
		)

		Log.d(CLIENT_LOGGER, "RECEIVER_FOR_UUID_REGISTERED")

		val deviceUUID: UUID = _otherDeviceUUID.firstOrBlock()

		// uuid found to unregister the receiver
		context.unregisterReceiver(remoteDeviceUUIDReceiver)
		Log.d(CLIENT_LOGGER, "RECEIVER_FOR_UUID_REMOVED")

		// create the socket
		return if (secure) device?.createRfcommSocketToServiceRecord(deviceUUID)
		else device?.createInsecureRfcommSocketToServiceRecord(deviceUUID)

	}

	private fun connectWithSpecifiedUUID(
		secure: Boolean,
		device: BluetoothDevice?
	): BluetoothSocket? {

		val connectionUUID = UUID.fromString(BTConstants.SERVICE_UUID)

		return if (secure) device?.createRfcommSocketToServiceRecord(connectionUUID)
		else device?.createInsecureRfcommSocketToServiceRecord(connectionUUID)

	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override val readIncomingData: Flow<BluetoothMessage>
		get() = _connectMode.flatMapLatest { status ->
			// start reading from the flow is status is connected otherwise it
			// will return an empty flow
			val canRead = status == BTClientStatus.CONNECTION_ACCEPTED
			_transferService?.readFromStream(canRead = canRead) ?: emptyFlow()
		}.flowOn(Dispatchers.IO)


	override suspend fun sendData(info: ByteArray): Result<Boolean> {
		// don't allow anything to be sent till connection is in accepted mode
		if (_connectMode.value == BTClientStatus.CONNECTION_ACCEPTED)
			return Result.success(false)

		return _transferService?.writeToStream(info) ?: Result.success(false)
	}


	override fun closeClient(): Result<Unit> {
		return try {
			Log.d(CLIENT_LOGGER, "CLOSING CONNECTION")
			//close the client socket
			_btClientSocket?.close()
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
			Log.d(CLIENT_LOGGER, "CLOSING CLIENT INFO RECEIVER")
			context.unregisterReceiver(remoteConnectInfoReceiver)
		} catch (e: Exception) {
			Log.d(CLIENT_LOGGER, "CANNOT REMOVE RECEIVER")
		}
	}

	private suspend fun <T> Flow<T>.firstOrBlock(): T {
		// TODO: SOME FIX LATER
		var possibleValue = firstOrNull()

		while (possibleValue == null) {
			try {
				possibleValue = first()
			} catch (e: NoSuchElementException) {
				Log.d(CLIENT_LOGGER, "STILL WAITING FOR UUID")
				delay(1.seconds)
			}
		}
		return possibleValue
	}
}