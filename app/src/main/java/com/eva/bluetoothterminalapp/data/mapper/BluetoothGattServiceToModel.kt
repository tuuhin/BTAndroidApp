package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.collections.immutable.toPersistentList

fun BluetoothGattService.toDomainModel(): BLEServiceModel = BLEServiceModel(
	serviceId = instanceId,
	serviceUUID = uuid,
	serviceType = bleServiceType,
).apply {
	this.characteristic = characteristics.map(BluetoothGattCharacteristic::toDomainModel)
		.toPersistentList()
}

fun BluetoothGattService.toDomainModel(
	probableName: String? = null,
	characteristic: List<BLECharacteristicsModel> = emptyList()
): BLEServiceModel = BLEServiceModel(
	serviceId = instanceId,
	serviceUUID = uuid,
	serviceType = bleServiceType,
).apply {
	this.characteristic = characteristic.toPersistentList()
	this.probableName = probableName
}

private val BluetoothGattService.bleServiceType: BLEServicesTypes
	get() = when (this.type) {
		BluetoothGattService.SERVICE_TYPE_PRIMARY -> BLEServicesTypes.PRIMARY
		BluetoothGattService.SERVICE_TYPE_SECONDARY -> BLEServicesTypes.SECONDARY
		else -> BLEServicesTypes.UNKNOWN
	}
