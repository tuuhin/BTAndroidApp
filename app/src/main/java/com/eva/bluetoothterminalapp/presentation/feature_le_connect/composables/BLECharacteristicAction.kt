package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.mapper.canIndicate
import com.eva.bluetoothterminalapp.data.mapper.canNotify
import com.eva.bluetoothterminalapp.data.mapper.canRead
import com.eva.bluetoothterminalapp.data.mapper.canWrite
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BLECharacteristicsActions(
	characteristic: BLECharacteristicsModel,
	isDeviceConnected: Boolean,
	onRead: () -> Unit,
	onWrite: () -> Unit,
	onNotify: () -> Unit,
	onStopNotify: () -> Unit,
	onStopIndicate: () -> Unit,
	onIndicate: () -> Unit, modifier: Modifier = Modifier,
	chipBordersEnabled: Boolean = false,
	shape: Shape = MaterialTheme.shapes.large,
	chipColors: Color = MaterialTheme.colorScheme.primary,
) {

	val primaryColor = SuggestionChipDefaults.suggestionChipColors(
		containerColor = chipColors,
		labelColor = contentColorFor(backgroundColor = chipColors),
		disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
	)

	val secondaryColor = SuggestionChipDefaults.suggestionChipColors(
		containerColor = MaterialTheme.colorScheme.tertiaryContainer,
		labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
		disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
	)

	FlowRow(
		horizontalArrangement = Arrangement.spacedBy(6.dp),
		modifier = modifier.fillMaxWidth()
	) {
		if (characteristic.canRead) SuggestionChip(
			onClick = onRead,
			shape = shape,
			label = { Text(text = stringResource(id = R.string.ble_property_read)) },
			enabled = isDeviceConnected,
			colors = primaryColor,
			border = SuggestionChipDefaults
				.suggestionChipBorder(enabled = chipBordersEnabled)
		)

		if (characteristic.canWrite) SuggestionChip(
			onClick = onWrite,
			shape = shape,
			label = { Text(text = stringResource(id = R.string.ble_property_write)) },
			enabled = isDeviceConnected,
			colors = primaryColor,
			border = SuggestionChipDefaults
				.suggestionChipBorder(enabled = chipBordersEnabled)
		)

		if (characteristic.canNotify) Crossfade(
			targetState = characteristic.isNotificationRunning,
			label = "Is notify running for the characteristic"
		) { isRunning ->
			if (isRunning) SuggestionChip(
				shape = shape,
				onClick = onStopNotify,
				label = { Text(text = stringResource(R.string.ble_property_notify_stop)) },
				enabled = isDeviceConnected,
				colors = secondaryColor,
				border = SuggestionChipDefaults
					.suggestionChipBorder(enabled = chipBordersEnabled)
			)
			else SuggestionChip(
				shape = shape,
				onClick = onNotify,
				label = { Text(text = stringResource(R.string.ble_property_notify)) },
				enabled = isDeviceConnected,
				colors = primaryColor,
				border = SuggestionChipDefaults
					.suggestionChipBorder(enabled = chipBordersEnabled)
			)
		}
		if (characteristic.canIndicate) Crossfade(
			targetState = characteristic.isIndicationRunning,
			label = "Is Indications are running for the characteristic"
		) { isRunning ->
			if (isRunning) SuggestionChip(
				shape = shape,
				onClick = onStopIndicate,
				label = { Text(text = stringResource(R.string.ble_property_indicate_stop)) },
				enabled = isDeviceConnected,
				colors = secondaryColor,
				border = SuggestionChipDefaults
					.suggestionChipBorder(enabled = chipBordersEnabled)
			)
			else SuggestionChip(
				shape = shape,
				onClick = onIndicate,
				label = { Text(text = stringResource(R.string.ble_property_indicate)) },
				enabled = isDeviceConnected && !characteristic.isNotificationRunning,
				colors = primaryColor,
				border = SuggestionChipDefaults
					.suggestionChipBorder(enabled = chipBordersEnabled)
			)
		}
	}
}

private class BLECharacteristicsPropertyParams :
	CollectionPreviewParameterProvider<BLECharacteristicsModel>(
		listOf(
			PreviewFakes.FAKE_BLE_CHARACTERISTIC_MODEL_WITH_DATA
				.copy(isSetNotificationActive = true)
		)
	)

@PreviewLightDark
@Composable
private fun BLECharacteristicsPropertiesOptionsPreview(
	@PreviewParameter(BLECharacteristicsPropertyParams::class)
	characteristic: BLECharacteristicsModel
) = BlueToothTerminalAppTheme {
	BLECharacteristicsActions(
		characteristic = characteristic,
		isDeviceConnected = true,
		onRead = { },
		onWrite = { },
		onNotify = { },
		onStopNotify = { },
		onStopIndicate = { },
		onIndicate = { })
}