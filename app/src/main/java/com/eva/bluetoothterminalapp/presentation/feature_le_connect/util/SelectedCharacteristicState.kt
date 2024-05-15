package com.eva.bluetoothterminalapp.presentation.feature_le_connect.util

import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel

data class SelectedCharacteristicState(
	val service: BLEServiceModel? = null,
	val characteristic: BLECharacteristicsModel? = null,
) {
	val isSheetExpanded: Boolean
		get() = characteristic != null
}