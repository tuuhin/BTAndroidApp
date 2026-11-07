package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors
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

	var isMenuExpanded by rememberSaveable { mutableStateOf(false) }
	var menuOffset by remember { mutableStateOf(DpOffset.Zero) }

	var showRequestMTUDialog by rememberSaveable { mutableStateOf(false) }

	BLEDeviceRequestMTUDialog(
		showDialog = showRequestMTUDialog,
		onDismissRequest = { showRequestMTUDialog = false },
		onRequestValue = { unit ->
			onConfigEvent(BLEDeviceConfigEvent.OnUpdateMTU(unit))
			showRequestMTUDialog = false
		},
	)

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
					FilledIconButton(
						onClick = { isMenuExpanded = true },
						colors = IconButtonDefaults.iconButtonColors(
							containerColor = MaterialTheme.colorScheme.secondary,
							contentColor = MaterialTheme.colorScheme.onSecondary,
						),
						modifier = Modifier
							.minimumInteractiveComponentSize()
							.size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
						shapes = IconButtonDefaults.shapes(
							shape = IconButtonDefaults.smallRoundShape,
							pressedShape = IconButtonDefaults.smallPressedShape,
						),
					) {
						Icon(
							imageVector = AppIconVectors.moreVert,
							contentDescription = stringResource(id = R.string.menu_option_more),
						)
					}
				}
				DropdownMenu(
					expanded = isMenuExpanded,
					offset = menuOffset,
					onDismissRequest = { isMenuExpanded = false },
					shape = MaterialTheme.shapes.large,
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
					DropdownMenuItem(
						text = { Text(text = stringResource(R.string.ble_device_profile_action_request_mtu)) },
						enabled = connectionState == BLEConnectionState.CONNECTED,
						onClick = { showRequestMTUDialog = true },
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