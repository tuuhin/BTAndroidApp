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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
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

	Surface(
		onClick = { isMenuExpanded = true },
		color = cardContainerColor,
		contentColor = contentColorFor(containerColor),
		shape = shape,
		modifier = modifier.sharedBoundsWrapper(
			key = SharedElementTransitionKeys.leDeviceCardToLeDeviceProfile(leDeviceModel.deviceModel.address)
		),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
		) {
			BTDeviceIcon(
				device = leDeviceModel.deviceModel,
				deviceName = leDeviceModel.deviceName,
				containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
				contentColor = MaterialTheme.colorScheme.secondaryContainer,
			)
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = leDeviceModel.deviceName,
					style = MaterialTheme.typography.titleMedium,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = buildAnnotatedString {
						append(stringResource(id = R.string.bluetooth_device_mac_address_title))
						append(" ")
						withStyle(SpanStyle(fontFamily = FontFamily.Monospace)) {
							append(leDeviceModel.deviceModel.address)
						}
					},
					style = MaterialTheme.typography.bodyMedium,
				)
				Row(
					verticalAlignment = Alignment.Bottom,
					horizontalArrangement = Arrangement.spacedBy(2.dp),
					modifier = Modifier
						.wrapContentSize()
						.padding(top = 4.dp)
				) {
					SignalsBars(
						rssi = { localRssi },
						color = MaterialTheme.colorScheme.onPrimaryContainer,
						modifier = Modifier.size(width = 20.dp, height = 20.dp)
					)
					Text(
						text = buildString {
							append(localRssi)
							append(" ")
							append(BluetoothDeviceModel.RSSI_UNIT)
						},
						style = MaterialTheme.typography.labelLarge
					)
				}
			}
			Box {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = stringResource(id = R.string.menu_option_more)
				)
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

@Composable
private fun SignalsBars(
	rssi: () -> Int,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.primary
) {
	Spacer(
		modifier = modifier
			.sizeIn(minWidth = 24.dp, minHeight = 24.dp)
			.drawWithCache {
				val blockWidth = size.width / 3
				val blockHeight = size.height / 3

				val strokeWidth = maxOf(2.dp.toPx(), blockWidth * .5f)
				val maxHeight = size.height - 4.dp.toPx()
				val noOfBars = 3

				onDrawBehind {
					val computedRssi = rssi()
					val count = if (computedRssi >= -50) 3
					else if (computedRssi >= -71) 2
					else if (computedRssi >= -90) 1
					else 0

					for (idx in 0..<noOfBars) {
						if (idx == count) break
						drawLine(
							color = color,
							start = Offset(
								idx * blockWidth,
								(blockHeight * (noOfBars - idx - 1))
									.coerceIn(4.dp.toPx(), maxHeight)
							),
							end = Offset(idx * blockWidth, maxHeight),
							strokeWidth = strokeWidth,
							cap = StrokeCap.Round,
						)
					}
				}
			},
	)
}

@PreviewLightDark
@Composable
private fun BluetoothLEDeviceCardPreview() = BlueToothTerminalAppTheme {
	BluetoothLEDeviceCard(
		leDeviceModel = PreviewFakes.FAKE_BLE_DEVICE_MODEL,
	)
}