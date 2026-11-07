package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.imageVector
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTDeviceIconLarge(
	device: BluetoothDeviceModel,
	modifier: Modifier = Modifier,
	deviceName: String? = null,
	containerColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
	contentColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
	shape: Shape = MaterialShapes.Bun.toShape(),
) {

	Surface(
		color = containerColor,
		contentColor = contentColor,
		shape = shape,
		modifier = modifier.defaultMinSize(
			minWidth = dimensionResource(R.dimen.min_bl_image_container_size),
			minHeight = dimensionResource(R.dimen.min_bl_image_container_size),
		),
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.padding(24.dp)
		) {
			Icon(
				imageVector = device.imageVector,
				contentDescription = deviceName?.let {
					stringResource(id = R.string.devices_image_type, it)
				},
				modifier = Modifier.sizeIn(minWidth = 60.dp, minHeight = 60.dp),
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun BTDeviceIconLargePreview() = BlueToothTerminalAppTheme {
	BTDeviceIconLarge(device = PreviewFakes.FAKE_DEVICE_MODEL)
}