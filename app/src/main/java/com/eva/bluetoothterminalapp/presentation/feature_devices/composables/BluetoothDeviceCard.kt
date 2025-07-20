package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.presentation.util.SharedElementTransitionKeys
import com.eva.bluetoothterminalapp.presentation.util.sharedBoundsWrapper
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BluetoothDeviceCard(
	device: BluetoothDeviceModel,
	onConnect: () -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
	selectedColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
	var isExpanded by remember { mutableStateOf(false) }

	val cardContainerColor by animateColorAsState(
		targetValue = if (isExpanded) selectedColor else containerColor,
		label = "Card container color",
		animationSpec = tween(durationMillis = 200)
	)

	Card(
		onClick = { isExpanded = true },
		shape = shape,
		colors = CardDefaults.cardColors(
			containerColor = cardContainerColor,
			contentColor = contentColorFor(if (isExpanded) selectedColor else containerColor)
		),
		elevation = CardDefaults.elevatedCardElevation(),
		modifier = modifier.sharedBoundsWrapper(SharedElementTransitionKeys.btProfileScreen(device.address))
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
		) {
			BTDeviceIcon(
				device = device,
				deviceName = device.name,
				contentColor = MaterialTheme.colorScheme.secondaryContainer,
				containerColor = MaterialTheme.colorScheme.onSecondaryContainer
			)
			Column {
				Text(
					text = device.name,
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = stringResource(
						id = R.string.bluetooth_device_mac_address, device.address
					),
					style = MaterialTheme.typography.bodyMedium,
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Box(modifier = Modifier) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = stringResource(id = R.string.menu_option_more)
				)
				DropdownMenu(
					expanded = isExpanded,
					onDismissRequest = { isExpanded = false },
					shape = MaterialTheme.shapes.medium,
					tonalElevation = 4.dp
				) {
					DropdownMenuItem(
						text = { Text(text = stringResource(R.string.connect_to_client)) },
						onClick = onConnect,
						leadingIcon = {
							Icon(
								imageVector = Icons.Outlined.Cable,
								contentDescription = stringResource(R.string.connect_to_client)
							)
						},
					)
				}
			}
		}
	}
}


@PreviewLightDark
@Composable
private fun BluetoothDeviceCardPreview() = BlueToothTerminalAppTheme {
	BluetoothDeviceCard(
		device = PreviewFakes.FAKE_DEVICE_MODEL,
		onConnect = {}
	)
}