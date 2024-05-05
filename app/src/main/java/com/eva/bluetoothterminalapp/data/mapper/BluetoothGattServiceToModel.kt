package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel

fun BluetoothGattService.toModel(): BLEServiceModel = BLEServiceModel(
	serviceId = instanceId,
	serviceUUID = uuid,
	serviceType = bleServiceType,
	characteristic = characteristics.map(BluetoothGattCharacteristic::toModel)
)

fun BluetoothGattService.toModel(
	sampleName: String? = null,
	characteristic: List<BLECharacteristicsModel>? = null
): BLEServiceModel = BLEServiceModel(
	serviceId = instanceId,
	serviceUUID = uuid,
	serviceType = bleServiceType,
	characteristic = characteristic ?: characteristics.map(BluetoothGattCharacteristic::toModel),
	probableName = sampleName
)

private val BluetoothGattService.bleServiceType: BLEServicesTypes
	get() = when (this.type) {
		BluetoothGattService.SERVICE_TYPE_PRIMARY -> BLEServicesTypes.PRIMARY
		BluetoothGattService.SERVICE_TYPE_SECONDARY -> BLEServicesTypes.SECONDARY
		else -> BLEServicesTypes.UNKNOWN
	}
