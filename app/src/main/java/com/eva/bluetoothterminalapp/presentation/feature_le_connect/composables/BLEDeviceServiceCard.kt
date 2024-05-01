package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEDeviceServiceCard(
	service: BLEServiceModel,
	modifier: Modifier = Modifier,
) {
	var isSelected by remember { mutableStateOf(false) }

	val cardColor by animateColorAsState(
		targetValue = if (isSelected)
			MaterialTheme.colorScheme.surfaceContainerHighest
		else MaterialTheme.colorScheme.surfaceContainerHigh,
		label = "Selected Card Container"
	)

	val elevation by animateDpAsState(
		targetValue = if (isSelected) 4.dp else 0.dp,
		label = "card elevation"
	)

	Card(
		shape = MaterialTheme.shapes.large,
		colors = CardDefaults.cardColors(containerColor = cardColor),
		elevation = CardDefaults.cardElevation(defaultElevation = elevation),
		modifier = modifier
			.clip(MaterialTheme.shapes.large)
			.clickable { isSelected = !isSelected }
			.animateContentSize(),
	) {
		Row(
			modifier = Modifier.padding(12.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Column {
				BLEServiceContent(service = service)
				AnimatedVisibility(visible = isSelected) {
					Column {
						service.characteristic.forEach {
							BLECharacteristicContent(characteristic = it)
						}
					}
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun BLEDeviceServiceCardPreview() = BlueToothTerminalAppTheme {
	BLEDeviceServiceCard(service = PreviewFakes.FAKE_BLE_SERVICE)
}