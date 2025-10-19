package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLEDeviceConfigEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLEDeviceRouteTopBar(
	connectionState: BLEConnectionState,
	onConfigEvent: (BLEDeviceConfigEvent) -> Unit,
	modifier: Modifier = Modifier,
	scrollConnection: TopAppBarScrollBehavior? = null,
	navigation: @Composable () -> Unit = {},
	colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors()
) {

	var isMenuExpanded by remember { mutableStateOf(false) }
	var menuOffset by remember { mutableStateOf(DpOffset.Zero) }

	MediumTopAppBar(
		title = { Text(text = stringResource(id = R.string.ble_client_screen_title)) },
		actions = {
			ConnectReconnectButton(
				connectionState = connectionState,
				onDisconnect = { onConfigEvent(BLEDeviceConfigEvent.OnDisconnectEvent) },
				onReconnect = { onConfigEvent(BLEDeviceConfigEvent.OnReconnectEvent) },
			)

			Box(modifier = modifier) {
				TooltipBox(
					positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
						TooltipAnchorPosition.Below
					),
					tooltip = {
						PlainTooltip(
							modifier = Modifier.padding(4.dp),
							shape = MaterialTheme.shapes.medium
						) {
							Text(
								text = stringResource(id = R.string.settings_tooltip_text),
								style = MaterialTheme.typography.labelMedium
							)
						}
					},
					state = rememberTooltipState()
				) {
					IconButton(onClick = { isMenuExpanded = true }) {
						Icon(
							imageVector = Icons.Default.MoreVert,
							contentDescription = stringResource(id = R.string.menu_option_more),
						)
					}
				}
				DropdownMenu(
					expanded = isMenuExpanded,
					offset = menuOffset,
					onDismissRequest = { isMenuExpanded = false },
					shape = MaterialTheme.shapes.medium,
					tonalElevation = 2.dp
				) {
					DropdownMenuItem(
						text = { Text(text = stringResource(R.string.ble_device_profile_action_refresh_rssi)) },
						onClick = { onConfigEvent(BLEDeviceConfigEvent.OnReadRssiStrength) },
						colors = MenuDefaults
							.itemColors(leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer)
					)
					DropdownMenuItem(
						text = { Text(text = stringResource(R.string.ble_device_profile_action_refresh_services)) },
						enabled = connectionState == BLEConnectionState.CONNECTED,
						onClick = { onConfigEvent(BLEDeviceConfigEvent.OnReDiscoverServices) },
						colors = MenuDefaults
							.itemColors(leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer)
					)
				}
			}
		},
		scrollBehavior = scrollConnection,
		modifier = modifier,
		colors = colors,
		navigationIcon = navigation,
	)
}