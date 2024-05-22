package com.eva.bluetoothterminalapp.domain.settings.models

import com.eva.bluetoothterminalapp.domain.settings.enums.BLEScanPeriodTimmings
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsScanMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsSupportedLayer

data class BLESettingsModel(
	val scanPeriod: BLEScanPeriodTimmings = BLEScanPeriodTimmings.FIVE_MINUTES,
	val supportedLayer: BLESettingsSupportedLayer = BLESettingsSupportedLayer.ALL,
	val scanMode: BLESettingsScanMode = BLESettingsScanMode.BALANCED,
	val isLegacyOnly: Boolean = true
)
