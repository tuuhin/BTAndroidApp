package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables.BTClientTopBar
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientDeviceState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientMessagesState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util.BTClientDeviceStatePreviewParam
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util.BTClientMessagesStatePreviewParams
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.BTClientDeviceProfile
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.BTMessagesList
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.KeepScreenOnSideEffect
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.SendCommandTextField
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BTClientRoute(
	device: BTClientDeviceState,
	messages: BTClientMessagesState,
	btSettings: BTSettingsModel,
	onConnectionEvent: (BTClientRouteEvents) -> Unit,
	modifier: Modifier = Modifier,
	onBackPress: () -> Unit = {},
	navigation: @Composable () -> Unit = {},
) {
	val snackBarHostState = LocalSnackBarProvider.current
	val layoutDirection = LocalLayoutDirection.current

	val isConnected by remember(device.connectionStatus) {
		derivedStateOf { device.connectionStatus == ClientConnectionState.CONNECTION_CONNECTED }
	}

	KeepScreenOnSideEffect(
		connectionState = device.connectionStatus,
		isKeepScreenOn = btSettings.keepScreenOnWhenConnected
	)

	BackHandler(
		enabled = isConnected,
		onBack = onBackPress,
	)

	val topScrollState = TopAppBarDefaults.enterAlwaysScrollBehavior()

	Scaffold(
		topBar = {
			BTClientTopBar(
				clientState = device.connectionStatus,
				navigation = navigation,
				onReconnect = { onConnectionEvent(BTClientRouteEvents.OnReconnectClient) },
				onDisconnect = { onConnectionEvent(BTClientRouteEvents.OnDisconnectClient) },
				scrollBehavior = topScrollState,
			)
		},
		snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
		modifier = modifier
			.nestedScroll(topScrollState.nestedScrollConnection)
			.imeNestedScroll()
	) { scPadding ->
		Column(
			modifier = Modifier
				.padding(
					top = scPadding.calculateTopPadding() + dimensionResource(R.dimen.sc_padding_secondary),
					bottom = scPadding.calculateBottomPadding() + dimensionResource(R.dimen.sc_padding_secondary),
					start = scPadding.calculateStartPadding(layoutDirection) + dimensionResource(R.dimen.sc_padding),
					end = scPadding.calculateEndPadding(layoutDirection) + dimensionResource(R.dimen.sc_padding)
				)
				.imePadding()
				.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(10.dp)
		) {
			BTClientDeviceProfile(
				device = device.device,
				connectionState = device.connectionStatus,
				shape = MaterialTheme.shapes.large
			)
			BTMessagesList(
				messages = messages.messages,
				scrollToEnd = btSettings.autoScrollEnabled,
				showTimeInMessage = btSettings.showTimeStamp,
				contentPadding = PaddingValues(
					horizontal = dimensionResource(id = R.dimen.messages_list_horizontal_padding),
					vertical = dimensionResource(id = R.dimen.messages_list_vertical_padding)
				),
				shape = MaterialTheme.shapes.extraLarge,
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f)
			)
			SendCommandTextField(
				value = messages.textFieldValue,
				isEnable = isConnected,
				onChange = { onConnectionEvent(BTClientRouteEvents.OnSendFieldTextChanged(it)) },
				onImeAction = { onConnectionEvent(BTClientRouteEvents.OnSendEvents) },
				modifier = Modifier
					.fillMaxWidth()
					.windowInsetsPadding(WindowInsets.navigationBars)
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun BTClientRouteMessagesPreview(
	@PreviewParameter(BTClientMessagesStatePreviewParams::class)
	state: BTClientMessagesState,
) = BlueToothTerminalAppTheme {
	BTClientRoute(
		messages = state,
		device = BTClientDeviceState(
			connectionStatus = ClientConnectionState.CONNECTION_CONNECTED,
			device = PreviewFakes.FAKE_DEVICE_MODEL
		),
		btSettings = BTSettingsModel(),
		onConnectionEvent = {},
		onBackPress = {},
		navigation = {
			Icon(
				imageVector = AppIconVectors.backArrow,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		},
	)
}

@PreviewLightDark
@Composable
private fun BTClientRoutePreview(
	@PreviewParameter(BTClientDeviceStatePreviewParam::class)
	state: BTClientDeviceState,
) = BlueToothTerminalAppTheme {
	BTClientRoute(
		messages = BTClientMessagesState(),
		device = state,
		btSettings = BTSettingsModel(),
		onConnectionEvent = {},
		onBackPress = {},
		navigation = {
			Icon(
				imageVector = AppIconVectors.backArrow,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		},
	)
}