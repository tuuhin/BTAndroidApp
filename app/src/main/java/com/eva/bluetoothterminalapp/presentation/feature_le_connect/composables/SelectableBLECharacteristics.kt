package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.propertyRes
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun SelectableBLECharacteristics(
	isSelected: Boolean,
	characteristic: BLECharacteristicsModel,
	onSelect: () -> Unit,
	modifier: Modifier = Modifier,
	backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
	shape: Shape = MaterialTheme.shapes.medium,
) {

	val context = LocalContext.current

	val probableName = remember(characteristic.probableName) {
		characteristic.probableName ?: context.getString(R.string.ble_characteristic_name_unknown)
	}

	val properites = remember(characteristic.properties) {
		buildString {
			characteristic.properties.forEachIndexed { idx, property ->
				if (idx != 0) append(",")
				property.propertyRes?.let { res ->
					append(context.getString(res))
				}
			}
		}
	}

	Card(
		onClick = onSelect,
		colors = CardDefaults.cardColors(containerColor = backgroundColor),
		shape = shape,
		modifier = modifier
	) {
		Row(
			modifier = Modifier.padding(all = 4.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			RadioButton(
				selected = isSelected,
				onClick = onSelect,
				colors = RadioButtonDefaults
					.colors(selectedColor = MaterialTheme.colorScheme.secondary)
			)
			Column(
				modifier = Modifier.weight(1f),
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

				Text(
					text = buildAnnotatedString {
						with(MaterialTheme.typography.labelMedium) {
							withStyle(SpanStyle(color, fontSize, fontWeight, fontStyle)) {
								append("Propeties: ")
							}
						}
						append(properites)
					},
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}

		}
	}
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