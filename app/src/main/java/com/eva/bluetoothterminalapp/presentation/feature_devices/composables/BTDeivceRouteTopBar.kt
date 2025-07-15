package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.contracts.StartBluetoothDiscovery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTDeviceRouteTopBar(
	canShowScanOption: Boolean,
	isScanning: Boolean,
	navigation: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	startScan: () -> Unit = {},
	stopScan: () -> Unit = {},
	colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
	scrollBehavior: TopAppBarScrollBehavior? = null,
) {
	val context = LocalContext.current

	val launcher = rememberLauncherForActivityResult(
		contract = StartBluetoothDiscovery(),
		onResult = { result ->
			val message = when (result) {
				Activity.RESULT_CANCELED -> context.getString(R.string.device_discoverable_denied)
				else -> context.getString(R.string.device_discoverable_success, result)
			}
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
		},
	)


	MediumTopAppBar(
		title = { Text(text = stringResource(id = R.string.devices_route)) },
		navigationIcon = navigation,
		actions = {
			AnimatedScanButton(
				isScanning = isScanning,
				canShowScanOption = canShowScanOption,
				startScan = startScan,
				stopScan = stopScan
			)
			BTDevicesTopBarMenu(
				hasDiscoverPermission = canShowScanOption,
				onStartDiscovery = launcher::launch
			)
		},
		colors = colors,
		scrollBehavior = scrollBehavior,
		modifier = modifier
	)
}