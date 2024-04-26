package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes
import java.util.UUID

data class BLEServiceModel(
	val serviceId: Int,
	val serviceUUID: UUID,
	val serviceType: BLEServicesTypes = BLEServicesTypes.UNKNOWN,
	val characteristic: List<BLECharacteristicsModel> = emptyList(),
	val bleServiceName: String? = null,
)