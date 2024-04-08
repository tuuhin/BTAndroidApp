package com.eva.bluetoothterminalapp.domain.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import com.eva.bluetoothterminalapp.domain.models.BTClientStatus
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothClientConnector {

	/**
	 * Provides [BTClientStatus] as a stateflow,the connection mode determine what the connection
	 * is doing currently
	 * @see [BTClientStatus]
	 */
	val isConnected: StateFlow<BTClientStatus>

	/**
	 * Connect the client either to a device via its UUID or current server via specified uuid
	 * @param address Address of the [BluetoothDevice]
	 * @param connectAsClient Determines if connection is device or server client
	 * @param secure Is connection over secure RFComm socket
	 */
	suspend fun connectClient(
		address: String,
		connectAsClient: Boolean = false,
		secure: Boolean = true,
	)

	/**
	 * Reads the incoming data for the connect client
	 * @return A [Flow] of [BluetoothMessage]
	 */
	val readIncomingData: Flow<BluetoothMessage>

	/**
	 * Sends the information to the end server from this device
	 * @param info [ByteArray] of the data to be sent
	 */
	suspend fun sendData(info: ByteArray): Result<Boolean>

	/**
	 * Closes the [BluetoothSocket] to which the client is connected
	 * @return [Result] indicating if sockets are closed properly
	 */
	fun closeClient(): Result<Unit>

	/**
	 * Closes all the [BroadcastReceiver] for the client
	 */
	fun releaseResources()

}