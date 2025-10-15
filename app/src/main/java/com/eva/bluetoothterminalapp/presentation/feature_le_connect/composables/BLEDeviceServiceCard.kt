package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalInspectionMode
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
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.serviceTypeText
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEDeviceServiceCard(
	bleService: BLEServiceModel,
	modifier: Modifier = Modifier,
	selectedCharacteristic: BLECharacteristicsModel? = null,
	onCharacteristicSelect: (BLECharacteristicsModel) -> Unit = {},
	shape: Shape = MaterialTheme.shapes.medium,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainer
) {
	val isInspectionMode = LocalInspectionMode.current
	var isSelected by remember { mutableStateOf(isInspectionMode) }

	Card(
		onClick = { isSelected = !isSelected },
		shape = shape,
		colors = CardDefaults.cardColors(
			containerColor = containerColor,
			contentColor = contentColorFor(containerColor)
		),
		elevation = CardDefaults.cardElevation(),
		modifier = modifier,
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(12.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp),
		) {
			Text(
				text = bleService.probableName ?: stringResource(R.string.ble_service_unknown),
				color = MaterialTheme.colorScheme.primary,
				style = MaterialTheme.typography.titleMedium
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
						append("${bleService.serviceUUID}")
					}
				},
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.secondary,
				maxLines = 2,
				overflow = TextOverflow.Clip
			)
			Text(
				text = "Type : ${bleService.serviceTypeText}",
				style = MaterialTheme.typography.labelLarge,
			)

			Spacer(modifier = Modifier.height(4.dp))

			when (bleService.characteristicsCount) {
				0 -> Text(
					text = stringResource(id = R.string.le_characteristics_not_present),
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.tertiary
				)

				else -> AnimatedVisibility(
					visible = isSelected,
					enter = expandVertically() + fadeIn(),
					exit = shrinkVertically() + fadeOut(),
				) {
					Column(
						verticalArrangement = Arrangement.spacedBy(4.dp)
					) {
						Row(
							horizontalArrangement = Arrangement.spacedBy(4.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(
								text = "Characteristics",
								style = MaterialTheme.typography.labelLarge,
								fontWeight = FontWeight.SemiBold,
								color = MaterialTheme.colorScheme.tertiary
							)
							Surface(
								color = MaterialTheme.colorScheme.tertiaryContainer,
								contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
								shape = MaterialTheme.shapes.small,
							) {
								Text(
									text = "${bleService.characteristicsCount}",
									style = MaterialTheme.typography.labelLarge,
									fontWeight = FontWeight.SemiBold,
									modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
								)
							}
						}
						bleService.characteristic.forEach { characteristic ->
							SelectableBLECharacteristics(
								characteristic = characteristic,
								isSelected = characteristic == selectedCharacteristic,
								onSelect = { onCharacteristicSelect(characteristic) },
								modifier = Modifier.fillMaxWidth()
							)
						}
					}
				}
			}
		}
	}
}

class BLEServicesPreviewParams : CollectionPreviewParameterProvider<BLEServiceModel>(
	listOf(
		PreviewFakes.FAKE_BLE_SERVICE,
		PreviewFakes.FAKE_SERVICE_WITH_CHARACTERISTICS
	)
)

@PreviewLightDark
@Composable
private fun BLEDeviceServiceCardPreview(
	@PreviewParameter(BLEServicesPreviewParams::class)
	service: BLEServiceModel,
) = BlueToothTerminalAppTheme {
	BLEDeviceServiceCard(bleService = service)
}