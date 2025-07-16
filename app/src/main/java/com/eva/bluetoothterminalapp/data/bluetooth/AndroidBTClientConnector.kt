package com.eva.bluetoothterminalapp.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothSocketException
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.bluetooth.receivers.RemoteConnectionReceiver
import com.eva.bluetoothterminalapp.data.bluetooth.receivers.RemoteDeviceUUIDReceiver
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.data.utils.hasBTConnectPermission
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.exceptions.BluetoothConnectException
import com.eva.bluetoothterminalapp.domain.exceptions.BluetoothPermissionNotProvided
import com.eva.bluetoothterminalapp.domain.exceptions.InvalidBluetoothAddressException
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

private const val CLIENT_LOGGER = "CLIENT_LOGGER"

@SuppressLint("MissingPermission")
class AndroidBTClientConnector(
	private val context: Context,
	private val btSettings: BTSettingsDataSore,
) : BluetoothClientConnector {

	private val _bTManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _btAdapter: BluetoothAdapter?
		get() = _bTManager?.adapter

	private val _hasBtPermission: Boolean
		get() = context.hasBTConnectPermission

	private val _connectState = MutableStateFlow(ClientConnectionState.CONNECTION_INITIALIZING)
	override val connectionState: Flow<ClientConnectionState>
		get() = merge(androidBTConnectionFlow, _connectState)


	private var _transferService: BluetoothTransferService? = null
	private var _btClientSocket: BluetoothSocket? = null

	override suspend fun connectClient(address: String, connectUUID: UUID, secure: Boolean)
			: Result<BluetoothDeviceModel> {
		// if no permission don't do a thing
		if (!_hasBtPermission)
			return Result.failure(BluetoothPermissionNotProvided())
		// get the remote device
		val device = withContext(Dispatchers.IO) {
			_btAdapter?.getRemoteDevice(address)
		} ?: return Result.failure(InvalidBluetoothAddressException())

		if (secure && device.bondState == BluetoothDevice.BOND_NONE) {
			// if the device is not bonded with the device
			Log.d(CLIENT_LOGGER, "BONDING DEVICE")
			device.createBond()
		}

		_btClientSocket = if (secure) device.createRfcommSocketToServiceRecord(connectUUID)
		else device.createInsecureRfcommSocketToServiceRecord(connectUUID)

		Log.d(CLIENT_LOGGER, "CREATED_SOCKET SECURE:$secure SPECIFIED UUID: $connectUUID")

		//  stop if any discovery is running
		if (_btAdapter?.isDiscovering == true)
			_btAdapter?.cancelDiscovery()

		val socket = _btClientSocket ?: return Result.failure(Exception("Client Socket Not ready"))

		return withContext(Dispatchers.Default) {
			try {
				// blocks the thread until a connection is found
				socket.connect()
				Log.d(CLIENT_LOGGER, "CLIENT CONNECTED")
				// set socket
				_transferService = BluetoothTransferService(socket, btSettings)
				// set connection mode to accepted
				_connectState.update { ClientConnectionState.CONNECTION_ACCEPTED }

				Result.success(device.toDomainModel())
			} catch (e: CancellationException) {
				// close the connection if any
				closeClient()
				// throw cancellation exception
				Log.i(CLIENT_LOGGER, "CONNECTION COROUTINE IS CANCELLED")
				throw e
			} catch (e: Exception) {
				// close the connection if any
				closeClient()
				_connectState.update { ClientConnectionState.CONNECTION_DENIED }
				//error handling
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && e is BluetoothSocketException) {
					return@withContext Result.failure(
						BluetoothConnectException(e.errorCode, e.message ?: "")
					)
				}
				e.printStackTrace()
				// if any exception occurred it's a denied connection
				Result.failure(e)
			}
		}
	}


	override fun fetchUUIDs(address: String): Flow<List<UUID>> = callbackFlow {
		val device = _btAdapter?.getRemoteDevice(address)

//		if (device?.bondState == BluetoothDevice.BOND_NONE)
//			device.createBond()

		val remoteDeviceUUIDReceiver = RemoteDeviceUUIDReceiver(
			onReceivedUUIDs = { uuids ->
				// remove the client-server uuid
				val uuidSet = uuids.distinct()
				Log.d(CLIENT_LOGGER, "FOUND UUIDS: $uuids")
				launch { send(uuidSet) }
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
		val valueToSend = data.trim()
		if (valueToSend.isEmpty()) return Result.success(false)

		return _transferService?.writeToStream(value = valueToSend)
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


	val androidBTConnectionFlow: Flow<ClientConnectionState>
		get() = callbackFlow {

			trySend(ClientConnectionState.CONNECTION_INITIALIZING)

			val remoteConnectInfoReceiver = RemoteConnectionReceiver(
				onResults = { newState, _ -> trySend(newState) },
			)

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

			awaitClose {
				// cancel the scope
				Log.d(CLIENT_LOGGER, "CLOSING CLIENT INFO RECEIVER")
				context.unregisterReceiver(remoteConnectInfoReceiver)
			}
		}.scan(ClientConnectionState.CONNECTION_INITIALIZING) { old, new ->
			if (old.checkCorrectNextState(new)) new else old
		}
}