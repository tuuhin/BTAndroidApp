package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEWriteTypes
import java.util.UUID

data class BLECharacteristicsModel(
	val instanceId: Int,
	val uuid: UUID,
	val permission: BLEPermission,
	val properties: List<BLEPropertyTypes>,
	val writeType: BLEWriteTypes,
	val byteArray: ByteArray = byteArrayOf(),
	val descriptors: List<BLEDescriptorModel> = emptyList()
) : BLEValueModel(byteArray) {

	private var _probableName: String? = null

	var probableName: String?
		get() = _probableName
		set(value) {
			_probableName = value
		}
}
