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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.presentation.util.SharedElementTransitionKeys
import com.eva.bluetoothterminalapp.presentation.util.sharedBoundsWrapper
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalSharedTransitionApi::class)
@Composable
fun BluetoothLEDeviceCard(
	leDeviceModel: BluetoothLEDeviceModel,
	modifier: Modifier = Modifier,
	onItemSelect: () -> Unit = {},
	shape: Shape = MaterialTheme.shapes.medium,
	selectedColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
	var isMenuExpanded by remember { mutableStateOf(false) }
	var localRssi by remember { mutableIntStateOf(leDeviceModel.rssi) }

	LaunchedEffect(leDeviceModel.rssi) {
		// a debounced version
		snapshotFlow { leDeviceModel.rssi }
			.debounce(200.milliseconds)
			.distinctUntilChanged()
			.collect { rssi -> localRssi = rssi }
	}

	val cardContainerColor by animateColorAsState(
		targetValue = if (isMenuExpanded) selectedColor else containerColor,
		label = "Card container color",
		animationSpec = tween(durationMillis = 200)
	)

	Card(
		onClick = { isMenuExpanded = true },
		colors = CardDefaults.cardColors(
			containerColor = cardContainerColor,
			contentColor = contentColorFor(backgroundColor = cardContainerColor)
		),
		elevation = CardDefaults.elevatedCardElevation(),
		shape = shape,
		modifier = modifier
			.sharedBoundsWrapper(
				key = SharedElementTransitionKeys.leDeviceCardToLeDeviceProfile(
					leDeviceModel.deviceModel.address
				)
			),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
		) {
			BTDeviceIcon(
				device = leDeviceModel.deviceModel,
				deviceName = leDeviceModel.deviceName
			)
			Column {
				Text(
					text = leDeviceModel.deviceName,
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = stringResource(
						id = R.string.bluetooth_device_mac_address,
						leDeviceModel.deviceModel.address
					),
					style = MaterialTheme.typography.bodyMedium,
					fontFamily = FontFamily.Monospace,
					fontWeight = FontWeight.Medium,
					letterSpacing = .25.sp
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			Row(
				verticalAlignment = Alignment.Bottom,
				horizontalArrangement = Arrangement.spacedBy(2.dp)
			) {
				Icon(
					imageVector = Icons.Default.SignalCellularAlt,
					contentDescription = stringResource(R.string.device_rssi_value),
					modifier = Modifier.size(24.dp),
					tint = MaterialTheme.colorScheme.onPrimaryContainer
				)
				Text(
					text = "$localRssi ${BluetoothDeviceModel.RSSI_UNIT}",
					style = MaterialTheme.typography.labelLarge
				)
			}
			Box(modifier = Modifier) {
				DropdownMenu(
					expanded = isMenuExpanded,
					onDismissRequest = { isMenuExpanded = false },
					shape = MaterialTheme.shapes.large,
					tonalElevation = 2.dp
				) {
					DropdownMenuItem(
						text = { Text(text = stringResource(R.string.connect_to_client)) },
						onClick = onItemSelect,
						leadingIcon = {
							Icon(
								imageVector = Icons.Outlined.Cable,
								contentDescription = stringResource(R.string.connect_to_client)
							)
						},
						colors = MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.primary)
					)
				}
			}
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