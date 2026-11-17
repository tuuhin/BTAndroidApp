package com.eva.bluetoothterminalapp.data.bluetooth

import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.UUID

class FakeBluetoothClientConnector : BluetoothClientConnector {

	var connectCount = 0
	var sendCount = 0
	var closeCount = 0
	var failSend = false

	override val connectionState: Flow<ClientConnectionState> = emptyFlow()
	override val remoteDevice: Flow<BluetoothDeviceModel> = emptyFlow()
	override val readIncomingData: Flow<BluetoothMessage> = emptyFlow()

	override suspend fun connectClient(
		address: String,
		connectUUID: UUID,
		secure: Boolean
	): Result<Unit> {
		connectCount++
		return Result.success(Unit)
	}

	override suspend fun loadDeviceFeatureUUID(address: String): Result<List<UUID>> =
		Result.success(emptyList())

	override fun refreshDeviceFeatureUUID(address: String): Flow<List<UUID>> = emptyFlow()

	override suspend fun sendData(data: String, trimData: Boolean): Result<Boolean> =
		Result.success(true)

	override suspend fun sendBytes(data: ByteArray): Result<Boolean> {
		sendCount++
		return if (failSend) Result.failure(IllegalStateException("send failed")) else Result.success(
			true
		)
	}

	override fun closeClient(): Result<Unit> {
		closeCount++
		return Result.success(Unit)
	}
}