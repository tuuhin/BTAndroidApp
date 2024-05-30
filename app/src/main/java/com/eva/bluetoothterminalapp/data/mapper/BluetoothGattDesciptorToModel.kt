package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.BluetoothGattDescriptor
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.util.BLEDescriptorValue
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

fun BluetoothGattDescriptor.toModel(probableName: String? = null): BLEDescriptorModel =
	BLEDescriptorModel(
		uuid = uuid,
		permissions = gattPermissions,
	).apply {
		this.probableName = probableName
	}

val BLEDescriptorModel.descriptorValue: BLEDescriptorValue
	get() = when {
		byteArray.contentEquals(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE) -> BLEDescriptorValue.EnableIndication
		byteArray.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) -> BLEDescriptorValue.EnableNotifcation
		byteArray.contentEquals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE) -> BLEDescriptorValue.DisableNotifcationOrIndication
		else -> BLEDescriptorValue.ReadableValue(valueAsString)
	}


private val BluetoothGattDescriptor.gattPermissions: PersistentList<BLEPermission>
	get() {
		val permissions = buildList {
			if (permissions and BluetoothGattDescriptor.PERMISSION_READ != 0)
				add(BLEPermission.PERMISSION_READ)
			if (permissions and BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED != 0)
				add(BLEPermission.PERMISSION_READ_ENCRYPTED)
			if (permissions and BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM != 0)
				add(BLEPermission.PERMISSION_READ_ENCRYPTED_MITM)
			if (permissions and BluetoothGattDescriptor.PERMISSION_WRITE != 0)
				add(BLEPermission.PERMISSION_WRITE)
			if (permissions and BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED != 0)
				add(BLEPermission.PERMISSION_WRITE_ENCRYPTED)
			if (permissions and BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED != 0)
				add(BLEPermission.PERMISSION_WRITE_SIGNED)
			if (permissions and BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM != 0)
				add(BLEPermission.PERMISSION_WRITE_SIGNED_MITM)
			if (permissions and BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM != 0)
				add(BLEPermission.PERMISSION_WRITE_ENCRYPTED_MITM)
		}
		return persistentListOf<BLEPermission>()
			.addAll(permissions)
	}