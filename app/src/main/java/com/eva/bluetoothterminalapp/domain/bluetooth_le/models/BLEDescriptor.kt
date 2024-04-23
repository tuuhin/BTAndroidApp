package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import java.util.UUID

data class BLEDescriptor(
	val uuid: UUID,
	val permissions: BLEPermission,
)
