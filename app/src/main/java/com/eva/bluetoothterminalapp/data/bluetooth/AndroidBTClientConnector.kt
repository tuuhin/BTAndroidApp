package com.eva.bluetoothterminalapp.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.models.BTClientStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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


	private val _connectMode = MutableStateFlow(BTClientStatus.CONNECTION_INITIALIZED)
	override val isConnected: StateFlow<BTClientStatus>
		get() = _connectMode.asStateFlow()


	private val _otherDeviceUUID = MutableSharedFlow<UUID>()
	private val _isRFSocketSecure = MutableStateFlow(false)

	private var _btClientSocket: BluetoothSocket? = null


	private val remoteConnectInfoReceiver = RemoteConnectionReceiver { connected, _ ->

		_connectMode.update { connected }
	}


	private val remoteDeviceUUIDReceiver = RemoteDeviceUUIDReceiver { uuids ->

		val receivedUUID = uuids.firstOrNull() ?: return@RemoteDeviceUUIDReceiver
		scope.launch { _otherDeviceUUID.emit(receivedUUID) }
		Log.d(CLIENT_LOGGER, "UUID FOUND $receivedUUID")
	}

	override suspend fun connectClient(
		address: String,
		connectAsClient: Boolean,
		secure: Boolean
	) {
		// change the secure type for the receiver
		_isRFSocketSecure.update { secure }
		// remote connection receiver
		setRemoteConnectionReceiver()

		withContext(Dispatchers.IO) {
			// if no permission don't do a thing
			if (!_hasBtPermission) return@withContext
			// get the remote device
			val device = _btAdapter?.getRemoteDevice(address)

			if (connectAsClient) connectWithSpecifiedUUID(secure = secure, device = device)
			else connectWithDeviceUUID(secure = secure, device = device)
		}
	}

	private suspend fun connectWithDeviceUUID(secure: Boolean, device: BluetoothDevice?) {
		//fetch uuids
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

		// stop discovery
		_btAdapter?.cancelDiscovery()

		// create the socket
		_btClientSocket = if (secure) device?.createRfcommSocketToServiceRecord(deviceUUID)
		else device?.createInsecureRfcommSocketToServiceRecord(deviceUUID)

		Log.d(CLIENT_LOGGER, "CREATED_SOCKET")

		try {
			_btClientSocket?.let { socket ->
				// blocking call
				socket.connect()
				Log.d(CLIENT_LOGGER, "CONNECTED")
				_connectMode.update { BTClientStatus.CONNECTION_ACCEPTED }
			}
		} catch (e: IOException) {
			e.printStackTrace()
			_connectMode.update { BTClientStatus.CONNECTION_DENIED }
		}
	}

	private fun connectWithSpecifiedUUID(
		secure: Boolean,
		device: BluetoothDevice?,
	) {

		val connectionUUID = UUID.fromString(BTConstants.SERVICE_UUID)
		_btClientSocket = if (secure) device?.createRfcommSocketToServiceRecord(connectionUUID)
		else device?.createInsecureRfcommSocketToServiceRecord(connectionUUID)

		Log.d(CLIENT_LOGGER, "CREATED_SOCKET SECURE:$secure")

		//stop discovery then try to connect
		_btAdapter?.cancelDiscovery()

		try {
			_btClientSocket?.let { socket ->
				// blocking call
				socket.connect()
				Log.d(CLIENT_LOGGER, "CONNECTED")
				_connectMode.update { BTClientStatus.CONNECTION_ACCEPTED }
			}
		} catch (e: IOException) {
			e.printStackTrace()
			_connectMode.update { BTClientStatus.CONNECTION_DENIED }
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}


	override fun closeClient() {
		try {
			Log.d(CLIENT_LOGGER, "CLOSING CONNECTION")

			_btClientSocket?.close()
			_btClientSocket = null
		} catch (e: IOException) {
			Log.d(CLIENT_LOGGER, "CANNOT CLOSE CONNECTION")
			e.printStackTrace()
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