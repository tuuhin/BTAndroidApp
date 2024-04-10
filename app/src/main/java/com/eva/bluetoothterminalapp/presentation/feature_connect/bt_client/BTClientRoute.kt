package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_client.composables.BTClientTopBar
import com.eva.bluetoothterminalapp.presentation.feature_client.composables.BTMessageText
import com.eva.bluetoothterminalapp.presentation.feature_client.composables.ClientConnectionStateCard
import com.eva.bluetoothterminalapp.presentation.feature_client.composables.EndConnectionDialog
import com.eva.bluetoothterminalapp.presentation.feature_client.composables.SendCommandTextField
import com.eva.bluetoothterminalapp.presentation.feature_connect.state.BTClientRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.state.BTClientRouteState
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BTClientRoute(
	state: BTClientRouteState,
	onEvent: (BTClientRouteEvents) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
) {
	val snackBarHostState = LocalSnackBarProvider.current

	val isStatusAccepted by remember(state.connectionMode) {
		derivedStateOf { state.connectionMode == ClientConnectionState.CONNECTION_ACCEPTED }
	}

	EndConnectionDialog(
		showDialog = state.showDisconnectDialog,
		onConfirm = { onEvent(BTClientRouteEvents.DisConnectClient) },
		onDismiss = { onEvent(BTClientRouteEvents.CloseDisconnectDialog) },
	)

	BackHandler(
		enabled = isStatusAccepted,
		onBack = { onEvent(BTClientRouteEvents.OpenDisconnectDialog) },
	)

	Scaffold(
		topBar = {
			BTClientTopBar(
				connectionState = state.connectionMode,
				navigation = navigation,
				onConnect = { onEvent(BTClientRouteEvents.ConnectClient) },
				onDisconnect = { onEvent(BTClientRouteEvents.DisConnectClient) },
				onClear = { onEvent(BTClientRouteEvents.ClearTerminal) },
			)
		},
		snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
		modifier = modifier
	) { scPadding ->
		Column(
			modifier = Modifier
				.padding(scPadding)
				.padding(horizontal = dimensionResource(id = R.dimen.sc_padding))
				.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			ClientConnectionStateCard(
				clientState = state.connectionMode,
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.fillMaxWidth(.75f)
			)
			LazyColumn(
				verticalArrangement = Arrangement.Bottom,
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f)
					.imeNestedScroll(),
			) {
				itemsIndexed(
					items = state.messages,
					key = { _, message -> message.logTime.toEpochMilliseconds() }
				) { _, message ->
					BTMessageText(
						message = message,
						modifier = Modifier.padding(vertical = 2.dp)
					)
				}
			}
			HorizontalDivider(
				thickness = 1.dp,
				modifier = Modifier.fillMaxWidth()
			)
			SendCommandTextField(
				value = state.textFieldValue,
				isEnable = isStatusAccepted,
				onChange = { onEvent(BTClientRouteEvents.OnTextFieldValue(it)) },
				onImeAction = { onEvent(BTClientRouteEvents.OnSendEvents) },
				modifier = modifier
					.fillMaxWidth()
					.imePadding()
			)
		}
	}
}