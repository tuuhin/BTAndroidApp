package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.mapper.canIndicate
import com.eva.bluetoothterminalapp.data.mapper.canNotify
import com.eva.bluetoothterminalapp.data.mapper.canRead
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.propertyRes
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BLECharacteristicsCard(
	characteristic: BLECharacteristicsModel,
	isDeviceConnected: Boolean,
	onRead: () -> Unit,
	onWrite: () -> Unit,
	onNotify: () -> Unit,
	onIndicate: () -> Unit,
	modifier: Modifier = Modifier,
	chipBordersEnabled: Boolean = false,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
	shape: Shape = MaterialTheme.shapes.large,
	chipColors: Color = MaterialTheme.colorScheme.primary,
) {

	val context = LocalContext.current

	val probableName = remember(characteristic.probableName) {
		characteristic.probableName ?: context.getString(R.string.ble_characteristic_name_unknown)
	}


	val properites = remember(characteristic.properties) {
		buildString {
			append(" ")
			characteristic.properties.forEachIndexed { idx, property ->
				if (idx != 0) append(", ")
				val res = property.propertyRes ?: return@forEachIndexed
				append(context.getString(res))
			}
		}
	}

	val canWrite = remember(characteristic.properties) {
		characteristic.let { model ->
			model.properties.any { property ->
				property in arrayOf(
					BLEPropertyTypes.PROPERTY_WRITE,
					BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE,
					BLEPropertyTypes.PROPERTY_SIGNED_WRITE
				)
			}
		}
	}

	val canNotify = remember(characteristic.properties) {
		characteristic.canNotify
	}

	val canIndicate = remember(characteristic) {
		characteristic.canIndicate
	}

	val canRead = remember(characteristic.properties) {
		characteristic.canRead
	}

	val showStringValue = remember(characteristic.byteArray) {
		characteristic.valueAsString?.isNotBlank() == true
	}

	val showHexValue = remember(characteristic.byteArray) {
		characteristic.valueHexString.isNotBlank()
	}

	val suggestionChipColors = SuggestionChipDefaults.suggestionChipColors(
		containerColor = chipColors,
		labelColor = contentColorFor(backgroundColor = chipColors),
		disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
	)

	Card(
		elevation = CardDefaults.elevatedCardElevation(),
		colors = CardDefaults.elevatedCardColors(
			containerColor = containerColor,
			contentColor = contentColorFor(backgroundColor = containerColor)
		),
		shape = shape,
		modifier = modifier.animateContentSize(),
	) {
		Column(
			modifier = Modifier.padding(12.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {
			Text(
				text = probableName,
				style = MaterialTheme.typography.bodyLarge
			)
			Spacer(modifier = Modifier.height(2.dp))
			Text(
				text = buildAnnotatedString {
					withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
						append("UUID: ")
					}
					append("${characteristic.uuid}")
				},
				style = MaterialTheme.typography.bodySmall,
			)
			Text(
				text = buildAnnotatedString {
					withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
						append("Properties: ")
					}
					append(properites)
				},
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
			HorizontalDivider(
				modifier = Modifier.padding(vertical = 2.dp),
				color = MaterialTheme.colorScheme.outlineVariant
			)
			AnimatedVisibility(
				visible = showHexValue,
				enter = slideInVertically { height -> height } + fadeIn(),
				exit = slideOutVertically { height -> -height } + fadeOut()
			) {
				Text(
					text = buildAnnotatedString {
						withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
							append("Value As Hex: ")
						}
						append(characteristic.valueHexString)
					},
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
				)

			}
			AnimatedVisibility(
				visible = showStringValue,
				enter = slideInVertically { height -> height } + fadeIn(),
				exit = slideOutVertically { height -> -height } + fadeOut()
			) {
				Text(
					text = buildAnnotatedString {
						withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
							append("Value : ")
						}
						characteristic.valueAsString?.let(::append)
					},
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
				)

			}
			FlowRow(
				horizontalArrangement = Arrangement.spacedBy(6.dp),
				modifier = Modifier.fillMaxWidth()

			) {
				if (canRead) SuggestionChip(
					onClick = onRead,
					label = { Text(text = stringResource(id = R.string.ble_property_read)) },

					enabled = isDeviceConnected,
					colors = suggestionChipColors,
					border = SuggestionChipDefaults
						.suggestionChipBorder(enabled = chipBordersEnabled)
				)

				if (canWrite) SuggestionChip(
					onClick = onWrite,
					label = { Text(text = stringResource(id = R.string.ble_property_write)) },
					enabled = isDeviceConnected,
					colors = suggestionChipColors,
					border = SuggestionChipDefaults
						.suggestionChipBorder(enabled = chipBordersEnabled)
				)

				if (canNotify) SuggestionChip(
					onClick = onNotify,
					label = { Text(text = stringResource(id = R.string.ble_property_notify)) },
					enabled = isDeviceConnected,
					colors = suggestionChipColors,
					border = SuggestionChipDefaults
						.suggestionChipBorder(enabled = chipBordersEnabled)
				)
				if (canIndicate) SuggestionChip(
					onClick = onIndicate,
					label = { Text(text = stringResource(id = R.string.ble_property_indicate)) },
					enabled = isDeviceConnected,
					colors = suggestionChipColors,
					border = SuggestionChipDefaults
						.suggestionChipBorder(enabled = chipBordersEnabled)
				)
			}
		}
	}
}

private class BLECharacteristicsPreviewParams :
	CollectionPreviewParameterProvider<BLECharacteristicsModel>(
		listOf(
			PreviewFakes.FAKE_BLE_CHARACTERISTIC_MODEL,
			PreviewFakes.FAKE_BLE_CHARACTERISTIC_MODEL_WITH_DATA
		)
	)

@PreviewLightDark
@Composable
private fun BLECharacteristicCardPreview(
	@PreviewParameter(BLECharacteristicsPreviewParams::class)
	characteristic: BLECharacteristicsModel
) = BlueToothTerminalAppTheme {
	BLECharacteristicsCard(
		characteristic = characteristic,
		onRead = {},
		onWrite = { },
		onNotify = { },
		onIndicate = {},
		isDeviceConnected = true,
	)
}
