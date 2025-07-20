package com.eva.bluetoothterminalapp.domain.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface BluetoothClientConnector {

	/**
	 * Provides [ClientConnectionState] as a stateflow,the connection mode determine what the connection
	 * is doing currently
	 * @see [ClientConnectionState]
	 */
	val connectionState: Flow<ClientConnectionState>

	val remoteDevice: Flow<BluetoothDeviceModel>

	/**
	 * Connect the client either to a device via its UUID or current server via specified uuid
	 * @param address Address of the [BluetoothDevice]
	 * @param connectUUID client connection to the specified uuid
	 * @param secure Is connection over secure RFComm socket
	 * @return [Result] indicating is everything gone correct and socket is accepted.
	 */
	suspend fun connectClient(address: String, connectUUID: UUID, secure: Boolean = true)
			: Result<Unit>

	/**
	 * Loads the available feature uuid if available otherwise a empty list
	 * @see refreshDeviceFeatureUUID
	 */
	suspend fun loadDeviceFeatureUUID(address: String): Result<List<UUID>>

	/**
	 * fetches device feature uuids mostly done if a new feature is added or
	 * new device is to be paired
	 */
	fun refreshDeviceFeatureUUID(address: String): Flow<List<UUID>>

	/**
	 * Reads the incoming data for the connect client
	 * @return A [Flow] of [BluetoothMessage]
	 */
	val readIncomingData: Flow<BluetoothMessage>

	/**
	 * Sends the information to the end server from this device
	 * @param data [ByteArray] of the data to be sent
	 * @param trimData whether to trim the empty spaces around the given string
	 * @return [Result] inculcating data is sent without any error
	 */
	suspend fun sendData(data: String, trimData: Boolean = true): Result<Boolean>

	/**
	 * Closes the [BluetoothSocket] to which the client is connected
	 * @return [Result] indicating if sockets are closed properly
	 */
	fun closeClient(): Result<Unit>

}