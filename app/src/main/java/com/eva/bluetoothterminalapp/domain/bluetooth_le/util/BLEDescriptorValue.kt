package com.eva.bluetoothterminalapp.domain.bluetooth_le.util

sealed interface BLEDescriptorValue {
	data object EnableNotification : BLEDescriptorValue
	data object EnableIndication : BLEDescriptorValue
	data object DisableNotifyOrIndication : BLEDescriptorValue
	data class ReadableValue(val string: String? = null) : BLEDescriptorValue
}