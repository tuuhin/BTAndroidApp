package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.contracts.StartBluetoothDiscovery
import com.eva.bluetoothterminalapp.presentation.util.BluetoothTypes

@Composable
fun BTDeviceRouteTopBar(
	canShowScanOption: Boolean,
	isScanning: Boolean,
	navigation: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	currentTab: BluetoothTypes = BluetoothTypes.CLASSIC,
	hasLocationPermission: Boolean = false,
	startClassicScan: () -> Unit = {},
	stopClassicScan: () -> Unit = {},
	startBLEScan: () -> Unit = {},
	stopBLEScan: () -> Unit = {},
	onStartDiscoveryResponse: (Int) -> Unit = {},
	colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
	scrollBehavior: TopAppBarScrollBehavior? = null,
) {

	val launcher = rememberLauncherForActivityResult(
		contract = StartBluetoothDiscovery(),
		onResult = onStartDiscoveryResponse,
	)

	val showScanOption = remember(canShowScanOption, currentTab) {
		if (currentTab == BluetoothTypes.LOW_ENERGY) canShowScanOption && hasLocationPermission
		else canShowScanOption
	}

	val startScan by rememberUpdatedState(if (currentTab == BluetoothTypes.CLASSIC) startClassicScan else startBLEScan)
	val stopScan by rememberUpdatedState(if (currentTab == BluetoothTypes.CLASSIC) stopClassicScan else stopBLEScan)

	MediumTopAppBar(
		title = { Text(text = stringResource(id = R.string.devices_route)) },
		navigationIcon = navigation,
		actions = {
			AnimatedScanButton(
				isScanning = isScanning,
				canShowScanOption = showScanOption,
				startScan = startScan,
				stopScan = stopScan
			)
			Spacer(modifier = Modifier.width(2.dp))
			BTDevicesTopBarMenu(
				isBTEnabled = canShowScanOption,
				onStartDiscovery = launcher::launch
			)
		},
		colors = colors,
		scrollBehavior = scrollBehavior,
		modifier = modifier
	)
}