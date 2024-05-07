package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_classic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.BTClientRoute
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.BTClientViewModel
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables.CloseConnectionDialog
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.EndConnectionEvents
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothClientConnectArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination(
	route = Routes.CLIENT_CONNECTION_ROUTE,
	style = RouteAnimation::class,
	navArgsDelegate = BluetoothClientConnectArgs::class
)
@Composable
fun BTClassicClientScreen(
	navigator: DestinationsNavigator,
) {
	val viewModel = koinViewModel<BTClientViewModel>()
	val clientState by viewModel.clientState.collectAsStateWithLifecycle()
	val showCloseDialog by viewModel.showCloseDialog.collectAsStateWithLifecycle()

	//SideEffect
	UIEventsSideEffect(
		viewModel = viewModel,
		navigator = navigator
	)
	//close dialog when leaving
	CloseConnectionDialog(
		showDialog = showCloseDialog,
		onEvent = viewModel::onCloseConnectionEvent
	)
	// actual route
	BTClientRoute(
		state = clientState,
		onConnectionEvent = viewModel::onClientConnectionEvents,
		onBackPress = { viewModel.onCloseConnectionEvent(EndConnectionEvents.OnOpenDisconnectDialog) },
		navigation = {
			IconButton(
				onClick = {
					// open close dialog if connection running
					if (clientState.connectionMode == ClientConnectionState.CONNECTION_ACCEPTED)
						viewModel.onCloseConnectionEvent(EndConnectionEvents.OnOpenDisconnectDialog)
					// else pop backstack
					else navigator.popBackStack()
				},
			) {
				Icon(
					imageVector = Icons.AutoMirrored.Default.ArrowBack,
					contentDescription = stringResource(id = R.string.back_arrow)
				)
			}
		},
	)
}