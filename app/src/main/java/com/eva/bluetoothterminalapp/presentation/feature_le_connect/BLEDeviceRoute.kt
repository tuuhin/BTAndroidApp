package com.eva.bluetoothterminalapp.presentation.feature_le_connect

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEDeviceRouteTopBar
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEDeviceScreenContent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLECharacteristicEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLEDeviceConfigEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLEDeviceProfileState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.SelectedCharacteristicState
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.presentation.util.SharedElementTransitionKeys
import com.eva.bluetoothterminalapp.presentation.util.sharedBoundsWrapper
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalSharedTransitionApi::class
)
@Composable
fun BLEDeviceRoute(
	deviceAddress: String,
	profile: BLEDeviceProfileState,
	selectedCharacteristic: SelectedCharacteristicState,
	onSelectEvent: (BLECharacteristicEvent) -> Unit,
	modifier: Modifier = Modifier,
	onConfigEvent: (BLEDeviceConfigEvent) -> Unit = {},
	navigation: @Composable () -> Unit = {},
) {
	val snackBarHostState = LocalSnackBarProvider.current
	val layoutDirection = LocalLayoutDirection.current
	val scrollConnection = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


	Scaffold(
		topBar = {
			BLEDeviceRouteTopBar(
				connectionState = profile.connectionState,
				onConfigEvent = onConfigEvent,
				navigation = navigation,
				scrollConnection = scrollConnection,
			)
		},
		snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
		modifier = modifier
			.nestedScroll(scrollConnection.nestedScrollConnection)
			.sharedBoundsWrapper(
				key = SharedElementTransitionKeys.leDeviceCardToLeDeviceProfile(deviceAddress)
			)
	) { scPadding ->
		BLEDeviceScreenContent(
			profile = profile,
			onCharacteristicSelect = { service, characteristics ->
				val event = BLECharacteristicEvent.OnSelectCharacteristic(service, characteristics)
				onSelectEvent(event)
			},
			selectedCharacteristic = selectedCharacteristic.characteristic,
			contentPadding = PaddingValues(
				top = scPadding.calculateTopPadding() + dimensionResource(R.dimen.sc_padding_secondary),
				bottom = scPadding.calculateBottomPadding() + dimensionResource(R.dimen.sc_padding_secondary),
				start = scPadding.calculateStartPadding(layoutDirection) + dimensionResource(R.dimen.sc_padding),
				end = scPadding.calculateEndPadding(layoutDirection) + dimensionResource(R.dimen.sc_padding)
			),
			modifier = Modifier.fillMaxSize(),
		)
	}
}


private class BTLEDevicesProfilePreviewParams
	: CollectionPreviewParameterProvider<BLEDeviceProfileState>(
	listOf(
		PreviewFakes.FAKE_BLE_PROFILE_STATE,
		PreviewFakes.FAKE_BLE_PROFILE_STATE_CONNECTING
	)
)

@PreviewLightDark
@Composable
private fun BLEDevicesRoutePreview(
	@PreviewParameter(BTLEDevicesProfilePreviewParams::class)
	profile: BLEDeviceProfileState,
) = BlueToothTerminalAppTheme {
	BLEDeviceRoute(
		deviceAddress = PreviewFakes.FAKE_DEVICE_MODEL.address,
		profile = profile,
		selectedCharacteristic = SelectedCharacteristicState(),
		onSelectEvent = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = "Back",
				modifier = Modifier.padding(ButtonDefaults.ContentPadding)
			)
		},
	)
}