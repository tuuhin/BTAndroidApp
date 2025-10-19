package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.toReadableProperties
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun SelectableBLECharacteristics(
	isSelected: Boolean,
	characteristic: BLECharacteristicsModel,
	onSelect: () -> Unit,
	modifier: Modifier = Modifier,
	backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
	selectedBackGroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
	shape: Shape = MaterialTheme.shapes.medium,
) {

	val containerColor by animateColorAsState(
		targetValue = if (isSelected) selectedBackGroundColor else backgroundColor,
		label = "Service Card Background color"
	)

	Card(
		onClick = onSelect,
		colors = CardDefaults.cardColors(
			containerColor = containerColor,
			contentColor = contentColorFor(containerColor)
		),
		shape = shape,
		modifier = modifier
	) {
		Column(
			modifier = Modifier.padding(all = 12.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {
			Text(
				text = characteristic.probableName
					?: stringResource(R.string.ble_characteristic_name_unknown),
				style = MaterialTheme.typography.labelLarge,
			)
			Text(
				text = buildAnnotatedString {
					append("ID: ")
					withStyle(
						SpanStyle(
							fontFamily = FontFamily.Monospace,
							fontWeight = FontWeight.Medium,
							color = MaterialTheme.colorScheme.onSurfaceVariant,
						)
					) {
						append("${characteristic.uuid}")
					}
				},
				style = MaterialTheme.typography.labelMedium,
				overflow = TextOverflow.Ellipsis,
				maxLines = 1
			)

			Text(
				text = buildAnnotatedString {
					append("Properties: ")
					append(characteristic.toReadableProperties)
				},
				style = MaterialTheme.typography.labelMedium,
			)
		}

	}
}

private class BooleanPreviewParams :
	CollectionPreviewParameterProvider<Boolean>(listOf(true, false))


@PreviewLightDark
@Composable
private fun SelectableBLECharacteristicsPreview(
	@PreviewParameter(BooleanPreviewParams::class)
	isSelected: Boolean
) = BlueToothTerminalAppTheme {
	SelectableBLECharacteristics(
		isSelected = isSelected,
		characteristic = PreviewFakes.FAKE_BLE_CHARACTERISTIC_MODEL,
		onSelect = { },
	)
}