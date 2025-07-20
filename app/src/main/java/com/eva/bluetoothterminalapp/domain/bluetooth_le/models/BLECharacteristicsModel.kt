package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEWriteTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.util.BLEValueModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data class BLECharacteristicsModel(
	val instanceId: Int,
	val uuid: UUID,
	val permission: BLEPermission,
	val properties: ImmutableList<BLEPropertyTypes>,
	val writeType: BLEWriteTypes,
	val descriptors: ImmutableList<BLEDescriptorModel> = persistentListOf(),
	val byteArray: ByteArray = byteArrayOf(),
	private val isSetNotificationActive: Boolean = false,
) : BLEValueModel(byteArray) {

	private var _probableName: String? = null

	var probableName: String?
		get() = _probableName
		set(value) {
			_probableName = value
		}

	val isIndicationRunning: Boolean
		get() = BLEPropertyTypes.PROPERTY_INDICATE in properties && isSetNotificationActive

	val isNotificationRunning: Boolean
		get() = BLEPropertyTypes.PROPERTY_NOTIFY in properties && isSetNotificationActive

	val isIndicateOrNotify: Boolean
		get() = properties.contains(BLEPropertyTypes.PROPERTY_NOTIFY) ||
				properties.contains(BLEPropertyTypes.PROPERTY_INDICATE)

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as BLECharacteristicsModel

		if (instanceId != other.instanceId) return false
		if (isSetNotificationActive != other.isSetNotificationActive) return false
		if (uuid != other.uuid) return false
		if (permission != other.permission) return false
		if (properties != other.properties) return false
		if (writeType != other.writeType) return false
		if (descriptors != other.descriptors) return false
		if (!byteArray.contentEquals(other.byteArray)) return false
		if (_probableName != other._probableName) return false
		if (isIndicationRunning != other.isIndicationRunning) return false
		if (isNotificationRunning != other.isNotificationRunning) return false
		if (isIndicateOrNotify != other.isIndicateOrNotify) return false
		if (probableName != other.probableName) return false

		return true
	}

	override fun hashCode(): Int {
		var result = instanceId
		result = 31 * result + isSetNotificationActive.hashCode()
		result = 31 * result + uuid.hashCode()
		result = 31 * result + permission.hashCode()
		result = 31 * result + properties.hashCode()
		result = 31 * result + writeType.hashCode()
		result = 31 * result + descriptors.hashCode()
		result = 31 * result + byteArray.contentHashCode()
		result = 31 * result + (_probableName?.hashCode() ?: 0)
		result = 31 * result + isIndicationRunning.hashCode()
		result = 31 * result + isNotificationRunning.hashCode()
		result = 31 * result + isIndicateOrNotify.hashCode()
		result = 31 * result + (probableName?.hashCode() ?: 0)
		return result
	}

}
