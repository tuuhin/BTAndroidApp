package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.imageVector
import com.eva.bluetoothterminalapp.ui.theme.RoundedPolygonShape

@Composable
fun BTDeviceIconLarge(
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
