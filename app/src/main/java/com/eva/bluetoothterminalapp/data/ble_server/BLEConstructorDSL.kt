package com.eva.bluetoothterminalapp.data.ble_server

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes
import java.util.UUID

fun bleServiceOf(uuid: UUID, serviceType: BLEServicesTypes): BluetoothGattService =
	BluetoothGattService(
		uuid,
		when (serviceType) {
			BLEServicesTypes.PRIMARY -> BluetoothGattService.SERVICE_TYPE_PRIMARY
			BLEServicesTypes.SECONDARY -> BluetoothGattService.SERVICE_TYPE_SECONDARY
			BLEServicesTypes.UNKNOWN -> throw Exception("Unknown service type not allowed")
		}
	)

fun bleCharacteristicsOf(
	uuid: UUID,
	properties: List<BLEPropertyTypes>,
	permissions: List<BLEPermission>,
): BluetoothGattCharacteristic {

	require(properties.isNotEmpty() || properties.contains(BLEPropertyTypes.UNKNOWN)) {
		"Required at-least a single property and the use of property unknown cannot be used"
	}

	require(permissions.isNotEmpty() || permissions.contains(BLEPermission.PERMISSION_UNKNOWN)) {
		"Required at-least a single permission associated with the properties"
	}

	var gattProperty = 0
	var gattPermission = 0
	properties.filterNot { it == BLEPropertyTypes.UNKNOWN }.forEach { type ->
		gattProperty = when (type) {
			BLEPropertyTypes.PROPERTY_BROADCAST -> gattProperty or BluetoothGattCharacteristic.PROPERTY_BROADCAST
			BLEPropertyTypes.PROPERTY_READ -> gattProperty or BluetoothGattCharacteristic.PROPERTY_READ
			BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE -> gattProperty or BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
			BLEPropertyTypes.PROPERTY_WRITE -> gattProperty or BluetoothGattCharacteristic.PROPERTY_WRITE
			BLEPropertyTypes.PROPERTY_NOTIFY -> gattProperty or BluetoothGattCharacteristic.PROPERTY_NOTIFY
			BLEPropertyTypes.PROPERTY_INDICATE -> gattProperty or BluetoothGattCharacteristic.PROPERTY_INDICATE
			BLEPropertyTypes.PROPERTY_SIGNED_WRITE -> gattProperty or BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE
			BLEPropertyTypes.PROPERTY_EXTENDED_PROPS -> gattProperty or BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS
			BLEPropertyTypes.UNKNOWN -> gattProperty
		}
	}

	permissions.filterNot { it == BLEPermission.PERMISSION_UNKNOWN }.forEach { perms ->
		gattPermission = when (perms) {
			BLEPermission.PERMISSION_READ -> gattPermission or BluetoothGattCharacteristic.PERMISSION_READ
			BLEPermission.PERMISSION_READ_ENCRYPTED -> gattPermission or BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED
			BLEPermission.PERMISSION_READ_ENCRYPTED_MITM -> gattPermission or BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM
			BLEPermission.PERMISSION_WRITE -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE
			BLEPermission.PERMISSION_WRITE_ENCRYPTED -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED
			BLEPermission.PERMISSION_WRITE_ENCRYPTED_MITM -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM
			BLEPermission.PERMISSION_WRITE_SIGNED -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED
			BLEPermission.PERMISSION_WRITE_SIGNED_MITM -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED_MITM
			BLEPermission.PERMISSION_UNKNOWN -> gattPermission
		}
	}

	val writeProperties = listOf(
		BLEPropertyTypes.PROPERTY_WRITE, BLEPropertyTypes.PROPERTY_SIGNED_WRITE,
		BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE
	)

	return BluetoothGattCharacteristic(uuid, gattProperty, gattPermission).apply {
		if (properties.containsAll(writeProperties))
			writeType = when {
				BLEPropertyTypes.PROPERTY_WRITE in properties -> BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
				BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE in properties -> BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
				BLEPropertyTypes.PROPERTY_SIGNED_WRITE in properties -> BluetoothGattCharacteristic.WRITE_TYPE_SIGNED
				else -> BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
			}
	}
}

fun bleDescriptorOf(uuid: UUID, permissions: List<BLEPermission>): BluetoothGattDescriptor {

	require(permissions.isNotEmpty() || permissions.contains(BLEPermission.PERMISSION_UNKNOWN)) {
		"Required at-least a single permission associated with the properties"
	}

	var gattPermission = 0

	permissions.filterNot { it == BLEPermission.PERMISSION_UNKNOWN }.forEach { perms ->
		gattPermission = when (perms) {
			BLEPermission.PERMISSION_READ -> gattPermission or BluetoothGattCharacteristic.PERMISSION_READ
			BLEPermission.PERMISSION_READ_ENCRYPTED -> gattPermission or BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED
			BLEPermission.PERMISSION_READ_ENCRYPTED_MITM -> gattPermission or BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM
			BLEPermission.PERMISSION_WRITE -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE
			BLEPermission.PERMISSION_WRITE_ENCRYPTED -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED
			BLEPermission.PERMISSION_WRITE_ENCRYPTED_MITM -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM
			BLEPermission.PERMISSION_WRITE_SIGNED -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED
			BLEPermission.PERMISSION_WRITE_SIGNED_MITM -> gattPermission or BluetoothGattCharacteristic.PERMISSION_WRITE_SIGNED_MITM
			BLEPermission.PERMISSION_UNKNOWN -> gattPermission
		}
	}

	return BluetoothGattDescriptor(uuid, gattPermission)
}