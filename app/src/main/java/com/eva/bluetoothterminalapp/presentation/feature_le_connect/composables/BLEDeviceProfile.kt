package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.imageVector
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEDeviceProfile(
	device: BluetoothDeviceModel?,
	modifier: Modifier = Modifier,
	rssi: Int = 0,
	shape: Shape = MaterialTheme.shapes.large,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {

	val deviceStrength = remember(rssi) {
		"RSSI : $rssi ${BluetoothDeviceModel.RSSI_UNIT}"
	}

	AnimatedVisibility(
		visible = device != null,
		enter = slideInVertically { height -> height },
		exit = slideOutVertically { height -> -height }
	) {
		// the content will not be visible if its null
		if (device == null) return@AnimatedVisibility

		Column(modifier = modifier) {
			Text(
				text = stringResource(id = R.string.ble_device_info_title),
				style = MaterialTheme.typography.titleMedium,
				modifier = Modifier.padding(all = 4.dp)
			)
			Card(
				colors = CardDefaults.cardColors(
					containerColor = containerColor,
					contentColor = contentColorFor(containerColor)
				),
				elevation = CardDefaults.elevatedCardElevation(),
				shape = shape,
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
				) {
					Column(
						verticalArrangement = Arrangement.spacedBy(2.dp),
						horizontalAlignment = Alignment.Start,
						modifier = Modifier.weight(.7f)
					) {
						Text(
							text = device.name,
							style = MaterialTheme.typography.titleLarge
						)
						Text(
							text = device.address,
							style = MaterialTheme.typography.bodyLarge,
							modifier = Modifier.width(IntrinsicSize.Max)
						)

						Text(
							text = deviceStrength,
							style = MaterialTheme.typography.labelLarge,
							color = MaterialTheme.colorScheme.onSurfaceVariant,
							modifier = Modifier.width(IntrinsicSize.Max)
						)
					}
					Box(
						modifier = Modifier
							.defaultMinSize(80.dp, 80.dp)
							.clip(MaterialTheme.shapes.large)
							.background(MaterialTheme.colorScheme.secondaryContainer),
						contentAlignment = Alignment.Center
					) {
						Icon(
							imageVector = device.imageVector,
							contentDescription = device.name,
							modifier = Modifier.defaultMinSize(40.dp, 40.dp),
							tint = MaterialTheme.colorScheme.onSecondaryContainer,
						)
					}
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun BLEDeviceProfilePreview() = BlueToothTerminalAppTheme {
	Surface {
		BLEDeviceProfile(
			device = PreviewFakes.FAKE_DEVICE_MODEL,
			rssi = -50,
			modifier = Modifier
				.padding(12.dp)
				.fillMaxWidth()
		)
	}
}