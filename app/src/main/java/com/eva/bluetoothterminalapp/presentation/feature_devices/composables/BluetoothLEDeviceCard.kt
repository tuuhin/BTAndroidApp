package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.imageVector
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BluetoothLEDeviceCard(
	leDeviceModel: BluetoothLEDeviceModel,
	modifier: Modifier = Modifier,
	onItemSelect: () -> Unit = {},
	shape: Shape = MaterialTheme.shapes.medium,
) {

	var isMenuExpanded by remember { mutableStateOf(false) }
	var dropDownOffset by remember { mutableStateOf(DpOffset.Zero) }
	var componentSize by remember { mutableStateOf(IntSize.Zero) }

	val interactionSource = remember { MutableInteractionSource() }

	val cardContainerColor by animateColorAsState(
		targetValue = if (isMenuExpanded)
			MaterialTheme.colorScheme.surfaceContainerHighest
		else MaterialTheme.colorScheme.surfaceContainerHigh,
		label = "Card container color",
		animationSpec = tween(durationMillis = 200)
	)

	Box(
		modifier = modifier
			.clip(shape)
			.indication(interactionSource, LocalIndication.current)
			.onSizeChanged { size -> componentSize = size }
			.pointerInput(Unit) {
				detectTapGestures(
					onPress = { offset ->
						val press = PressInteraction.Press(offset)
						interactionSource.emit(press)
						tryAwaitRelease()
						val release = PressInteraction.Release(press)
						interactionSource.emit(release)
					},
					onTap = { offset ->
						//set drop down offset
						dropDownOffset = DpOffset(
							x = offset.x.toDp(),
							y = (offset.y - componentSize.height).toDp()
						)
						// show dropdown
						isMenuExpanded = true
					},
				)
			},
	) {
		Card(
			colors = CardDefaults.cardColors(containerColor = cardContainerColor),
			elevation = CardDefaults.elevatedCardElevation(),
			shape = shape,
			modifier = Modifier.fillMaxWidth(),
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
		DropdownMenu(
			expanded = isMenuExpanded,
			onDismissRequest = { isMenuExpanded = false },
			offset = dropDownOffset,
		) {
			DropdownMenuItem(
				text = { Text(text = stringResource(id = R.string.connect_to_client)) },
				onClick = onItemSelect,
				leadingIcon = {
					Icon(
						imageVector = Icons.Default.BluetoothConnected,
						contentDescription = stringResource(id = R.string.connect_to_client)
					)
				}
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun BluetoothLEDeviceCardPreview() = BlueToothTerminalAppTheme {
	BluetoothLEDeviceCard(
		leDeviceModel = PreviewFakes.FAKE_BLE_DEVICE_MODEL,
	)
}