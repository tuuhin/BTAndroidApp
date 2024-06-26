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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.toReadbleProperties
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
	onStopNotify: () -> Unit,
	onStopIndicate: () -> Unit,
	onIndicate: () -> Unit,
	modifier: Modifier = Modifier,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
	shape: Shape = MaterialTheme.shapes.large,
) {

	val context = LocalContext.current

	val probableName = remember(characteristic.probableName) {
		characteristic.probableName ?: context.getString(R.string.ble_characteristic_name_unknown)
	}

	val showStringValue = remember(characteristic.byteArray) {
		characteristic.valueAsString?.isNotBlank() == true
	}

	val showHexValue = remember(characteristic.byteArray) {
		characteristic.valueHexString.isNotBlank()
	}

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
				text = "UUID : ${characteristic.uuid}",
				style = MaterialTheme.typography.bodySmall,
			)
			Text(
				text = stringResource(
					R.string.ble_characteristic_properties,
					characteristic.toReadbleProperties
				),
				style = MaterialTheme.typography.bodySmall,
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
					text = stringResource(
						id = R.string.ble_value_hex,
						characteristic.valueHexString
					),
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
					text = stringResource(
						R.string.ble_descriptor_readble_value,
						characteristic.valueAsString ?: ""
					),
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
				)

			}
			BLECharacteristicsPropertiesOptions(
				characteristic = characteristic,
				isDeviceConnected = isDeviceConnected,
				onRead = onRead,
				onWrite = onWrite,
				onNotify = onNotify,
				onStopNotify = onStopNotify,
				onStopIndicate = onStopIndicate,
				onIndicate = onIndicate
			)
		}
	}
}

class BLECharacteristicsPreviewParams :
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
		onStopNotify = {},
		onIndicate = {},
		onStopIndicate = {},
		isDeviceConnected = true,
	)
}
