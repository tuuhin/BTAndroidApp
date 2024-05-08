package com.eva.bluetoothterminalapp.domain.bluetooth

import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.models.ServerConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothServerConnector {

	/**
	 * Checks the connect mode for the server this helps to check the current state of the server
	 * whether any device is connected or not , is the server is ready or not
	 * @see ServerConnectionState
	 */
	val connectMode: StateFlow<ServerConnectionState>

	/**
	 * Starts the bluetooth classic server
	 * @param secure Indicates if the communication is rf comm secure or not
	 */
	suspend fun startServer(secure: Boolean = true)

	/**
	 * Reads the incomming data from the server
	 */
	val readIncomingData: Flow<BluetoothMessage>

	/**
	 * Sends data to the server
	 * @param data String to be send
	 * @return [Result] indicating if successfully send
	 */
	suspend fun sendData(data: String): Result<Boolean>

	/**
	 * Finally close the server if not required any more
	 */
	fun closeServer()
}