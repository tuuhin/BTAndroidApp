package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEWriteTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import kotlinx.collections.immutable.toPersistentList

fun BluetoothGattCharacteristic.toDomainModel(probableName: String? = null): BLECharacteristicsModel =
	BLECharacteristicsModel(
		characteristicsId = instanceId,
		uuid = uuid,
		permission = permission,
		properties = bleProperties,
		writeType = bleWriteType,
	).apply {
		this.probableName = probableName
		this.descriptors = getDescriptors().map(BluetoothGattDescriptor::toModel).toPersistentList()
	}

fun BluetoothGattCharacteristic.toDomainModel(
	probableName: String? = null,
	descriptors: List<BLEDescriptorModel> = emptyList()
): BLECharacteristicsModel = BLECharacteristicsModel(
	characteristicsId = instanceId,
	uuid = uuid,
	permission = permission,
	properties = bleProperties,
	writeType = bleWriteType,
).apply {
	this.probableName = probableName
	this.descriptors = descriptors.toPersistentList()
}


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

private val BluetoothGattCharacteristic.bleProperties: List<BLEPropertyTypes>
	get() = buildList<BLEPropertyTypes> {
		if (properties and BluetoothGattCharacteristic.PROPERTY_BROADCAST != 0) add(BLEPropertyTypes.PROPERTY_BROADCAST)
		if (properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) add(BLEPropertyTypes.PROPERTY_INDICATE)
		if (properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) add(BLEPropertyTypes.PROPERTY_INDICATE)
		if (properties and BluetoothGattCharacteristic.PROPERTY_READ != 0) add(BLEPropertyTypes.PROPERTY_READ)
		if (properties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) add(BLEPropertyTypes.PROPERTY_WRITE)
		if (properties and BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS != 0) add(
			BLEPropertyTypes.PROPERTY_EXTENDED_PROPS
		)
		if (properties and BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE != 0) add(
			BLEPropertyTypes.PROPERTY_SIGNED_WRITE
		)
		if (properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0) add(
			BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE
		)
	}


private val BluetoothGattCharacteristic.bleWriteType: BLEWriteTypes
	get() = when (writeType) {
		BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT -> BLEWriteTypes.TYPE_DEFAULT
		BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE -> BLEWriteTypes.TYPE_NO_RESPONSE
		BluetoothGattCharacteristic.WRITE_TYPE_SIGNED -> BLEWriteTypes.TYPE_SIGNED
		else -> BLEWriteTypes.TYPE_UNKNOWN
	}