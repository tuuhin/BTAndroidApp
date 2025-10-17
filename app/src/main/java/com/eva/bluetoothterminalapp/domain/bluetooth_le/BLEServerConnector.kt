package com.eva.bluetoothterminalapp.domain.bluetooth_le

import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServerServices
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BLEServerConnector {

	val connectedDevices: Flow<List<BluetoothDeviceModel>>

	val services: Flow<List<BLEServiceModel>>

	val isServerRunning: StateFlow<Boolean>

	val errorsFlow: Flow<Exception>

	fun onStartServer(options: Set<BLEServerServices> = emptySet()): Result<Boolean>

	fun onStopServer()

	fun cleanUp()
}