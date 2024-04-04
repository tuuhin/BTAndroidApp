package com.eva.bluetoothterminalapp.presentation.feature_devices.state

sealed interface DeviceScreenEvents {
	data object StartScan : DeviceScreenEvents
	data object StopScan : DeviceScreenEvents

}