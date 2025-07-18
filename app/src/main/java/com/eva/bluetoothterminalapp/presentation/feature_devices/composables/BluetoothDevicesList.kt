package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.composables.LocationPermissionCard
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BluetoothDevicesList(
	showLocationPlaceholder: Boolean,
	pairedDevices: ImmutableList<BluetoothDeviceModel>,
	availableDevices: ImmutableList<BluetoothDeviceModel>,
	onSelectDevice: (BluetoothDeviceModel) -> Unit,
	onLocationPermsAccept: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp)
) {

	val isPairedListEmpty by remember(pairedDevices) {
		derivedStateOf(pairedDevices::isEmpty)
	}

	val isLocalInspectionMode = LocalInspectionMode.current

	val devicesKey: ((Int, BluetoothDeviceModel) -> Any)? = remember {
		if (isLocalInspectionMode) null
		else { _, device -> device.address }
	}

	val devicesContentKey: ((Int, BluetoothDeviceModel) -> Any?) = remember {
		{ _, _ -> BluetoothDeviceModel::class.simpleName }
	}


	LazyColumn(
		modifier = modifier.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		contentPadding = contentPadding
	) {

		pairedDevicesHeader()

		if (isPairedListEmpty) {
			item {
				Text(
					text = stringResource(id = R.string.paired_device_not_found),
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.bodyLarge,
					modifier = Modifier
						.fillMaxWidth()
						.animateItem(),
				)
			}
		}

		itemsIndexed(
			items = pairedDevices,
			key = devicesKey,
			contentType = devicesContentKey,
		) { _, device ->
			BluetoothDeviceCard(
				device = device,
				onConnect = { onSelectDevice(device) },
				modifier = Modifier
					.fillMaxWidth()
					.animateItem()
			)
		}

		availableDevicesHeader()

		if (showLocationPlaceholder) {
			item {
				LocationPermissionCard(
					onLocationAccess = onLocationPermsAccept,
					modifier = Modifier
						.fillMaxWidth()
						.animateItem()
				)
			}
		}

		itemsIndexed(
			items = availableDevices,
			key = devicesKey,
			contentType = devicesContentKey
		) { _, device ->
			BluetoothDeviceCard(
				device = device,
				onConnect = { onSelectDevice(device) },
				modifier = Modifier
					.fillMaxWidth()
					.animateItem()
			)
		}
	}
}

private fun LazyListScope.pairedDevicesHeader() = stickyHeader {
	Column(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.surface)
			.padding(bottom = 8.dp)
			.fillMaxWidth(),
	) {
		Text(
			text = stringResource(id = R.string.paired_device),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Text(
			text = stringResource(id = R.string.paired_device_desc),
			style = MaterialTheme.typography.labelLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
	}
}

private fun LazyListScope.availableDevicesHeader() = stickyHeader {
	Column(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.surface)
			.padding(bottom = 8.dp)
			.fillMaxWidth(),
	) {
		Text(
			text = stringResource(id = R.string.available_scan_devices),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Text(
			text = stringResource(id = R.string.available_scan_devices_desc),
			style = MaterialTheme.typography.labelLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
	}
}
