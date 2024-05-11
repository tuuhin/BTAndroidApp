package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

data class BLEServiceModel(
	val serviceId: Int,
	val serviceUUID: UUID,
	val serviceType: BLEServicesTypes = BLEServicesTypes.UNKNOWN,
) {

	private var _characteristic: List<BLECharacteristicsModel> = emptyList()
	private var _probableName: String? = null

	var characteristic: PersistentList<BLECharacteristicsModel>
		get() = _characteristic.toPersistentList()
		set(value) {
			_characteristic = value
		}

	var probableName: String?
		get() = _probableName
		set(value) {
			_probableName = value
		}


	val charisticsCount: Int
		get() = _characteristic.size
}