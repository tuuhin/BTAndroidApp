package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables.BTClientTopBar
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables.ClientConnectionStateChip
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.BTClientRouteState
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.BTMessagesList
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.SendCommandTextField
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@OptIn(
	ExperimentalLayoutApi::class,
	ExperimentalMaterial3Api::class
)
@Composable
fun BTClientRoute(
	state: BTClientRouteState,
	onConnectionEvent: (BTClientRouteEvents) -> Unit,
	modifier: Modifier = Modifier,
	onBackPress: () -> Unit = {},
	navigation: @Composable () -> Unit = {},
) {
	val snackBarHostState = LocalSnackBarProvider.current

	val isStatusAccepted by remember(state.connectionMode) {
		derivedStateOf { state.connectionMode == ClientConnectionState.CONNECTION_ACCEPTED }
	}

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
		modifier = modifier.nestedScroll(topScrollState.nestedScrollConnection)
	) { scPadding ->
		Column(
			modifier = Modifier
				.padding(top = scPadding.calculateTopPadding())
				.padding(
					horizontal = dimensionResource(id = R.dimen.sc_padding),
					vertical = dimensionResource(id = R.dimen.sc_padding_secondary)
				)
				.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f)
					.clip(MaterialTheme.shapes.medium)
					.imeNestedScroll()
					.background(MaterialTheme.colorScheme.surfaceContainer),
			) {
				ClientConnectionStateChip(
					connectionState = state.connectionMode,
					modifier = Modifier
						.offset(y = dimensionResource(id = R.dimen.connection_chip_offset))
						.align(Alignment.TopCenter)
						.zIndex(1f)
				)
				BTMessagesList(
					messages = state.messages,
					verticalArrangement = Arrangement.Bottom,
					contentPadding = PaddingValues(horizontal = 4.dp),
					modifier = Modifier.fillMaxSize()
				)
			}
			HorizontalDivider(
				color = MaterialTheme.colorScheme.outlineVariant,
				modifier = Modifier.fillMaxWidth()
			)
			SendCommandTextField(
				value = state.textFieldValue,
				isEnable = isStatusAccepted,
				onChange = { onConnectionEvent(BTClientRouteEvents.OnSendFieldTextChanged(it)) },
				onImeAction = { onConnectionEvent(BTClientRouteEvents.OnSendEvents) },
				modifier = Modifier
					.imePadding()
					.windowInsetsPadding(WindowInsets.navigationBars)
					.fillMaxWidth()
			)
		}
	}
}

private class BTClientRoutePreviewParams : CollectionPreviewParameterProvider<BTClientRouteState>(
	listOf(
		BTClientRouteState(
			connectionMode = ClientConnectionState.CONNECTION_ACCEPTED,
			messages = persistentListOf(
				BluetoothMessage(
					message = "Hello",
					type = BluetoothMessageType.MESSAGE_FROM_CLIENT
				),
				BluetoothMessage(
					message = "Hi",
					type = BluetoothMessageType.MESSAGE_FROM_SERVER,
				)
			)
		),
		BTClientRouteState(
			connectionMode = ClientConnectionState.CONNECTION_DISCONNECTED,
			messages = persistentListOf(
				BluetoothMessage("Hello", BluetoothMessageType.MESSAGE_FROM_CLIENT)
			),
		),
		BTClientRouteState(
			connectionMode = ClientConnectionState.CONNECTION_ACCEPTED,
			messages = List(30) {
				BluetoothMessage("Hello", BluetoothMessageType.MESSAGE_FROM_CLIENT)
			}.toPersistentList(),
		)
	)
)

@PreviewLightDark
@Composable
private fun BTClientRoutePreview(
	@PreviewParameter(BTClientRoutePreviewParams::class)
	state: BTClientRouteState
) = BlueToothTerminalAppTheme {
	BTClientRoute(
		state = state,
		onConnectionEvent = {},
		onBackPress = {}
	)
}