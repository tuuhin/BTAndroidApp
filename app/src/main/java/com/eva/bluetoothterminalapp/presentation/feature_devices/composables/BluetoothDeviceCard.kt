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
import androidx.compose.material.icons.outlined.BluetoothConnected
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
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
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.imageVector
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BluetoothDeviceCard(
	device: BluetoothDeviceModel,
	onConnect: () -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
) {
	var showDropDown by remember { mutableStateOf(false) }
	var componentSize by remember { mutableStateOf(IntSize.Zero) }
	var dpOffset by remember { mutableStateOf(DpOffset.Zero) }

	val interactionSource = remember { MutableInteractionSource() }

	val cardContainerColor by animateColorAsState(
		targetValue = if (showDropDown)
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
						showDropDown = true
						dpOffset = DpOffset(
							x = offset.x.toDp(),
							y = (offset.y - componentSize.height).toDp()
						)
					}
				)
			},
	) {
		Card(
			shape = shape,
			colors = CardDefaults.cardColors(containerColor = cardContainerColor),
			elevation = CardDefaults.elevatedCardElevation(),
			modifier = Modifier.fillMaxWidth()
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
						imageVector = device.imageVector,
						contentDescription = stringResource(
							id = R.string.devices_image_type,
							"${device.type}"
						),
						tint = MaterialTheme.colorScheme.primaryContainer,
						modifier = Modifier.padding(8.dp)
					)
				}
				Column {
					Text(
						text = device.name,
						style = MaterialTheme.typography.titleMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
					Text(
						text = device.address,
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
			}
			DropdownMenu(
				expanded = showDropDown,
				onDismissRequest = { showDropDown = false },
				offset = dpOffset
			) {
				DropdownMenuItem(
					text = { Text(text = stringResource(R.string.bt_connection_type_client)) },
					onClick = onConnect,
					leadingIcon = {
						Icon(
							imageVector = Icons.Outlined.BluetoothConnected,
							contentDescription = stringResource(R.string.bt_connection_type_client)
						)
					},
					colors = MenuDefaults.itemColors(
						textColor = MaterialTheme.colorScheme.onSurface,
						leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
					),
				)
			}
		}
	}
}


@PreviewLightDark
@Composable
private fun BluetoothDeviceCardPreview() = BlueToothTerminalAppTheme {
	Surface {
		BluetoothDeviceCard(
			device = PreviewFakes.FAKE_DEVICE_MODEL,
			onConnect = {}
		)
	}
}