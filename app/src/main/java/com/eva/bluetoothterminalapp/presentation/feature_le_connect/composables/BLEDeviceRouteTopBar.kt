package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceConfigEvent

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
	TopAppBar(
		title = { Text(text = stringResource(id = R.string.ble_device_profile)) },
		actions = {
			ConnectReconnectButton(
				connctionState = connectionState,
				onDisconnect = { onConfigEvent(BLEDeviceConfigEvent.OnDisconnectEvent) },
				onReconnect = { onConfigEvent(BLEDeviceConfigEvent.OnReconnectEvent) },
			)
			BLEdeviceTopBarMenuOptions(
				onReDiscoverServices = { onConfigEvent(BLEDeviceConfigEvent.OnRefreshCharacteristics) },
				onReadRemoteRssi = { onConfigEvent(BLEDeviceConfigEvent.OnReadRssiStrength) },
			)
		},
		scrollBehavior = scrollConnection,
		modifier = modifier,
		colors = colors,
		navigationIcon = navigation,
	)
}