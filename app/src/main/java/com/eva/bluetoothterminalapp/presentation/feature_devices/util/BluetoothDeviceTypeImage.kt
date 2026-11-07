package com.eva.bluetoothterminalapp.presentation.feature_devices.util

import androidx.compose.ui.graphics.vector.ImageVector
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.BluetoothDeviceType
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors

val BluetoothDeviceModel.imageVector: ImageVector
	get() = when (type) {
		BluetoothDeviceType.MISC -> AppIconVectors.homeStorageGear
		BluetoothDeviceType.COMPUTER -> AppIconVectors.computerConnected
		BluetoothDeviceType.PHONE -> AppIconVectors.mobileHand
		BluetoothDeviceType.NETWORKING -> AppIconVectors.networkNode
		BluetoothDeviceType.AUDIO_VIDEO -> AppIconVectors.audioVideoReceiver
		BluetoothDeviceType.PERIPHERAL -> AppIconVectors.usb
		BluetoothDeviceType.IMAGING -> AppIconVectors.radiology
		BluetoothDeviceType.WEARABLE -> AppIconVectors.watchScreenTime
		BluetoothDeviceType.TOY -> AppIconVectors.toys
		BluetoothDeviceType.HEALTH -> AppIconVectors.ecgHeart
		else -> AppIconVectors.deviceUnknown
	}