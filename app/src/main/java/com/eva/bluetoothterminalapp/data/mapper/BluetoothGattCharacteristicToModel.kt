package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEWriteTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

fun BluetoothGattCharacteristic.toDomainModel(probableName: String? = null): BLECharacteristicsModel =
	BLECharacteristicsModel(
		instanceId = instanceId,
		uuid = uuid,
		permission = permission,
		properties = bleProperties,
		writeType = bleWriteType,
		descriptors = descriptors.map(BluetoothGattDescriptor::toModel).toPersistentList()
	).apply {
		this.probableName = probableName
	}

fun BluetoothGattCharacteristic.toDomainModel(
	probableName: String? = null,
	descriptors: List<BLEDescriptorModel> = emptyList()
): BLECharacteristicsModel = BLECharacteristicsModel(
	instanceId = instanceId,
	uuid = uuid,
	permission = permission,
	properties = bleProperties,
	writeType = bleWriteType,
	descriptors = descriptors.toPersistentList()
).apply {
	this.probableName = probableName
}

/**
 * Characteristic contains properties of indicate
 * @see BLEPropertyTypes
 */
val BLECharacteristicsModel.canIndicate: Boolean
	get() = BLEPropertyTypes.PROPERTY_INDICATE in properties

/**
 * Characteristic contains properties of notify
 * @see BLEPropertyTypes
 */
val BLECharacteristicsModel.canNotify: Boolean
	get() = BLEPropertyTypes.PROPERTY_NOTIFY in properties

/**
 * Characteristic contains properties of read
 * @see BLEPropertyTypes
 */
val BLECharacteristicsModel.canRead: Boolean
	get() = BLEPropertyTypes.PROPERTY_READ in properties

/**
 * Characteristic contains properties of write and write no response
 * @see BLEPropertyTypes
 */
val BLECharacteristicsModel.canWrite: Boolean
	get() = properties.any { property ->
		property in arrayOf(
			BLEPropertyTypes.PROPERTY_WRITE,
			BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE,
		) && writeType in arrayOf(BLEWriteTypes.TYPE_NO_RESPONSE, BLEWriteTypes.TYPE_DEFAULT)
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

private val BluetoothGattCharacteristic.bleProperties: ImmutableList<BLEPropertyTypes>
	get() {
		val properties = buildList {
			if (properties and BluetoothGattCharacteristic.PROPERTY_BROADCAST != 0)
				add(BLEPropertyTypes.PROPERTY_BROADCAST)
			if (properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0)
				add(BLEPropertyTypes.PROPERTY_INDICATE)
			if (properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0)
				add(BLEPropertyTypes.PROPERTY_NOTIFY)
			if (properties and BluetoothGattCharacteristic.PROPERTY_READ != 0)
				add(BLEPropertyTypes.PROPERTY_READ)
			if (properties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0)
				add(BLEPropertyTypes.PROPERTY_WRITE)
			if (properties and BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS != 0)
				add(BLEPropertyTypes.PROPERTY_EXTENDED_PROPS)
			if (properties and BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE != 0)
				add(BLEPropertyTypes.PROPERTY_SIGNED_WRITE)
			if (properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0)
				add(BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE)
		}
		return persistentListOf<BLEPropertyTypes>().addAll(properties)
	}


private val BluetoothGattCharacteristic.bleWriteType: BLEWriteTypes
	get() = when (writeType) {
		BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT -> BLEWriteTypes.TYPE_DEFAULT
		BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE -> BLEWriteTypes.TYPE_NO_RESPONSE
		BluetoothGattCharacteristic.WRITE_TYPE_SIGNED -> BLEWriteTypes.TYPE_SIGNED
		else -> BLEWriteTypes.TYPE_UNKNOWN
	}