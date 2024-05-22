package com.eva.bluetoothterminalapp.data.mapper

import com.eva.bluetoothterminalapp.data.datastore.BLEAppSettings
import com.eva.bluetoothterminalapp.data.datastore.ScanModes
import com.eva.bluetoothterminalapp.data.datastore.ScanTimmings
import com.eva.bluetoothterminalapp.data.datastore.SupportedLayers
import com.eva.bluetoothterminalapp.domain.settings.enums.BLEScanPeriodTimmings
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsScanMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsSupportedLayer
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel

fun BLEAppSettings.toModel(): BLESettingsModel = BLESettingsModel(
	scanPeriod = when (scanPeriod) {
		ScanTimmings.TWELVE_SECONDS -> BLEScanPeriodTimmings.TWELVE_SECONDS
		ScanTimmings.FOURTY_FIVE_SECONDS -> BLEScanPeriodTimmings.FOURTY_FIVE_SECONDS
		ScanTimmings.ONE_MINUTE -> BLEScanPeriodTimmings.ONE_MINUTE
		ScanTimmings.THREE_MINUTE -> BLEScanPeriodTimmings.THREE_MINUTE
		ScanTimmings.FIVE_MINUTES -> BLEScanPeriodTimmings.FIVE_MINUTES
		else -> BLEScanPeriodTimmings.TWELVE_SECONDS
	},
	isLegacyOnly = isAdvertisingExtension,
	supportedLayer = when (supportedLayer) {
		SupportedLayers.ALL -> BLESettingsSupportedLayer.ALL
		SupportedLayers.LEGACY -> BLESettingsSupportedLayer.LEGACY
		SupportedLayers.LONG_RANGE -> BLESettingsSupportedLayer.LONG_RANGE
		else -> BLESettingsSupportedLayer.ALL
	},
	scanMode = when (scanMode) {
		ScanModes.LOW_POWER -> BLESettingsScanMode.LOW_POWER
		ScanModes.BALANCED -> BLESettingsScanMode.BALANCED
		ScanModes.LOW_LATENCY -> BLESettingsScanMode.LOW_LATENCY
		else -> BLESettingsScanMode.BALANCED
	},
)

val BLEScanPeriodTimmings.toProto: ScanTimmings
	get() = when (this) {
		BLEScanPeriodTimmings.TWELVE_SECONDS -> ScanTimmings.TWELVE_SECONDS
		BLEScanPeriodTimmings.FOURTY_FIVE_SECONDS -> ScanTimmings.FOURTY_FIVE_SECONDS
		BLEScanPeriodTimmings.ONE_MINUTE -> ScanTimmings.ONE_MINUTE
		BLEScanPeriodTimmings.THREE_MINUTE -> ScanTimmings.THREE_MINUTE
		BLEScanPeriodTimmings.FIVE_MINUTES -> ScanTimmings.FIVE_MINUTES
	}

val BLESettingsScanMode.toProto: ScanModes
	get() = when (this) {
		BLESettingsScanMode.LOW_POWER -> ScanModes.LOW_POWER
		BLESettingsScanMode.BALANCED -> ScanModes.BALANCED
		BLESettingsScanMode.LOW_LATENCY -> ScanModes.LOW_LATENCY
	}

val BLESettingsSupportedLayer.toProto: SupportedLayers
	get() = when (this) {
		BLESettingsSupportedLayer.ALL -> SupportedLayers.ALL
		BLESettingsSupportedLayer.LEGACY -> SupportedLayers.LEGACY
		BLESettingsSupportedLayer.LONG_RANGE -> SupportedLayers.LONG_RANGE
	}