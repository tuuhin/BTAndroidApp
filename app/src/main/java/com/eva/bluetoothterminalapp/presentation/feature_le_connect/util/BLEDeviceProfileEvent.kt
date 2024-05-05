package com.eva.bluetoothterminalapp.presentation.feature_le_connect.util

import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel

sealed interface BLEDeviceProfileEvent {

	data class OnSelectCharacteristic(
		val characteristics: BLECharacteristicsModel
	) : BLEDeviceProfileEvent

	data object OnCharcteristicsConfirmed : BLEDeviceProfileEvent
}