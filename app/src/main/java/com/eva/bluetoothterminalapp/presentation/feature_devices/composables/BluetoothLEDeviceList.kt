package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.presentation.composables.LocationPermissionBox
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
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

	Crossfade(
		targetState = hasLocationPermission,
		label = "Location Permission",
		modifier = modifier,
	) { isGranted ->
		if (isGranted) LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			contentPadding = contentPadding,
		) {
			BLEDevicesHeader()
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
		} else Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			LocationPermissionBox(
				onLocationPermsGranted = onLocationPermissionChanged
			)
		}
	}
}


@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.BLEDevicesHeader() = stickyHeader {
	Column(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.surface)
			.padding(bottom = 8.dp)
			.fillMaxWidth(),
	) {
		Text(
			text = stringResource(id = R.string.bt_le_device),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Text(
			text = stringResource(id = R.string.bt_le_device_desc),
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
	}
}