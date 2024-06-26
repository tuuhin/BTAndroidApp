package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_classic

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.BTServerRoute
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.BTServerViewModel
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.CloseServerConnectionDialog
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.StartServerConnectionDailog
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerEvents
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination(
	route = Routes.SERVER_ROUTE,
	style = RouteAnimation::class
)
@Composable
fun BTServerScreen(
	navigator: DestinationsNavigator,
) {
	val viewModel = koinViewModel<BTServerViewModel>()
	val state by viewModel.state.collectAsStateWithLifecycle()
	val settings by viewModel.btSettings.collectAsStateWithLifecycle()

	UIEventsSideEffect(
		viewModel = viewModel,
		navigator = navigator
	)

	StartServerConnectionDailog(
		showDialog = state.showStartServerDialog,
		onEvent = viewModel::onEvent
	)

	CloseServerConnectionDialog(
		showDialog = state.showDisconnectDialog,
		onEvent = viewModel::onEvent
	)

	val isBackHandlerEnabled = remember(state.connectionMode) {
		state.connectionMode in arrayOf(
			ServerConnectionState.CONNECTION_ACCEPTED,
			ServerConnectionState.CONNECTION_LISTENING
		)
	}

	val rememberOnBackCallback: () -> Unit = remember {
		// the callback is remembered
		{ viewModel.onEvent(BTServerEvents.OnOpenDisconnectDialog) }
	}

	BackHandler(
		enabled = isBackHandlerEnabled,
		onBack = rememberOnBackCallback,
	)

	BTServerRoute(
		state = state,
		btSettings = settings,
		onEvent = viewModel::onEvent,
		navigation = {
			val onBack = dropUnlessResumed {
				if (isBackHandlerEnabled) rememberOnBackCallback()
				else navigator.popBackStack()
			}
			IconButton(onClick = onBack) {
				Icon(
					imageVector = Icons.AutoMirrored.Default.ArrowBack,
					contentDescription = stringResource(id = R.string.back_arrow)
				)
			}
		},
	)
}