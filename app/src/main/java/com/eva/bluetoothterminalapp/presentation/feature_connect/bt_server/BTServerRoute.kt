package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.ServerConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_client.composables.BTMessageText
import com.eva.bluetoothterminalapp.presentation.feature_client.composables.EndConnectionDialog
import com.eva.bluetoothterminalapp.presentation.feature_client.composables.SendCommandTextField
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.ServerConnectionStateCard
import com.eva.bluetoothterminalapp.presentation.feature_connect.state.BTServerRouteEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.state.BTServerRouteState
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BTServerRoute(
	state: BTServerRouteState,
	onEvent: (BTServerRouteEvents) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
) {

	val snackBarHostState = LocalSnackBarProvider.current

	val isConnectionAccepted by remember(state.connectionMode) {
		derivedStateOf {
			state.connectionMode == ServerConnectionState.CONNECTION_ACCEPTED
		}
	}

	EndConnectionDialog(
		showDialog = state.showDisconnectDialog,
		onConfirm = { },
		onDismiss = { },
	)

	BackHandler(
		enabled = isConnectionAccepted,
		onBack = { },
	)

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = "Server") },
				navigationIcon = navigation,
				actions = {
					AnimatedVisibility(
						visible = isConnectionAccepted,
						enter = slideInVertically(),
						exit = slideOutVertically()
					) {
						TextButton(onClick = {}) {
							Text(text = "Stop Server")
						}
					}
				},
			)
		},
		snackbarHost = { SnackbarHost(snackBarHostState) },
		modifier = modifier
	) { scPadding ->
		Column(
			modifier = Modifier
				.padding(scPadding)
				.padding(horizontal = dimensionResource(id = R.dimen.sc_padding))
				.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			ServerConnectionStateCard(
				connectionState = state.connectionMode,
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
				color = MaterialTheme.colorScheme.outlineVariant,
				modifier = Modifier.fillMaxWidth()
			)
			SendCommandTextField(
				value = state.textFieldValue,
				isEnable = isConnectionAccepted,
				onChange = { value -> onEvent(BTServerRouteEvents.OnTextFieldValue(value)) },
				onImeAction = { onEvent(BTServerRouteEvents.OnSendEvents) },
				modifier = modifier
					.fillMaxWidth()
					.navigationBarsPadding()
			)
		}
	}
}