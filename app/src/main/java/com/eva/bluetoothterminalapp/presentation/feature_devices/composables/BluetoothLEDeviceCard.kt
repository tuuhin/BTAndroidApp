package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.imageVector

@Composable
fun BluetoothLEDeviceCard(
	leDeviceModel: BluetoothLEDeviceModel,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
) {

	Card(
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
		elevation = CardDefaults.elevatedCardElevation(),
		shape = shape,
		modifier = modifier,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
		) {
			Box(
				modifier = Modifier
					.defaultMinSize(
						minWidth = dimensionResource(id = R.dimen.min_device_image_size),
						minHeight = dimensionResource(id = R.dimen.min_device_image_size),
					)
					.clip(MaterialTheme.shapes.medium)
					.background(MaterialTheme.colorScheme.onPrimaryContainer),
				contentAlignment = Alignment.Center
			) {
				Icon(
					imageVector = leDeviceModel.deviceModel.imageVector,
					contentDescription = stringResource(
						id = R.string.devices_image_type,
						leDeviceModel.deviceName
					),
					tint = MaterialTheme.colorScheme.primaryContainer,
					modifier = Modifier.padding(8.dp)
				)
			}
			Column {
				Text(
					text = leDeviceModel.deviceName,
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = leDeviceModel.deviceModel.address,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}