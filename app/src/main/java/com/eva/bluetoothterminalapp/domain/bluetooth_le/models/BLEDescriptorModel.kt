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

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as BLEDescriptorModel

		if (uuid != other.uuid) return false
		if (permissions != other.permissions) return false
		if (!byteArray.contentEquals(other.byteArray)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = uuid.hashCode()
		result = 31 * result + permissions.hashCode()
		result = 31 * result + byteArray.contentHashCode()
		return result
	}

}
