package com.eva.bluetoothterminalapp.presentation.feature_devices

import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.utils.hasBTScanPermission
import com.eva.bluetoothterminalapp.data.utils.hasLocationPermission
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.composables.BTDeviceRouteTopBar
import com.eva.bluetoothterminalapp.presentation.feature_devices.composables.BTDevicesTabsLayout
import com.eva.bluetoothterminalapp.presentation.feature_devices.composables.BluetoothDevicesList
import com.eva.bluetoothterminalapp.presentation.feature_devices.composables.BluetoothLeDeviceList
import com.eva.bluetoothterminalapp.presentation.feature_devices.composables.DevicesScreenModeContainer
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenEvents
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenState
import com.eva.bluetoothterminalapp.presentation.util.BluetoothTypes
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTDevicesRoute(
	isBTActive: Boolean,
	isScanning: Boolean,
	state: BTDevicesScreenState,
	onEvent: (BTDevicesScreenEvents) -> Unit,
	modifier: Modifier = Modifier,
	initialTab: BluetoothTypes = BluetoothTypes.CLASSIC,
	onSelectDevice: (BluetoothDeviceModel) -> Unit = {},
	onSelectLeDevice: (BluetoothLEDeviceModel) -> Unit = {},
	navigation: @Composable () -> Unit = {},
) {
	val context = LocalContext.current
	val snackBarHostState = LocalSnackBarProvider.current
	val layoutDirection = LocalLayoutDirection.current

	var currentTab by rememberSaveable { mutableStateOf(initialTab) }

	var hasBtPermission by rememberSaveable {
		mutableStateOf(context.hasBTScanPermission)
	}

	var hasLocationPermission by rememberSaveable {
		mutableStateOf(context.hasLocationPermission)
	}

	val showLocationBlock by remember(hasLocationPermission) {
		derivedStateOf { Build.VERSION.SDK_INT < Build.VERSION_CODES.S && !hasLocationPermission }
	}

	val showScanButton by remember(hasBtPermission, hasLocationPermission, isBTActive) {
		derivedStateOf {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) hasBtPermission && isBTActive
			else hasLocationPermission && isBTActive
		}
	}

	val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

	Scaffold(
		topBar = {
			BTDeviceRouteTopBar(
				isScanning = isScanning,
				canShowScanOption = showScanButton,
				currentTab = currentTab,
				hasLocationPermission = hasLocationPermission,
				startClassicScan = { onEvent(BTDevicesScreenEvents.StartScan) },
				stopClassicScan = { onEvent(BTDevicesScreenEvents.StopScan) },
				startBLEScan = { onEvent(BTDevicesScreenEvents.StartLEDeviceScan) },
				stopBLEScan = { onEvent(BTDevicesScreenEvents.StopLEDevicesScan) },
				onStartDiscoveryResponse = { code ->
					onEvent(BTDevicesScreenEvents.OnHandleStartBTDiscoveryContract(code))
				},
				navigation = navigation,
				scrollBehavior = scrollBehaviour,
			)
		},
		snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
		modifier = modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
	) { scPadding ->
		DevicesScreenModeContainer(
			isActive = isBTActive,
			hasPermission = hasBtPermission,
			onBTPermissionChanged = { isGranted ->
				hasBtPermission = isGranted
				onEvent(BTDevicesScreenEvents.OnBTPermissionChanged(isGranted))
			},
			modifier = Modifier
				.fillMaxSize()
				.padding(
					top = scPadding.calculateTopPadding(),
					bottom = scPadding.calculateBottomPadding() + dimensionResource(R.dimen.sc_padding_secondary),
				)
		) {
			BTDevicesTabsLayout(
				isScanning = isScanning,
				initialTab = initialTab,
				onCurrentTabChanged = { type ->
					currentTab = type
					onEvent(BTDevicesScreenEvents.OnStopAnyRunningScan)
				},
				contentPadding = PaddingValues(
					start = scPadding.calculateStartPadding(layoutDirection) + dimensionResource(R.dimen.sc_padding),
					end = scPadding.calculateEndPadding(layoutDirection) + dimensionResource(R.dimen.sc_padding)
				),
				classicTabContent = {
					BluetoothDevicesList(
						pairedDevices = state.pairedDevices,
						availableDevices = state.availableDevices,
						isPairedDevicesReady = state.isPairedDevicesLoaded,
						showLocationPlaceholder = showLocationBlock,
						onSelectDevice = onSelectDevice,
						onLocationPermsAccept = { isGranted ->
							hasLocationPermission = isGranted
							onEvent(BTDevicesScreenEvents.OnLocationPermissionChanged(isGranted))
						},
						modifier = Modifier.fillMaxSize(),
					)
				},
				leTabContent = {
					BluetoothLeDeviceList(
						hasLocationPermission = hasLocationPermission,
						leDevices = state.leDevices,
						onDeviceSelect = onSelectLeDevice,
						onLocationPermissionChanged = { isAccepted ->
							hasLocationPermission = isAccepted
						},
						modifier = Modifier.fillMaxSize(),
					)
				},
			)
		}
	}
}

private class BTDeviceClassicalScreenStateParams :
	CollectionPreviewParameterProvider<BTDevicesScreenState>(
		listOf(
			PreviewFakes.FAKE_DEVICE_STATE_WITH_PAIRED_AND_AVAILABLE_DEVICES,
			PreviewFakes.FAKE_DEVICE_STATE_WITH_PAIRED_DEVICE,
			PreviewFakes.FAKE_DEVICE_STATE_WITH_NO_DEVICE,
		)
	)

@PreviewLightDark
@Composable
private fun BTDeviceRouteWithClassicDevicesPreview(
	@PreviewParameter(BTDeviceClassicalScreenStateParams::class)
	state: BTDevicesScreenState
) = BlueToothTerminalAppTheme {
	BTDevicesRoute(
		isBTActive = true,
		isScanning = false,
		state = state,
		onEvent = {},
		onSelectDevice = { }
	)
}

class BTDevicesLEScreenStateParams
	: CollectionPreviewParameterProvider<BTDevicesScreenState>(
	listOf(
		PreviewFakes.FAKE_DEVICE_STATE_WITH_NO_DEVICE,
		PreviewFakes.FAKE_DEVICE_STATE_WITH_SOME_BLE_DEVICES,
	)
)

@PreviewLightDark
@Composable
private fun BTDeviceRouteWithLEDevicesPreview(
	@PreviewParameter(BTDevicesLEScreenStateParams::class)
	state: BTDevicesScreenState
) = BlueToothTerminalAppTheme {
	BTDevicesRoute(
		isBTActive = true,
		isScanning = false,
		state = state,
		onEvent = {},
		onSelectDevice = { }
	)
}