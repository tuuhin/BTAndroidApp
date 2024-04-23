package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.BluetoothGattDescriptor
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptor

fun BluetoothGattDescriptor.toModel(): BLEDescriptor = BLEDescriptor(
	uuid = uuid,
	permissions = permission,
)


private val BluetoothGattDescriptor.permission: BLEPermission
	get() = when (permissions) {
		BluetoothGattDescriptor.PERMISSION_READ -> BLEPermission.PERMISSION_READ
		BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED -> BLEPermission.PERMISSION_READ_ENCRYPTED
		BluetoothGattDescriptor.PERMISSION_WRITE -> BLEPermission.PERMISSION_WRITE
		BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED -> BLEPermission.PERMISSION_WRITE_ENCRYPTED
		BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED -> BLEPermission.PERMISSION_WRITE_SIGNED
		BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM -> BLEPermission.PERMISSION_READ_ENCRYPTED_MITM
		BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM -> BLEPermission.PERMISSION_WRITE_ENCRYPTED_MITM
		BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM -> BLEPermission.PERMISSION_WRITE_SIGNED_MITM
		else -> BLEPermission.PERMISSION_UNKNOWN
	}