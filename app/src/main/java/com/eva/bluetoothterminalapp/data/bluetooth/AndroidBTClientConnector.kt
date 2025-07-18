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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
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
	private val _remoteDevice = MutableStateFlow<BluetoothDeviceModel?>(null)


	override val connectionState: Flow<ClientConnectionState>
		get() = merge(androidBTConnectionFlow, _connectState)
			.scan(ClientConnectionState.CONNECTION_INITIALIZING) { old, new ->
				Log.d(CLIENT_LOGGER, "PREVIOUS: $old NEW:$new")
				if (old.checkCorrectNextState(new)) new else old
			}

	override val remoteDevice: Flow<BluetoothDeviceModel>
		get() = _remoteDevice.filterNotNull()


	private var _transferService: BluetoothTransferService? = null
	private var _btClientSocket: BluetoothSocket? = null

	override suspend fun connectClient(address: String, connectUUID: UUID, secure: Boolean)
			: Result<Unit> {
		// if no permission don't do a thing
		if (!_hasBtPermission)
			return Result.failure(BluetoothPermissionNotProvided())
		// get the remote device
		val deviceResult = bluetoothDeviceFromAddress(address)
		if (deviceResult.isFailure) return Result.failure(deviceResult.exceptionOrNull()!!)
		val device = deviceResult.getOrThrow()
		_remoteDevice.update { device.toDomainModel() }

		if (secure && device.bondState == BluetoothDevice.BOND_NONE) {
			// if the device is not bonded with the device
			Log.d(CLIENT_LOGGER, "BONDING DEVICE")
			device.createBond()
		}

		return withContext(Dispatchers.IO) {
			try {
				_btClientSocket = if (secure) device.createRfcommSocketToServiceRecord(connectUUID)
				else device.createInsecureRfcommSocketToServiceRecord(connectUUID)

				Log.d(CLIENT_LOGGER, "CREATED_SOCKET SECURE:$secure SPECIFIED UUID: $connectUUID")

				//  stop if any discovery is running
				if (_btAdapter?.isDiscovering == true)
					_btAdapter?.cancelDiscovery()

				val socket = _btClientSocket
					?: return@withContext Result.failure(Exception("Client Socket Not ready"))

				// blocks the thread until a connection is found
				socket.connect()
				Log.d(CLIENT_LOGGER, "CLIENT CONNECTED")
				// set socket
				_transferService = BluetoothTransferService(socket, btSettings)
				// set connection mode to accepted
				_connectState.update { ClientConnectionState.CONNECTION_CONNECTED }
				Result.success(Unit)
			} catch (e: Exception) {
				// close the connection if any
				closeClient()
				_connectState.update { ClientConnectionState.CONNECTION_DENIED }
				//error handling
				if (e is CancellationException) {
					Log.d(CLIENT_LOGGER, "CONNECTION COROUTINE IS CANCELLED")
					throw e
				} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && e is BluetoothSocketException) {
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

	override suspend fun loadDeviceFeatureUUID(address: String): Result<List<UUID>> {
		val deviceResult = bluetoothDeviceFromAddress(address)
		if (deviceResult.isFailure) return Result.failure(deviceResult.exceptionOrNull()!!)
		val device = deviceResult.getOrThrow()

		val probableUUIDS = device.uuids?.map { it.uuid }?.distinct() ?: emptyList()
		return Result.success(probableUUIDS)
	}

	override fun refreshDeviceFeatureUUID(address: String): Flow<List<UUID>> = callbackFlow {
		val deviceResult = bluetoothDeviceFromAddress(address)
		val device = deviceResult.getOrNull()

//		if (device?.bondState == BluetoothDevice.BOND_NONE)
//			device.createBond()

		val remoteDeviceUUIDReceiver = RemoteDeviceUUIDReceiver(
			onReceivedUUIDs = { uuids ->
				// remove the client-server uuid
				val uuidSet = uuids.distinct()
				Log.d(CLIENT_LOGGER, "SEARCHED FOUND UUIDS: $uuids")
				trySend(uuidSet)
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
			val canRead = status == ClientConnectionState.CONNECTION_CONNECTED
			_transferService?.readFromStream(canRead = canRead) ?: emptyFlow()
		}


	override suspend fun sendData(data: String, trimData: Boolean): Result<Boolean> {
		val valueToSend = if (trimData) data.trim() else data
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
			_remoteDevice.update { null }
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


	private suspend fun bluetoothDeviceFromAddress(address: String): Result<BluetoothDevice> {
		return withContext(Dispatchers.IO) {
			try {
				val isValid = BluetoothAdapter.checkBluetoothAddress(address)
				if (!isValid)
					return@withContext Result.failure(InvalidBluetoothAddressException())
				val device = _btAdapter?.getRemoteDevice(address)
					?: return@withContext Result.failure(InvalidBluetoothAddressException())
				Result.success(device)
			} catch (_: IllegalArgumentException) {
				Result.failure(InvalidBluetoothAddressException())
			}
		}
	}

	private val androidBTConnectionFlow: Flow<ClientConnectionState>
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

			Log.d(CLIENT_LOGGER, " CONNECTION INFO RECEIVER")

			awaitClose {
				// cancel the scope
				Log.d(CLIENT_LOGGER, "CLOSING CLIENT CONNECTION INFO RECEIVER")
				context.unregisterReceiver(remoteConnectInfoReceiver)
			}
		}
}