package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.PeerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.BTPeerInteractionContent
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.BTServerTopAppBar
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.StartServerBox
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerDeviceState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerScreenState
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.KeepScreenOnSideEffect
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.presentation.util.SharedElementTransitionKeys
import com.eva.bluetoothterminalapp.presentation.util.sharedBoundsWrapper
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalLayoutApi::class,
	ExperimentalSharedTransitionApi::class,
)
@Composable
fun BTServerRoute(
	deviceState: BTServerDeviceState,
	messagesState: BTServerScreenState,
	btSettings: BTSettingsModel,
	onEvent: (BTServerEvents) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
) {
	val snackBarHostState = LocalSnackBarProvider.current
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	KeepScreenOnSideEffect(
		connectionState = deviceState.serverState,
		isKeepScreenOn = btSettings.keepScreenOnWhenConnected
	)

	val isConnected by remember(deviceState.serverState) {
		derivedStateOf { deviceState.isRunning }
	}

	BackHandler(
		enabled = isConnected,
		onBack = { onEvent(BTServerEvents.OnOpenDisconnectDialog) },
	)

	Scaffold(
		topBar = {
			BTServerTopAppBar(
				serverState = deviceState.serverState,
				peerState = deviceState.peerState,
				onStop = { onEvent(BTServerEvents.OnStopServer) },
				onRestart = { onEvent(BTServerEvents.OnRestartServer) },
				navigation = navigation,
				scrollBehavior = scrollBehavior,
			)
		},
		snackbarHost = { SnackbarHost(snackBarHostState) },
		modifier = modifier
			.sharedBoundsWrapper(SharedElementTransitionKeys.CLASSIC_SERVER_ITEM_TO_SERVER)
			.nestedScroll(scrollBehavior.nestedScrollConnection)
			.imeNestedScroll()
	) { scPadding ->
		AnimatedContent(
			targetState = messagesState.showServerTerminal,
			modifier = Modifier
				.padding(top = scPadding.calculateTopPadding())
				.padding(
					horizontal = dimensionResource(id = R.dimen.sc_padding),
					vertical = dimensionResource(id = R.dimen.sc_padding_secondary)
				)
				.fillMaxSize()
				.imePadding(),
		) { show ->
			if (show) {
				BTPeerInteractionContent(
					deviceState = deviceState,
					messagesState = messagesState,
					btSettings = btSettings,
					onEvent = onEvent
				)
			} else {
				StartServerBox(
					onStartServer = { onEvent(BTServerEvents.OnStartServer) },
					modifier = Modifier.fillMaxSize()
				)
			}
		}
	}
}


@PreviewLightDark
@Composable
private fun BTServerRoutePreview() = BlueToothTerminalAppTheme {
	BTServerRoute(
		messagesState = BTServerScreenState(
			messages = persistentListOf(
				BluetoothMessage(
					message = "Hello",
					type = BluetoothMessageType.MESSAGE_FROM_SELF
				),
				BluetoothMessage(
					message = "Hi",
					type = BluetoothMessageType.MESSAGE_FROM_OTHER,
				)
			)
		),
		deviceState = BTServerDeviceState(
			serverState = ServerConnectionState.PEER_CONNECTION_ACCEPTED,
			peerState = PeerConnectionState.PEER_CONNECTED,
			device = PreviewFakes.FAKE_DEVICE_MODEL
		),
		btSettings = BTSettingsModel(),
		onEvent = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		},
	)
}