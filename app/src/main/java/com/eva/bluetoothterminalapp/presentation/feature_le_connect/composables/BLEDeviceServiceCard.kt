package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEDeviceServiceCard(
	bleService: BLEServiceModel,
	modifier: Modifier = Modifier,
	selectedCharacteristic: BLECharacteristicsModel? = null,
	onCharacteristicSelect: (BLECharacteristicsModel) -> Unit = {},
	shape: Shape = MaterialTheme.shapes.large,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainer
) {
	val context = LocalContext.current
	val isInspectionMode = LocalInspectionMode.current

	var isSelected by remember { mutableStateOf(isInspectionMode) }

	val characteristicsSize = remember(bleService.characteristic) {
		"Characteristics: ${bleService.charisticsCount}"
	}

	val serviceTypeText = remember(bleService.serviceType) {
		val type = when (bleService.serviceType) {
			BLEServicesTypes.PRIMARY -> context.getString(R.string.ble_service_primary)
			BLEServicesTypes.SECONDARY -> context.getString(R.string.ble_service_secondary)
			else -> return@remember null
		}
		return@remember "Service : $type"
	}

	val serviceNameReadable = remember(bleService.probableName) {
		bleService.probableName ?: "Unknown Service"
	}

	Card(
		shape = shape,
		colors = CardDefaults.cardColors(containerColor = containerColor),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
		modifier = modifier
			.clip(MaterialTheme.shapes.large)
			.clickable { isSelected = !isSelected }
			.animateContentSize(),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(12.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp),
		) {

			Text(
				text = serviceNameReadable,
				color = MaterialTheme.colorScheme.onSurface,
				style = MaterialTheme.typography.titleMedium
			)
			Text(
				text = "UUID: ${bleService.serviceUUID}",
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
			serviceTypeText?.let { serviceType ->
				Text(
					text = serviceType,
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
			}

			Spacer(modifier = Modifier.height(4.dp))

			when (bleService.charisticsCount) {
				0 -> Text(
					text = stringResource(id = R.string.le_charactertics_not_present),
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				else -> AnimatedVisibility(
					visible = isSelected,
					enter = expandVertically() + fadeIn(),
					exit = shrinkVertically() + fadeOut(),
				) {
					Column(
						verticalArrangement = Arrangement.spacedBy(4.dp)
					) {
						Text(
							text = characteristicsSize,
							style = MaterialTheme.typography.titleSmall
						)
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
		PreviewFakes.FAKE_BLE_SERVICE
			.copy(characteristic = listOf(PreviewFakes.FAKE_BLE_CHARACTERISTIC_MODEL))
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