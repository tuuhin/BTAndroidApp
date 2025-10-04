package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.imageVector
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import com.eva.bluetoothterminalapp.ui.theme.RoundedPolygonShape

@Composable
fun BTDeviceIcon(
	device: BluetoothDeviceModel,
	modifier: Modifier = Modifier,
	deviceName: String? = null,
	containerColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
	contentColor: Color = MaterialTheme.colorScheme.primaryContainer,
	borderColor: Color = MaterialTheme.colorScheme.primaryFixed,
	showBorder: Boolean = false,
	innerIconSize: DpSize = DpSize(32.dp, 32.dp),
) {

	val pillShape = remember {
		RoundedPolygon.star(
			numVerticesPerRadius = 8,
			rounding = CornerRounding(radius = .2f, smoothing = .2f),
		)
	}

	Surface(
		color = containerColor,
		contentColor = contentColor,
		modifier = modifier
			.clip(RoundedPolygonShape(pillShape))
			.then(
				if (showBorder) Modifier.border(
					2.dp,
					borderColor,
					RoundedPolygonShape(pillShape)
				) else Modifier
			)
			.defaultMinSize(
				minWidth = dimensionResource(id = R.dimen.min_device_image_size),
				minHeight = dimensionResource(id = R.dimen.min_device_image_size),
			)
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.padding(8.dp)
		) {
			Icon(
				imageVector = device.imageVector,
				contentDescription = deviceName?.let {
					stringResource(id = R.string.devices_image_type, it)
				},
				modifier = Modifier.size(innerIconSize)
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun BTDeviceIconPreview() = BlueToothTerminalAppTheme {
	BTDeviceIcon(device = PreviewFakes.FAKE_DEVICE_MODEL)
}