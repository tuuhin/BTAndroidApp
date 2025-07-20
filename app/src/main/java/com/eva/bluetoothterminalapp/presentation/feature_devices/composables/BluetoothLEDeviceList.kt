package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.presentation.composables.LocationPermissionBox
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BluetoothLeDeviceList(
	hasLocationPermission: Boolean,
	leDevices: ImmutableList<BluetoothLEDeviceModel>,
	onLocationPermissionChanged: (Boolean) -> Unit,
	onDeviceSelect: (BluetoothLEDeviceModel) -> Unit,
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp)
) {
	val isLocalInspectionMode = LocalInspectionMode.current

	val devicesKey: ((Int, BluetoothLEDeviceModel) -> Any)? = remember {
		if (isLocalInspectionMode) null
		else { _, device -> device.deviceModel.address }
	}

	val devicesContentKey: ((Int, BluetoothLEDeviceModel) -> Any?) = remember {
		{ _, _ -> BluetoothLEDeviceModel::class.simpleName }
	}

	LazyColumn(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		contentPadding = contentPadding,
		modifier = modifier
	) {
		if (hasLocationPermission) {
			bleDevicesHeader()
			itemsIndexed(
				items = leDevices,
				key = devicesKey,
				contentType = devicesContentKey,
			) { _, item ->
				BluetoothLEDeviceCard(
					leDeviceModel = item,
					onItemSelect = { onDeviceSelect(item) },
					modifier = Modifier
						.fillMaxWidth()
						.animateItem()
				)
			}
		} else {
			item {
				Box(
					modifier = Modifier
						.fillParentMaxSize()
						.animateItem(),
					contentAlignment = Alignment.Center
				) {
					LocationPermissionBox(
						onLocationPermsGranted = onLocationPermissionChanged,
						modifier = Modifier.sizeIn(maxWidth = dimensionResource(R.dimen.permission_box_min_size))
					)
				}
			}
		}
	}
}

private fun LazyListScope.bleDevicesHeader() = stickyHeader {
	Column(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.surface)
			.padding(bottom = 8.dp)
			.fillMaxWidth()
			.animateItem(),
	) {
		Text(
			text = stringResource(id = R.string.bt_le_device),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Text(
			text = stringResource(id = R.string.bt_le_device_desc),
			style = MaterialTheme.typography.labelLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
	}
}