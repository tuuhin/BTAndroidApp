package com.eva.bluetoothterminalapp.domain.bluetooth_le

import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.coroutines.flow.Flow

interface BLEServerConnector {

	val connectedDevices: Flow<List<BluetoothDeviceModel>>

	val services: Flow<List<BLEServiceModel>>

	suspend fun onStartServer()

	suspend fun onStopServer()
}