package com.eva.bluetoothterminalapp.data.mapper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.BluetoothDeviceType
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.BluetoothMode
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel

@SuppressLint("MissingPermission")
fun BluetoothDevice.toDomainModel(): BluetoothDeviceModel = BluetoothDeviceModel(
	// name can return null so there maybe an exception
	name = name ?: BluetoothDeviceModel.UNNAMED_DEVICE_NAME,
	address = address,
	mode = when (type) {
		1 -> BluetoothMode.BLUETOOTH_DEVICE_CLASSIC
		2 -> BluetoothMode.BLUETOOTH_DEVICE_LE
		3 -> BluetoothMode.BLUETOOTH_DEVICE_DUAL
		else -> BluetoothMode.BLUETOOTH_DEVICE_UNKNOWN
	},
	type = when (bluetoothClass.majorDeviceClass) {
		BluetoothClass.Device.Major.AUDIO_VIDEO -> BluetoothDeviceType.AUDIO_VIDEO
		BluetoothClass.Device.Major.COMPUTER -> BluetoothDeviceType.COMPUTER
		BluetoothClass.Device.Major.HEALTH -> BluetoothDeviceType.HEALTH
		BluetoothClass.Device.Major.IMAGING -> BluetoothDeviceType.IMAGING
		BluetoothClass.Device.Major.WEARABLE -> BluetoothDeviceType.WEARABLE
		BluetoothClass.Device.Major.MISC -> BluetoothDeviceType.MISC
		BluetoothClass.Device.Major.PHONE -> BluetoothDeviceType.PHONE
		BluetoothClass.Device.Major.NETWORKING -> BluetoothDeviceType.NETWORKING
		BluetoothClass.Device.Major.TOY -> BluetoothDeviceType.TOY
		BluetoothClass.Device.Major.PERIPHERAL -> BluetoothDeviceType.PERIPHERAL
		else -> BluetoothDeviceType.UNCATEGORIZED
	},
)