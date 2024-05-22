package com.eva.bluetoothterminalapp.presentation.feature_settings.util

import com.eva.bluetoothterminalapp.domain.settings.enums.BLEScanPeriodTimmings
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsScanMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsSupportedLayer

sealed interface BLESettingsEvent {

	data class OnScanPeriodChange(val timmings: BLEScanPeriodTimmings) : BLESettingsEvent

	data class OnScanModeChange(val mode: BLESettingsScanMode) : BLESettingsEvent

	data class OnPhyLayerChange(val layer: BLESettingsSupportedLayer) : BLESettingsEvent

	data class OnToggleIsLegacyAdvertisement(val isLegacy: Boolean) : BLESettingsEvent
}