package com.eva.bluetoothterminalapp.presentation.feature_le_connect.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel

private val BLEPropertyTypes.propertyRes: Int?
	get() = when (this) {
		BLEPropertyTypes.PROPERTY_BROADCAST -> R.string.ble_property_broadcast
		BLEPropertyTypes.PROPERTY_READ -> R.string.ble_property_read
		BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE -> R.string.ble_property_write_no_resp
		BLEPropertyTypes.PROPERTY_WRITE -> R.string.ble_property_write
		BLEPropertyTypes.PROPERTY_NOTIFY -> R.string.ble_property_notify
		BLEPropertyTypes.PROPERTY_INDICATE -> R.string.ble_property_indicate
		BLEPropertyTypes.PROPERTY_SIGNED_WRITE -> R.string.ble_property_signed_write
		BLEPropertyTypes.PROPERTY_EXTENDED_PROPS -> R.string.ble_property_extended
		BLEPropertyTypes.UNKNOWN -> null
	}


val BLECharacteristicsModel.toReadbleProperties: String
	@Composable
	get() = buildString {
		properties.forEachIndexed { idx, property ->
			if (idx != 0) append(",")
			property.propertyRes?.let { res -> append(stringResource(id = res)) }
		}
	}