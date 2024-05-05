package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun SelectableBLECharacteristics(
	isSelected: Boolean,
	characteristic: BLECharacteristicsModel,
	onSelect: () -> Unit,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
	shape: Shape = MaterialTheme.shapes.medium,
) {

	val context = LocalContext.current

	val probableName = remember {
		characteristic.probableName ?: context.getString(R.string.ble_characteristic_name_unknown)
	}

	Row(
		modifier = modifier
			.clip(shape)
			.clickable(onClick = onSelect)
			.background(color = color, shape = shape),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		RadioButton(
			selected = isSelected,
			onClick = onSelect,
			colors = RadioButtonDefaults
				.colors(selectedColor = MaterialTheme.colorScheme.secondary)
		)
		Column(
			modifier = Modifier
				.padding(vertical = 4.dp)
				.weight(1f),
		) {
			Text(
				text = probableName,
				style = MaterialTheme.typography.labelLarge,
				color = MaterialTheme.colorScheme.onSurface
			)
			Text(
				text = "${characteristic.uuid}",
				style = MaterialTheme.typography.labelMedium,
				color = MaterialTheme.colorScheme.onSurface,
				overflow = TextOverflow.Ellipsis,
				maxLines = 1
			)
			characteristic.propertyRes?.let { property ->
				Text(
					text = property,
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}


private val BLECharacteristicsModel.propertyRes
	@Composable
	get() = when (property) {
		BLEPropertyTypes.PROPERTY_BROADCAST -> stringResource(R.string.ble_property_broadcast)
		BLEPropertyTypes.PROPERTY_READ -> stringResource(R.string.ble_property_read)
		BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE -> stringResource(R.string.ble_property_write_no_resp)
		BLEPropertyTypes.PROPERTY_WRITE -> stringResource(R.string.ble_property_write)
		BLEPropertyTypes.PROPERTY_NOTIFY -> stringResource(R.string.ble_property_notify)
		BLEPropertyTypes.PROPERTY_INDICATE -> stringResource(R.string.ble_property_indicate)
		BLEPropertyTypes.PROPERTY_SIGNED_WRITE -> stringResource(R.string.ble_property_signed_write)
		BLEPropertyTypes.PROPERTY_EXTENDED_PROPS -> stringResource(R.string.ble_property_extended)
		BLEPropertyTypes.UNKNOWN -> null
	}

@PreviewLightDark
@Composable
private fun SelectableBLECharacteristicsPreview() = BlueToothTerminalAppTheme {
	SelectableBLECharacteristics(
		isSelected = true,
		characteristic = PreviewFakes.FAKE_BLE_CHARACTERISTIC_MODEL,
		onSelect = { },
	)
}