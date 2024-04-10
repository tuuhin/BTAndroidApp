package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BluetoothDevicesList(
	pairedDevices: ImmutableList<BluetoothDeviceModel>,
	availableDevices: ImmutableList<BluetoothDeviceModel>,
	onSelectDevice: (BluetoothDeviceModel) -> Unit,
	modifier: Modifier = Modifier,
	locationPlaceholder: (@Composable LazyItemScope.() -> Unit)? = null,
	contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {

	LazyColumn(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(8.dp),
		contentPadding = contentPaddingValues
	) {

		stickyHeader {
			Column(
				modifier = Modifier
					.wrapContentHeight()
					.fillMaxWidth(),
			) {
				Text(
					text = stringResource(id = R.string.paired_device),
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = stringResource(id = R.string.paired_device_desc),
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
		if (pairedDevices.isEmpty()) {
			item {
				Text(
					text = stringResource(id = R.string.paired_device_not_found),
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center,
					style = MaterialTheme.typography.bodyLarge
				)
			}
		}
		itemsIndexed(
			items = pairedDevices,
			key = { _, device -> device.address },
			contentType = { _, device -> device.type }
		) { _, device ->
			BluetoothDeviceCard(
				device = device,
				onConnect = { onSelectDevice(device) },
				modifier = Modifier
					.fillMaxWidth()
					.animateItemPlacement()
			)
		}
		stickyHeader {
			Column(
				modifier = Modifier
					.wrapContentHeight()
					.fillMaxWidth(),
			) {
				Text(
					text = stringResource(id = R.string.available_scan_devices),
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = stringResource(id = R.string.available_scan_devices_desc),
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
		locationPlaceholder?.let { content ->
			item {
				content(this)
			}
		}
		itemsIndexed(
			items = availableDevices,
			key = { _, device -> device.address },
			contentType = { _, device -> device.type }
		) { _, device ->
			BluetoothDeviceCard(
				device = device,
				onConnect = { onSelectDevice(device) },
				modifier = Modifier
					.fillMaxWidth()
					.animateItemPlacement()
			)
		}
	}
}