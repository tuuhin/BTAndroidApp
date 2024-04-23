package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEWriteTypes
import java.util.UUID

data class BLECharacteristicsModel(
	val characteristicsId: Int,
	val uuid: UUID,
	val permission: BLEPermission,
	val property: BLEPropertyTypes,
	val writeType: BLEWriteTypes,
	val descriptors: List<BLEDescriptor> = emptyList(),
)
