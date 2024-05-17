package com.eva.bluetoothterminalapp.domain.bluetooth_le.util

sealed interface BLEDescriptorValue {
	data object EnableNotifcation : BLEDescriptorValue
	data object EnableIndication : BLEDescriptorValue
	data object DisableNotifcationOrIndication : BLEDescriptorValue
	data class ReadableValue(val string: String? = null) : BLEDescriptorValue
}