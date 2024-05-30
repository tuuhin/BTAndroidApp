package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.util.BLEValueModel
import kotlinx.collections.immutable.ImmutableList
import java.util.UUID

data class BLEDescriptorModel(
	val uuid: UUID,
	val permissions: ImmutableList<BLEPermission>,
	val byteArray: ByteArray = byteArrayOf()
) : BLEValueModel(byteArray) {

	private var _probableName: String? = null

	var probableName: String?
		get() = _probableName
		set(value) {
			_probableName = value
		}

}
