package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.mapper.descriptorValue
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.util.BLEDescriptorValue
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEDescriptorCard(
	descriptor: BLEDescriptorModel,
	onRead: () -> Unit,
	isDeviceConnected: Boolean,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
	titleColor: Color = MaterialTheme.colorScheme.secondary,
	valuesColor: Color = MaterialTheme.colorScheme.tertiary,
	buttonColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {

	val showHexValue = remember(descriptor.byteArray) { descriptor.valueHexString.isNotBlank() }

	Card(
		shape = shape,
		colors = CardDefaults.cardColors(
			containerColor = containerColor,
			contentColor = contentColorFor(backgroundColor = containerColor)
		),
		elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
		modifier = modifier,
	) {
		Column(
			modifier = Modifier.padding(12.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {
			Text(
				text = descriptor.probableName ?: stringResource(R.string.ble_descriptor_unknown),
				style = MaterialTheme.typography.bodyMedium,
				fontWeight = FontWeight.Medium,
				color = titleColor,
			)
			Text(
				text = buildAnnotatedString {
					append("ID: ")
					withStyle(
						SpanStyle(
							fontFamily = FontFamily.Monospace,
							fontWeight = FontWeight.Medium
						)
					) {
						append("${descriptor.uuid}")
					}
				},
				style = MaterialTheme.typography.labelMedium,
				color = titleColor
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
					text = stringResource(id = R.string.ble_value_hex, descriptor.valueHexString),
					style = MaterialTheme.typography.labelMedium,
					color = valuesColor,
				)

			}
			AnimatedVisibility(
				visible = descriptor.textValue.isNotBlank(),
				enter = slideInVertically { height -> height } + fadeIn(),
				exit = slideOutVertically { height -> -height } + fadeOut()
			) {
				Text(
					text = buildAnnotatedString {
						append(stringResource(R.string.ble_readable_value))
						append(" :")
						append(descriptor.textValue)
					},
					style = MaterialTheme.typography.labelMedium,
					color = valuesColor,
				)
			}
			SuggestionChip(
				onClick = onRead,
				label = {
					Text(
						text = stringResource(id = R.string.ble_property_read),
						style = MaterialTheme.typography.labelMedium
					)
				},
				enabled = isDeviceConnected,
				colors = SuggestionChipDefaults.suggestionChipColors(
					containerColor = buttonColor,
					labelColor = contentColorFor(backgroundColor = buttonColor),
					disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
				),
				border = SuggestionChipDefaults.suggestionChipBorder(enabled = true),
				shape = MaterialTheme.shapes.large
			)
		}
	}
}

private val BLEDescriptorModel.textValue: String
	@Composable
	get() = when (descriptorValue) {
		BLEDescriptorValue.DisableNotifyOrIndication -> stringResource(id = R.string.ble_disable_notification)
		BLEDescriptorValue.EnableIndication -> stringResource(id = R.string.ble_enable_indication)
		BLEDescriptorValue.EnableNotification -> stringResource(id = R.string.ble_enable_notification)
		is BLEDescriptorValue.ReadableValue -> valueAsString ?: ""

	}

private class BLEDescriptorPreviewParams : CollectionPreviewParameterProvider<BLEDescriptorModel>(
	listOf(
		PreviewFakes.FAKE_BLE_DESCRIPTOR_MODEL,
		PreviewFakes.FAKE_BLE_DESCRIPTOR_MODEL_WITH_VALUE,
		PreviewFakes.FAKE_BLE_DESCRIPTOR_WITH_ENABLE_NOTIFICATION_VALUE
	)
)

@PreviewLightDark
@Composable
private fun BLEDescriptorCardPreview(
	@PreviewParameter(BLEDescriptorPreviewParams::class)
	descriptor: BLEDescriptorModel
) = BlueToothTerminalAppTheme {
	BLEDescriptorCard(
		descriptor = descriptor,
		onRead = {},
		isDeviceConnected = true,
	)
}