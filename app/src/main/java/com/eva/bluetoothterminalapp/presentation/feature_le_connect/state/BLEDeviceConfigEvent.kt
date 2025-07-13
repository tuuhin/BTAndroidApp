package com.eva.bluetoothterminalapp.presentation.feature_le_connect.state

sealed interface BLEDeviceConfigEvent {

	data object OnDisconnectEvent : BLEDeviceConfigEvent

	data object OnReconnectEvent : BLEDeviceConfigEvent

	data object OnReadRssiStrength : BLEDeviceConfigEvent

	data object OnReDiscoverServices : BLEDeviceConfigEvent
}