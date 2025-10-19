package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.composables.BTDeviceIcon

@Composable
fun ConnectedDeviceCard(
	device: BluetoothDeviceModel,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.large,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
) {
	Surface(
		shape = shape,
		color = containerColor,
		contentColor = contentColorFor(containerColor),
		modifier = modifier
	) {
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.padding(all = dimensionResource(R.dimen.ble_server_device_card_padding))
		) {
			BTDeviceIcon(
				device = device,
				deviceName = device.name,
				contentColor = MaterialTheme.colorScheme.primaryContainer,
				containerColor = MaterialTheme.colorScheme.onPrimaryContainer
			)
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = device.name,
				maxLines = 2,
				overflow = TextOverflow.Ellipsis,
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurface
			)
			Spacer(modifier = Modifier.height(2.dp))
			Text(
				text = device.address,
				fontFamily = FontFamily.Monospace,
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.SemiBold
			)
		}
	}
}