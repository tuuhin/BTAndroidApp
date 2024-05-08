package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.ServerConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.AnimatedStopAndRestartButton
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.ServerConnectionStateChip
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerRouteState
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.BTMessagesList
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.SendCommandTextField
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalLayoutApi::class
)
@Composable
fun BTServerRoute(
	state: BTServerRouteState,
	onEvent: (BTServerRouteEvents) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
) {
	val snackBarHostState = LocalSnackBarProvider.current
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	BackHandler(
		enabled = true,
		onBack = { onEvent(BTServerRouteEvents.OpenDisconnectDialog) },
	)


	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(id = R.string.bt_server_route)) },
				navigationIcon = navigation,
				actions = {
					AnimatedStopAndRestartButton(
						state = state.connectionMode,
						onRestart = { onEvent(BTServerRouteEvents.RestartServer) },
						onStop = { onEvent(BTServerRouteEvents.StopServer) },
					)
				},
				colors = TopAppBarDefaults
					.topAppBarColors(actionIconContentColor = MaterialTheme.colorScheme.primary)
			)
		},
		snackbarHost = { SnackbarHost(snackBarHostState) },
		modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
	) { scPadding ->
		Column(
			modifier = Modifier
				.padding(scPadding)
				.padding(horizontal = dimensionResource(id = R.dimen.sc_padding))
				.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			ServerConnectionStateChip(
				connectionState = state.connectionMode,
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.fillMaxWidth(.75f)
			)
			BTMessagesList(
				messages = state.messages,
				verticalArrangement = Arrangement.Bottom,
				contentPadding = PaddingValues(
					horizontal = dimensionResource(id = R.dimen.messages_list_horizontal_padding),
					vertical = dimensionResource(id = R.dimen.messages_list_vertical_padding)
				),
				modifier = Modifier.fillMaxSize()
			)
			HorizontalDivider(
				color = MaterialTheme.colorScheme.outlineVariant,
				modifier = Modifier.fillMaxWidth()
			)
			SendCommandTextField(
				value = state.textFieldValue,
				isEnable = state.connectionMode == ServerConnectionState.CONNECTION_ACCEPTED,
				onChange = { value -> onEvent(BTServerRouteEvents.OnTextFieldValue(value)) },
				onImeAction = { onEvent(BTServerRouteEvents.OnSendEvents) },
				modifier = Modifier
					.fillMaxWidth()
					.imePadding()
			)
		}
	}
}