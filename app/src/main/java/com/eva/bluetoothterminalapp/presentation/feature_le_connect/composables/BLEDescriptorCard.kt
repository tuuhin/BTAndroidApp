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
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
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
	chipColors: Color = MaterialTheme.colorScheme.secondaryContainer,
) {

	val context = LocalContext.current

	val probableName = remember(descriptor.probableName) {
		descriptor.probableName ?: context.getString(R.string.ble_descriptor_unknown)
	}


	val showStringValue = remember(descriptor.byteArray) {
		descriptor.valueAsString != null
				&& descriptor.valueAsString?.isNotBlank() == true

	}

	val showHexValue = remember(descriptor.byteArray) {
		descriptor.valueHexString.isNotBlank()
	}

	Card(
		shape = shape,
		colors = CardDefaults.cardColors(containerColor = containerColor),
		elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
		modifier = modifier,
	) {
		Column(
			modifier = Modifier.padding(10.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {
			Text(
				text = probableName,
				style = MaterialTheme.typography.bodyMedium
			)
			Text(
				text = buildAnnotatedString {
					withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
						append("UUID: ")
					}
					append("${descriptor.uuid}")
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
						append(descriptor.valueHexString)
					},
					style = MaterialTheme.typography.labelMedium,
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
						descriptor.valueAsString?.let(::append)
					},
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
				)

			}
			SuggestionChip(
				onClick = onRead,
				label = { Text(text = stringResource(id = R.string.ble_property_read)) },
				enabled = isDeviceConnected,
				colors = SuggestionChipDefaults.suggestionChipColors(
					containerColor = chipColors,
					labelColor = contentColorFor(backgroundColor = chipColors),
					disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
				),
				border = SuggestionChipDefaults.suggestionChipBorder(enabled = true)
			)
		}
	}
}

private class BLEDescriptorPreviewParams : CollectionPreviewParameterProvider<BLEDescriptorModel>(
	listOf(
		PreviewFakes.FAKE_BLE_DESCRIPTOR_MODEL,
		PreviewFakes.FAKE_BLE_DESCRIPTOR_MODEL_WITH_VALUE
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