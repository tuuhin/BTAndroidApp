package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEWriteTypes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

data class BLECharacteristicsModel(
	val characteristicsId: Int,
	val uuid: UUID,
	val permission: BLEPermission,
	val properties: List<BLEPropertyTypes>,
	val writeType: BLEWriteTypes,
) {

	private var _descriptors: List<BLEDescriptorModel> = emptyList()
	private var _probableName: String? = null


	var descriptors: ImmutableList<BLEDescriptorModel>
		get() = _descriptors.toPersistentList()
		set(value) {
			_descriptors = value
		}

	var probableName: String?
		get() = _probableName
		set(value) {
			_probableName = value
		}
}
