package com.eva.bluetoothterminalapp.presentation.feature_le_connect.util

import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel

sealed interface BLECharacteristicEvent {

	data class OnSelectCharacteristic(
		val service: BLEServiceModel,
		val characteristics: BLECharacteristicsModel
	) : BLECharacteristicEvent

	data object OnUnSelectCharactetistic : BLECharacteristicEvent


	data class OnDescriptorRead(val desc: BLEDescriptorModel) : BLECharacteristicEvent

	data object ReadCharacteristic : BLECharacteristicEvent

	data object WriteCharacteristic : BLECharacteristicEvent

	data object OnIndicateCharacteristic : BLECharacteristicEvent

	data object OnNotifyCharacteristic : BLECharacteristicEvent

	data object OnStopNotifyOrIndication : BLECharacteristicEvent
}