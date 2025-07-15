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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.imageVector
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import com.eva.bluetoothterminalapp.ui.theme.RoundedPolygonShape

@Composable
fun BLEDeviceProfile(
	device: BluetoothDeviceModel?,
	modifier: Modifier = Modifier,
	connectionState: BLEConnectionState = BLEConnectionState.CONNECTED,
	rssi: Int = 0,
	shape: Shape = MaterialTheme.shapes.large,
	containerColor: Color = MaterialTheme.colorScheme.secondaryContainer
) {

	AnimatedVisibility(
		visible = device != null,
		enter = slideInVertically { height -> height },
		exit = slideOutVertically { height -> -height }
	) {
		// the content will not be visible if its null
		if (device == null) return@AnimatedVisibility

		Card(
			colors = CardDefaults.cardColors(
				containerColor = containerColor,
				contentColor = contentColorFor(containerColor)
			),
			elevation = CardDefaults.elevatedCardElevation(),
			shape = shape,
			modifier = modifier,
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
						style = MaterialTheme.typography.titleLarge,
						fontWeight = FontWeight.SemiBold
					)
					Text(
						text = device.address,
						style = MaterialTheme.typography.labelLarge,
						fontFamily = FontFamily.Monospace,
					)
					Spacer(modifier = Modifier.height(4.dp))
					Text(
						text = "RSSI : $rssi ${BluetoothDeviceModel.RSSI_UNIT}",
						style = MaterialTheme.typography.labelLarge,
						modifier = Modifier.width(IntrinsicSize.Max)
					)
					BLEConnectionStatusChip(
						state = connectionState,
						shape = MaterialTheme.shapes.small
					)
				}
				DeviceIcon(device = device)
			}
		}
	}
}

@Composable
private fun DeviceIcon(
	device: BluetoothDeviceModel,
	modifier: Modifier = Modifier,
	deviceName: String? = null,
	containerColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
	contentColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
) {

	val pillShape = remember {
		RoundedPolygon.star(
			numVerticesPerRadius = 4,
			rounding = CornerRounding(radius = .75f, smoothing = .2f),
		)
	}

	Box(
		modifier = modifier
			.defaultMinSize(
				minWidth = dimensionResource(R.dimen.min_bl_image_container_size),
				minHeight = dimensionResource(R.dimen.min_bl_image_container_size),
			)
			.clip(
				RoundedPolygonShape(polygon = pillShape, rotate = 45f)
			)
			.background(containerColor),
		contentAlignment = Alignment.Center
	) {
		Icon(
			imageVector = device.imageVector,
			contentDescription = deviceName?.let {
				stringResource(id = R.string.devices_image_type, it)
			},
			tint = contentColor,
			modifier = Modifier
				.defaultMinSize(60.dp, 60.dp)
				.padding(12.dp),
		)
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