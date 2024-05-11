package com.eva.bluetoothterminalapp.domain.bluetooth_le.models

data class CharacteristicsReadValue(
	val characteristic: BLECharacteristicsModel? = null,
	val valueAsString: String? = null,
)