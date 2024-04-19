package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.presentation.composables.LocationPermissionBox
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BluetoothLeDeviceList(
	hasLocationPermission: Boolean,
	leDevices: ImmutableList<BluetoothLEDeviceModel>,
	onLocationPermissionChanged: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp)
) {
	Crossfade(
		targetState = hasLocationPermission,
		label = "Location Permission",
		animationSpec = tween(400),
	) { isGranted ->
		when {
			!isGranted -> LocationPermissionBox(
				onLocationPermsGranted = onLocationPermissionChanged,
				modifier = modifier.fillMaxSize()
			)

			else -> LazyColumn(
				modifier = modifier.fillMaxSize(),
				verticalArrangement = Arrangement.spacedBy(8.dp),
				contentPadding = contentPadding,
			) {
				stickyHeader {
					Column(
						modifier = Modifier
							.wrapContentHeight()
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
				itemsIndexed(
					items = leDevices,
					key = { _, leDevice -> leDevice.deviceModel.address },
					contentType = { _, device -> device.javaClass.simpleName },
				) { _, item ->
					BluetoothLEDeviceCard(
						leDeviceModel = item,
						modifier = Modifier
							.fillMaxWidth()
							.animateItemPlacement()
					)
				}
			}
		}
	}
}