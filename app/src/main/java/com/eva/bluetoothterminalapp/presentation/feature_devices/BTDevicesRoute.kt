package com.eva.bluetoothterminalapp.presentation.feature_devices

import android.Manifest
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.eva.bluetoothterminalapp.R
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
import kotlinx.coroutines.launch

@OptIn(
	ExperimentalFoundationApi::class,
	ExperimentalMaterial3Api::class
)
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

	val pagerState = rememberPagerState(
		initialPage = initialTab.tabIdx,
		pageCount = { BluetoothTypes.entries.size }
	)

	val currentTab by remember(pagerState.currentPage) {
		derivedStateOf {
			if (BluetoothTypes.LOW_ENERGY.tabIdx == pagerState.currentPage)
				BluetoothTypes.LOW_ENERGY
			else BluetoothTypes.CLASSIC
		}
	}

	val scope = rememberCoroutineScope()

	var hasBtPermission by remember(context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
			mutableStateOf(
				ContextCompat.checkSelfPermission(
					context, Manifest.permission.BLUETOOTH_SCAN
				) == PermissionChecker.PERMISSION_GRANTED
			)
		else mutableStateOf(true)
	}

	var hasLocationPermission by remember(context) {
		mutableStateOf(
			ContextCompat.checkSelfPermission(
				context, Manifest.permission.ACCESS_FINE_LOCATION
			) == PermissionChecker.PERMISSION_GRANTED
		)
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

	val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

	LaunchedEffect(hasBtPermission) {
		// responds when bt permission has changed
		onEvent(BTDevicesScreenEvents.OnBTPermissionChanged(hasBtPermission))
	}

	LaunchedEffect(pagerState.currentPage) {
		// when the current page changes stop any running scan
		onEvent(BTDevicesScreenEvents.OnStopAnyRunningScan)
	}

	Scaffold(
		topBar = {
			BTDeviceRouteTopBar(
				isScanning = isScanning,
				canShowScanOption = showScanButton,
				navigation = navigation,
				startScan = {
					when (currentTab) {
						BluetoothTypes.CLASSIC -> onEvent(BTDevicesScreenEvents.StartScan)
						BluetoothTypes.LOW_ENERGY -> onEvent(BTDevicesScreenEvents.StartLEDeviceScan)
					}
				},
				stopScan = {
					when (currentTab) {
						BluetoothTypes.CLASSIC -> onEvent(BTDevicesScreenEvents.StopScan)
						BluetoothTypes.LOW_ENERGY -> onEvent(BTDevicesScreenEvents.StopLEDevicesScan)
					}
				},
				scrollBehavior = scrollBehaviour
			)
		},
		snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
		modifier = modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
	) { scPadding ->
		DevicesScreenModeContainer(
			isActive = isBTActive,
			hasPermission = hasBtPermission,
			onBTPermissionChanged = { hasBtPermission = it },
			modifier = Modifier
				.fillMaxSize()
				.padding(scPadding)
		) {
			BTDevicesTabsLayout(
				pagerState = pagerState,
				isScanning = isScanning,
				onTabChange = { tab ->
					scope.launch {
						pagerState.animateScrollToPage(tab.tabIdx)
					}
				},
				classicTabContent = {
					BluetoothDevicesList(
						pairedDevices = state.pairedDevices,
						availableDevices = state.availableDevices,
						showLocationPlaceholder = showLocationBlock,
						onSelectDevice = onSelectDevice,
						contentPadding = PaddingValues(all = dimensionResource(R.dimen.sc_padding)),
						onLocationPermsAccept = { isAccepted ->
							hasLocationPermission = isAccepted
						},
						modifier = Modifier.fillMaxSize(),
					)
				},
				leTabContent = {
					BluetoothLeDeviceList(
						hasLocationPermission = hasLocationPermission,
						leDevices = state.leDevices,
						onDeviceSelect = onSelectLeDevice,
						contentPadding = PaddingValues(all = dimensionResource(R.dimen.sc_padding)),
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

private class BTDeviceClassicalScreenStateParams
	: CollectionPreviewParameterProvider<BTDevicesScreenState>(
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
		initialTab = BluetoothTypes.LOW_ENERGY,
		onSelectDevice = { }
	)
}