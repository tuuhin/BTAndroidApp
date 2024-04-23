package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEWriteTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel

fun BluetoothGattCharacteristic.toModel(): BLECharacteristicsModel = BLECharacteristicsModel(
	characteristicsId = instanceId,
	uuid = uuid,
	permission = permission,
	property = bleProperty,
	writeType = bleWriteType,
	descriptors = descriptors.map(BluetoothGattDescriptor::toModel)
)


private val BluetoothGattCharacteristic.permission: BLEPermission
	get() = when (permissions) {
		BluetoothGattCharacteristic.PERMISSION_READ -> BLEPermission.PERMISSION_READ
		BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED -> BLEPermission.PERMISSION_READ_ENCRYPTED
		BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM -> BLEPermission.PERMISSION_READ_ENCRYPTED_MITM
		BluetoothGattCharacteristic.PERMISSION_WRITE -> BLEPermission.PERMISSION_WRITE
		BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED -> BLEPermission.PERMISSION_WRITE_ENCRYPTED
		BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED -> BLEPermission.PERMISSION_WRITE_SIGNED
		BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED_MITM -> BLEPermission.PERMISSION_WRITE_SIGNED_MITM
		BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM -> BLEPermission.PERMISSION_WRITE_ENCRYPTED_MITM
		else -> BLEPermission.PERMISSION_UNKNOWN
	}

private val BluetoothGattCharacteristic.bleProperty: BLEPropertyTypes
	get() = when (properties) {
		BluetoothGattCharacteristic.PROPERTY_BROADCAST -> BLEPropertyTypes.PROPERTY_BROADCAST
		BluetoothGattCharacteristic.PROPERTY_INDICATE -> BLEPropertyTypes.PROPERTY_INDICATE
		BluetoothGattCharacteristic.PROPERTY_NOTIFY -> BLEPropertyTypes.PROPERTY_NOTIFY
		BluetoothGattCharacteristic.PROPERTY_READ -> BLEPropertyTypes.PROPERTY_READ
		BluetoothGattCharacteristic.PROPERTY_WRITE -> BLEPropertyTypes.PROPERTY_WRITE
		BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS -> BLEPropertyTypes.PROPERTY_EXTENDED_PROPS
		BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE -> BLEPropertyTypes.PROPERTY_SIGNED_WRITE
		BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE -> BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE
		else -> BLEPropertyTypes.UNKNOWN
	}

private val BluetoothGattCharacteristic.bleWriteType: BLEWriteTypes
	get() = when (writeType) {
		BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT -> BLEWriteTypes.TYPE_DEFAULT
		BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE -> BLEWriteTypes.TYPE_NO_RESPONSE
		BluetoothGattCharacteristic.WRITE_TYPE_SIGNED -> BLEWriteTypes.TYPE_SIGNED
		else -> BLEWriteTypes.TYPE_UNKNOWN
	}