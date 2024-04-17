package com.eva.bluetoothterminalapp.presentation.navigation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.BTClientRoute
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.BTClientViewModel
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.ConnectionRouteArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination(
	route = Routes.CLIENT_CONNECTION_ROUTE,
	style = RouteAnimation::class,
	navArgsDelegate = ConnectionRouteArgs::class
)
@Composable
fun BTClientScreen(
	navigator: DestinationsNavigator,
) {
	val viewModel = koinViewModel<BTClientViewModel>()
	val isConnected by viewModel.clientState.collectAsStateWithLifecycle()
	val showCloseDialog by viewModel.showCloseDialog.collectAsStateWithLifecycle()
	val connectionType by viewModel.connectionType.collectAsStateWithLifecycle()

	UIEventsSideEffect(
		viewModel = viewModel,
		navigator = navigator
	)

	BTClientRoute(
		state = isConnected,
		connectTypeState = connectionType,
		showCloseDialog = showCloseDialog,
		onConnectionEvent = viewModel::onClientConnectionEvents,
		onCloseEvent = viewModel::onCloseConnectionEvent,
		onStartEvent = viewModel::onStartConnectionEvents,
		navigation = {
			IconButton(onClick = navigator::popBackStack) {
				Icon(
					imageVector = Icons.AutoMirrored.Default.ArrowBack,
					contentDescription = "Arrow Back"
				)
			}
		},
	)
}