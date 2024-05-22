package com.eva.bluetoothterminalapp.domain.settings.repository

import com.eva.bluetoothterminalapp.domain.settings.enums.BLEScanPeriodTimmings
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsScanMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsSupportedLayer
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel
import kotlinx.coroutines.flow.Flow

interface BLESettingsDataStore {

	val settingsFlow: Flow<BLESettingsModel>

	val settings: BLESettingsModel

	suspend fun onUpdateScanPeriod(timming: BLEScanPeriodTimmings)

	suspend fun onIsAdvertiseExtensionChanged(isAdvertiseExtensionsOnly: Boolean)

	suspend fun onUpdateScanMode(scanMode: BLESettingsScanMode)

	suspend fun onUpdateSupportedLayer(layer: BLESettingsSupportedLayer)
}