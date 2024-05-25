package com.eva.bluetoothterminalapp.presentation.feature_devices.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.DeviceUnknown
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MiscellaneousServices
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.NetworkPing
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Toys
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.ui.graphics.vector.ImageVector
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.BluetoothDeviceType
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel

val BluetoothDeviceModel.imageVector: ImageVector
	get() = when (type) {
		BluetoothDeviceType.MISC -> Icons.Outlined.MiscellaneousServices
		BluetoothDeviceType.COMPUTER -> Icons.Outlined.Computer
		BluetoothDeviceType.PHONE -> Icons.Outlined.PhoneAndroid
		BluetoothDeviceType.NETWORKING -> Icons.Outlined.NetworkPing
		BluetoothDeviceType.AUDIO_VIDEO -> Icons.Outlined.Audiotrack
		BluetoothDeviceType.PERIPHERAL -> Icons.Outlined.MiscellaneousServices
		BluetoothDeviceType.IMAGING -> Icons.Outlined.Image
		BluetoothDeviceType.WEARABLE -> Icons.Outlined.Watch
		BluetoothDeviceType.TOY -> Icons.Outlined.Toys
		BluetoothDeviceType.HEALTH -> Icons.Outlined.MonitorHeart
		else -> Icons.Outlined.DeviceUnknown
	}