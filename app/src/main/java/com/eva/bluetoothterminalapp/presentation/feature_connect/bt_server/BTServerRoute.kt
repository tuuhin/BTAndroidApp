package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.BTServerTopAppBar
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.ServerConnectionStateChip
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerRouteState
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.BTMessagesList
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.SendCommandTextField
import com.eva.bluetoothterminalapp.presentation.feature_connect.util.KeepScreenOnEffect
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalLayoutApi::class,
)
@Composable
fun BTServerRoute(
	state: BTServerRouteState,
	btSettings: BTSettingsModel,
	onEvent: (BTServerEvents) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
) {
	val snackBarHostState = LocalSnackBarProvider.current
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	KeepScreenOnEffect(
		connectionState = state.connectionMode,
		isKeepScreenOn = btSettings.keepScreenOnWhenConnected
	)


	Scaffold(
		topBar = {
			BTServerTopAppBar(
				connectionState = state.connectionMode,
				onStop = { onEvent(BTServerEvents.OnStopServer) },
				onRestart = { onEvent(BTServerEvents.OnRestartServer) },
				navigation = navigation,
				scrollBehavior = scrollBehavior,
			)
		},
		snackbarHost = { SnackbarHost(snackBarHostState) },
		modifier = modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection)
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
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f)
					.clip(MaterialTheme.shapes.medium)
					.background(MaterialTheme.colorScheme.surfaceContainer),
			) {
				ServerConnectionStateChip(
					connectionState = state.connectionMode,
					modifier = Modifier
						.offset(y = dimensionResource(id = R.dimen.connection_chip_offset))
						.align(Alignment.TopCenter)
						.zIndex(1f)
				)
				BTMessagesList(
					messages = state.messages,
					scrollToEnd = btSettings.autoScrollEnabled,
					showTimeInMessage = btSettings.showTimeStamp,
					contentPadding = PaddingValues(
						horizontal = dimensionResource(id = R.dimen.messages_list_horizontal_padding),
						vertical = dimensionResource(id = R.dimen.messages_list_vertical_padding)
					),
					modifier = Modifier.fillMaxSize()
				)
			}
			HorizontalDivider(
				color = MaterialTheme.colorScheme.outlineVariant,
				modifier = Modifier.padding(
					vertical = dimensionResource(id = R.dimen.messages_text_field_spacing)
				)
			)
			SendCommandTextField(
				value = state.textFieldValue,
				isEnable = state.connectionMode == ServerConnectionState.CONNECTION_ACCEPTED,
				onChange = { value -> onEvent(BTServerEvents.OnTextFieldValue(value)) },
				onImeAction = { onEvent(BTServerEvents.OnSendEvents) },
				modifier = Modifier
					.windowInsetsPadding(WindowInsets.navigationBars)
					.fillMaxWidth()
			)
		}
	}
}

private class BTServerRoutePreviewParams : CollectionPreviewParameterProvider<BTServerRouteState>(
	listOf(
		BTServerRouteState(
			connectionMode = ServerConnectionState.CONNECTION_ACCEPTED,
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
	)
)

@PreviewLightDark
@Composable
private fun BTServerRoutePreview(
	@PreviewParameter(BTServerRoutePreviewParams::class)
	state: BTServerRouteState
) = BlueToothTerminalAppTheme {
	BTServerRoute(
		state = state,
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