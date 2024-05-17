package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Rule
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLECharacteristicEvent
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEReadWriteSheetContent(
	characteristic: BLECharacteristicsModel?,
	onRead: () -> Unit,
	onWrite: () -> Unit,
	onIndicate: () -> Unit,
	onStopIndicate: () -> Unit,
	onNotify: () -> Unit,
	onStopNotify: () -> Unit,
	onDescriptorRead: (BLEDescriptorModel) -> Unit,
	modifier: Modifier = Modifier,
	isDeviceConnected: Boolean = true,
) {
	if (characteristic == null) {
		Column(
			modifier = modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Icon(
				imageVector = Icons.AutoMirrored.Outlined.Rule,
				contentDescription = stringResource(id = R.string.ble_characteristic_not_selected_title),
				modifier = Modifier.size(32.dp),
				tint = MaterialTheme.colorScheme.onPrimaryContainer
			)
			Spacer(modifier = Modifier.height(12.dp))
			Text(
				text = stringResource(id = R.string.ble_characteristic_not_selected_title),
				style = MaterialTheme.typography.titleLarge
			)
			Text(
				text = stringResource(id = R.string.ble_characteristic_not_selected_desc),
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
		return
	}

	val hasDescriptors = remember(characteristic.descriptors) {
		characteristic.descriptors.isNotEmpty()
	}

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Text(
			text = stringResource(id = R.string.ble_selected_characteristic),
			style = MaterialTheme.typography.titleLarge
		)
		Spacer(modifier = Modifier.height(12.dp))
		BLECharacteristicsCard(
			characteristic = characteristic,
			isDeviceConnected = isDeviceConnected,
			onRead = onRead,
			onWrite = onWrite,
			onNotify = onNotify,
			onIndicate = onIndicate,
			onStopIndicate = onStopIndicate,
			onStopNotify = onStopNotify,
			modifier = Modifier.fillMaxWidth(),
		)

		if (hasDescriptors) {
			Text(
				text = stringResource(id = R.string.ble_descriptor_title),
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.Normal,
				modifier = Modifier.padding(vertical = 2.dp)
			)
			characteristic.descriptors.forEach { desc ->
				BLEDescriptorCard(
					descriptor = desc,
					isDeviceConnected = isDeviceConnected,
					onRead = { onDescriptorRead(desc) },
					modifier = Modifier.fillMaxWidth(),
				)
			}
		}
	}
}


@Composable
fun BLEReadWriteContentSheet(
	characteristic: BLECharacteristicsModel?,
	onEvent: (BLECharacteristicEvent) -> Unit,
	modifier: Modifier = Modifier
) {
	BLEReadWriteSheetContent(
		characteristic = characteristic,
		onRead = { onEvent(BLECharacteristicEvent.ReadCharacteristic) },
		onWrite = { onEvent(BLECharacteristicEvent.WriteCharacteristic) },
		onIndicate = { onEvent(BLECharacteristicEvent.OnIndicateCharacteristic) },
		onNotify = { onEvent(BLECharacteristicEvent.OnNotifyCharacteristic) },
		onStopNotify = { onEvent(BLECharacteristicEvent.OnStopNotifyOrIndication) },
		onStopIndicate = { onEvent(BLECharacteristicEvent.OnStopNotifyOrIndication) },
		onDescriptorRead = { desc -> onEvent(BLECharacteristicEvent.OnDescriptorRead(desc)) },
		modifier = modifier
	)
}

private class CharacteristicModelPreviewParams :
	CollectionPreviewParameterProvider<BLECharacteristicsModel?>(
		listOf(
			PreviewFakes.FAKE_BLE_CHARACTERISTIC_MODEL,
			PreviewFakes.FAKE_BLE_CHARACTERISTIC_MODEL_WITH_DATA,
			null
		)
	)


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun BLEReadWriteSheetContentPreview(
	@PreviewParameter(CharacteristicModelPreviewParams::class)
	characteristic: BLECharacteristicsModel?
) = BlueToothTerminalAppTheme {
	Surface(
		color = MaterialTheme.colorScheme.surfaceContainer,
		shape = BottomSheetDefaults.ExpandedShape,
		tonalElevation = BottomSheetDefaults.Elevation
	) {
		BLEReadWriteSheetContent(
			characteristic = characteristic,
			onRead = {},
			onWrite = {},
			onIndicate = {},
			onNotify = {},
			onDescriptorRead = {},
			onStopIndicate = {},
			onStopNotify = {},
			modifier = Modifier
				.padding(vertical = 16.dp, horizontal = 20.dp)
				.fillMaxWidth()
		)
	}
}