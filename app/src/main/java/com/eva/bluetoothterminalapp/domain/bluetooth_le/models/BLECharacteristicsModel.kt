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
	val properties: List<BLEPropertyTypes>,
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

}
