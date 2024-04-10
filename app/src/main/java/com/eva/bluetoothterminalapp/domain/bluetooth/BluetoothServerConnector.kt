package com.eva.bluetoothterminalapp.domain.bluetooth

import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.models.ServerConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothServerConnector {

	val connectMode: StateFlow<ServerConnectionState>

	suspend fun startServer(secure: Boolean = true)


	val readIncomingData: Flow<BluetoothMessage>

	suspend fun sendData(data: String): Result<Boolean>

	fun closeServer()
}