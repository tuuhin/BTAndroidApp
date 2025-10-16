package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_le

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_le_server.BLEServerRoute
import com.eva.bluetoothterminalapp.presentation.feature_le_server.BLEServerViewModel
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.util.LocalSharedTransitionVisibilityScopeProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>(
	route = Routes.BLE_SERVER_ROUTE,
	style = RouteAnimation::class,
)
@Composable
fun AnimatedVisibilityScope.BLEServerScreen(
	navigator: DestinationsNavigator
) {

	val viewModel = koinViewModel<BLEServerViewModel>()
	val connectedClients by viewModel.connectedClients.collectAsStateWithLifecycle()
	val services by viewModel.serverServices.collectAsStateWithLifecycle()
	val isServerRunning by viewModel.isServerRunning.collectAsStateWithLifecycle()
	val showServerRunningDialog by viewModel.showServerRunningDialog.collectAsStateWithLifecycle()

	UIEventsSideEffect(
		events = { viewModel.uiEvents },
		onPopBack = dropUnlessResumed { navigator.popBackStack() }
	)

	CompositionLocalProvider(LocalSharedTransitionVisibilityScopeProvider provides this) {
		BLEServerRoute(
			connectedClients = connectedClients,
			serverServices = services,
			isServerRunning = isServerRunning,
			showConnectionCloseDialog = showServerRunningDialog,
			onEvent = viewModel::onEvent,
			navigation = {
				val onBack = dropUnlessResumed { navigator.popBackStack() }
				IconButton(onClick = onBack) {
					Icon(
						imageVector = Icons.AutoMirrored.Default.ArrowBack,
						contentDescription = stringResource(id = R.string.back_arrow)
					)
				}
			},
		)
	}
}