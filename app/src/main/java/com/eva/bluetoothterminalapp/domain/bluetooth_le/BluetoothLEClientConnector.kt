package com.eva.bluetoothterminalapp.domain.bluetooth_le

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.coroutines.flow.StateFlow

interface BluetoothLEClientConnector {

	val connectionState: StateFlow<BLEConnectionState>

	val deviceRssi: StateFlow<Int>

	val bleServices: StateFlow<List<BLEServiceModel>>

	fun connect(address: String): Result<Boolean>

	fun reconnect(): Result<Boolean>

	fun disconnect(): Result<Unit>

	fun close()
}