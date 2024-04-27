package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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

	Crossfade(
		targetState = hasLocationPermission,
		label = "Location Permission",
		animationSpec = tween(400),
	) { isGranted ->
		Box(
			modifier = modifier.fillMaxSize()
		) {
			when {
				!isGranted -> LocationPermissionBox(
					onLocationPermsGranted = onLocationPermissionChanged,
					modifier = Modifier.align(Alignment.Center)
				)

				else -> Column(
					modifier = Modifier.matchParentSize()
				) {
					BLEDevicesHeader()
					LazyColumn(
						verticalArrangement = Arrangement.spacedBy(8.dp),
						contentPadding = contentPadding,
						modifier = Modifier.weight(1f)
					) {
						itemsIndexed(
							items = leDevices,
							key = devicesKey,
							contentType = { _, device -> device.javaClass.simpleName },
						) { _, item ->
							BluetoothLEDeviceCard(
								leDeviceModel = item,
								onItemSelect = { onDeviceSelect(item) },
								modifier = Modifier
									.fillMaxWidth()
									.animateItemPlacement()
							)
						}
					}
				}
			}

		}
	}
}

@Composable
private fun BLEDevicesHeader(modifier: Modifier = Modifier) {
	ListItem(
		headlineContent = {
			Text(
				text = stringResource(id = R.string.bt_le_device),
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurface
			)
		},
		supportingContent = {
			Text(
				text = stringResource(id = R.string.bt_le_device_desc),
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		},
		colors = ListItemDefaults
			.colors(containerColor = MaterialTheme.colorScheme.surface),
		modifier = modifier,
	)
}