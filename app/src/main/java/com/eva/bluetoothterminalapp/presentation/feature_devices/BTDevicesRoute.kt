package com.eva.bluetoothterminalapp.presentation.feature_devices

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.composables.BTNotEnabledBox
import com.eva.bluetoothterminalapp.presentation.composables.BtPermissionNotProvidedBox
import com.eva.bluetoothterminalapp.presentation.composables.LocationPermissionCard
import com.eva.bluetoothterminalapp.presentation.feature_devices.composables.BTConnectToServerButton
import com.eva.bluetoothterminalapp.presentation.feature_devices.composables.BTDeviceRouteTopBar
import com.eva.bluetoothterminalapp.presentation.feature_devices.composables.BluetoothDevicesList
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenState
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.DeviceScreenEvents
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.BluetoothScreenType
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BTDevicesRoute(
	state: BTDevicesScreenState,
	onEvent: (DeviceScreenEvents) -> Unit,
	modifier: Modifier = Modifier,
	onConnectAsServer: () -> Unit = {},
	onSelectDevice: (BluetoothDeviceModel) -> Unit = {},
	navigation: @Composable () -> Unit = {},
) {
	val context = LocalContext.current
	val snackBarHostState = LocalSnackBarProvider.current

	var hasBtPermission by remember(context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
			mutableStateOf(
				ContextCompat.checkSelfPermission(
					context, Manifest.permission.BLUETOOTH_SCAN
				) == PermissionChecker.PERMISSION_GRANTED
			)
		else mutableStateOf(true)
	}

	var hasFineLocationPermission by remember(context) {
		mutableStateOf(
			ContextCompat.checkSelfPermission(
				context, Manifest.permission.ACCESS_FINE_LOCATION
			) == PermissionChecker.PERMISSION_GRANTED
		)
	}

	val screenType by remember(hasBtPermission, state.isBtActive) {
		derivedStateOf {
			when {
				hasBtPermission && state.isBtActive -> BluetoothScreenType.BLUETOOTH_PERMISSION_GRANTED
				hasBtPermission && !state.isBtActive -> BluetoothScreenType.BLUETOOTH_NOT_ENABLED
				else -> BluetoothScreenType.BLUETOOTH_PERMISSION_DENIED
			}
		}
	}


	val showLocationBlock by remember(hasFineLocationPermission) {
		derivedStateOf { Build.VERSION.SDK_INT <= Build.VERSION_CODES.R && !hasFineLocationPermission }
	}

	val showScanButton by remember(hasBtPermission, hasFineLocationPermission, state.isBtActive) {
		derivedStateOf {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
				hasBtPermission && state.isBtActive
			else hasFineLocationPermission && state.isBtActive
		}
	}

	val showServerButton by remember(hasBtPermission, state.isBtActive) {
		derivedStateOf {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
				hasBtPermission && state.isBtActive
			else state.isBtActive
		}
	}

	Scaffold(
		topBar = {
			BTDeviceRouteTopBar(
				isScanning = state.isScanning,
				canShowScanOption = showScanButton,
				navigation = navigation,
				startScan = { onEvent(DeviceScreenEvents.StartScan) },
				stopScan = { onEvent(DeviceScreenEvents.StopScan) },
			)
		},
		floatingActionButton = {
			AnimatedVisibility(
				visible = showServerButton,
				enter = slideInVertically(),
				exit = slideOutVertically()
			) {
				BTConnectToServerButton(onConnectAsServer = onConnectAsServer)
			}
		},
		snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
		modifier = modifier,
	) { scPadding ->

		Crossfade(
			targetState = screenType,
			label = "Is Bluetooth Active",
			animationSpec = tween(durationMillis = 400),
			modifier = Modifier.padding(scPadding)
		) { mode ->
			when (mode) {
				BluetoothScreenType.BLUETOOTH_NOT_ENABLED -> BTNotEnabledBox(
					modifier = Modifier
						.fillMaxSize()
						.padding(dimensionResource(R.dimen.sc_padding))
				)

				BluetoothScreenType.BLUETOOTH_PERMISSION_GRANTED -> Column(
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.spacedBy(4.dp)
				) {
					AnimatedVisibility(
						visible = state.isScanning,
						enter = slideInHorizontally(),
						exit = slideOutHorizontally()
					) {
						LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
					}
					BluetoothDevicesList(
						pairedDevices = state.pairedDevices,
						availableDevices = state.availableDevices,
						onSelectDevice = onSelectDevice,
						locationPlaceholder = {
							AnimatedVisibility(
								visible = showLocationBlock,
								enter = expandVertically() + fadeIn(),
								exit = shrinkVertically() + fadeOut(),
								modifier = Modifier.animateItemPlacement()
							) {
								LocationPermissionCard(
									onLocationAccess = { isAllowed ->
										hasFineLocationPermission = isAllowed
									},
								)
							}
						},
						modifier = Modifier.padding(dimensionResource(R.dimen.sc_padding))
					)
				}

				BluetoothScreenType.BLUETOOTH_PERMISSION_DENIED -> BtPermissionNotProvidedBox(
					onPermissionChanged = { hasBtPermission = it },
					modifier = Modifier
						.fillMaxSize()
						.padding(dimensionResource(R.dimen.sc_padding))
				)
			}
		}
	}
}

private class BTDeviceRoutePreviewParams : CollectionPreviewParameterProvider<BTDevicesScreenState>(
	listOf(
		PreviewFakes.FAKE_DEVICE_SCREEN_STATE_WITH_SCANNING_AND_CONNECTED_DEVICE,
		PreviewFakes.FAKE_DEVICE_SCREEN_STATE_WITH_BL_ON_AND_PAIRED_DEVICES,
		PreviewFakes.FAKE_DEVICE_SCREEN_STATE_WITH_BL_OFF,
	)
)

@PreviewLightDark
@Composable
private fun BTDeviceRoutePreview(
	@PreviewParameter(BTDeviceRoutePreviewParams::class)
	state: BTDevicesScreenState
) = BlueToothTerminalAppTheme {
	BTDevicesRoute(
		state = state,
		onEvent = {},
		onSelectDevice = { }
	)
}