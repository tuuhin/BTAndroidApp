package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel

@Composable
fun BLECharacteristicContent(
	characteristic: BLECharacteristicsModel,
	modifier: Modifier = Modifier
) {
	Column(modifier = modifier) {
		Text(text = "UUID: ${characteristic.uuid}", style = MaterialTheme.typography.titleSmall)
		Text(text = "PROPERTY:${characteristic.property}")
		Text(text = "WRITE TYPES ${characteristic.writeType}")
		Text(text = "PERMISSIONS ${characteristic.permission}")

	}
}