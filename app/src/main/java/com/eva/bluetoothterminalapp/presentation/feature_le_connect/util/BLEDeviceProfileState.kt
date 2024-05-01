package com.eva.bluetoothterminalapp.presentation.feature_le_connect.util

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class BLEDeviceProfileState(
	val connectionState: BLEConnectionState = BLEConnectionState.UNKNOWN,
	val device: BluetoothDeviceModel? = null,
	val signalStrength: Int = 0,
	val services: ImmutableList<BLEServiceModel> = persistentListOf()
)
