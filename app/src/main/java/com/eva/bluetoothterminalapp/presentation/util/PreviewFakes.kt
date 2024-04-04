package com.eva.bluetoothterminalapp.presentation.util

import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceType
import com.eva.bluetoothterminalapp.domain.models.BluetoothMode
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenState
import kotlinx.collections.immutable.toPersistentList


object PreviewFakes {


	val FAKE_DEVICE_MODEL = BluetoothDeviceModel(
		name = "Android HeadSet",
		address = "00:11:22:33:FF:EE",
		mode = BluetoothMode.BLUETOOTH_DEVICE_CLASSIC,
		type = BluetoothDeviceType.AUDIO_VIDEO
	)

	val FAKE_DEVICE_SCREEN_STATE_WITH_BL_OFF = BTDevicesScreenState(
		isBtActive = false,
	)

	val FAKE_DEVICE_SCREEN_STATE_WITH_BL_ON_AND_PAIRED_DEVICES = BTDevicesScreenState(
		isBtActive = true,
		pairedDevices = List(3) {
			FAKE_DEVICE_MODEL.copy(name = "THIS $it", address = "SOME $it")
		}.toPersistentList()
	)

	val FAKE_DEVICE_SCREEN_STATE_WITH_SCANNING_AND_CONNECTED_DEVICE = BTDevicesScreenState(
		isBtActive = true,
		pairedDevices = List(3) {
			FAKE_DEVICE_MODEL.copy(name = "THIS $it", address = "PAIRED SOME $it")
		}.toPersistentList(),
		availableDevices = List(2) {
			FAKE_DEVICE_MODEL.copy(name = "THIS $it", address = "AVAILABLE $it")
		}.toPersistentList()
	)
}