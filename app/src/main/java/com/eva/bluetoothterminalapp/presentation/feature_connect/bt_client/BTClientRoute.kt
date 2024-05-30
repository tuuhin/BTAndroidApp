package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables.BTClientTopBar
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables.BTMessageListWithConnectionBanner
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util.BTClientRoutePreviewParams
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.SendCommandTextField
import com.eva.bluetoothterminalapp.presentation.feature_connect.util.KeepScreenOnEffect
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(
	ExperimentalLayoutApi::class,
	ExperimentalMaterial3Api::class
)
@Composable
fun BTClientRoute(
	state: BTClientRouteState,
	btSettings: BTSettingsModel,
	onConnectionEvent: (BTClientRouteEvents) -> Unit,
	modifier: Modifier = Modifier,
	onBackPress: () -> Unit = {},
	navigation: @Composable () -> Unit = {},
) {
	val snackBarHostState = LocalSnackBarProvider.current

	val isStatusAccepted by remember(state.connectionMode) {
		derivedStateOf { state.connectionMode == ClientConnectionState.CONNECTION_ACCEPTED }
	}

	KeepScreenOnEffect(
		connectionState = state.connectionMode,
		isKeepScreenOn = btSettings.keepScreenOnWhenConnected
	)

	BackHandler(
		enabled = isStatusAccepted,
		onBack = onBackPress,
	)

	val topScrollState = TopAppBarDefaults.pinnedScrollBehavior()

	Scaffold(
		topBar = {
			BTClientTopBar(
				clientState = state.connectionMode,
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
				.padding(top = scPadding.calculateTopPadding())
				.padding(
					horizontal = dimensionResource(id = R.dimen.sc_padding),
					vertical = dimensionResource(id = R.dimen.sc_padding_secondary)
				)
				.fillMaxSize()
				.imePadding(),
		) {
			BTMessageListWithConnectionBanner(
				connectionState = state.connectionMode,
				messages = state.messages,
				scrollToEnd = btSettings.autoScrollEnabled,
				showTimestamps = btSettings.showTimeStamp,
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f)
			)
			HorizontalDivider(
				color = MaterialTheme.colorScheme.outlineVariant,
				modifier = Modifier.padding(
					vertical = dimensionResource(id = R.dimen.messages_text_field_spacing)
				)
			)
			SendCommandTextField(
				value = state.textFieldValue,
				isEnable = isStatusAccepted,
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
private fun BTClientRoutePreview(
	@PreviewParameter(BTClientRoutePreviewParams::class)
	state: BTClientRouteState
) = BlueToothTerminalAppTheme {
	BTClientRoute(
		state = state,
		btSettings = BTSettingsModel(),
		onConnectionEvent = {},
		onBackPress = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		},
	)
}