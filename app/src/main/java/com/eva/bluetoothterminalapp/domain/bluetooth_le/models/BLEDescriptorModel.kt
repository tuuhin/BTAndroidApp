package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import java.util.UUID

data class BLEDescriptorModel(
	val uuid: UUID,
	val permissions: List<BLEPermission>,
) {
	private var _probableName: String? = null

	var probableName: String?
		get() = _probableName
		set(value) {
			_probableName = value
		}
}
