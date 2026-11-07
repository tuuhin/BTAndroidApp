package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_le

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors
import com.eva.bluetoothterminalapp.presentation.feature_le_server.BLEServerRoute
import com.eva.bluetoothterminalapp.presentation.feature_le_server.BLEServerViewModel
import com.eva.bluetoothterminalapp.presentation.feature_le_server.state.BLEServerScreenEvents
import com.eva.bluetoothterminalapp.presentation.navigation.NavDestinationBuilder
import com.eva.bluetoothterminalapp.presentation.navigation.NavRoutes
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Single

@Single(binds = [NavDestinationBuilder::class])
class BLEServerScreen : NavDestinationBuilder {

	override fun EntryProviderScope<NavKey>.navEntry(backStack: NavBackStack<NavKey>) =
		entry<NavRoutes.BLEServerRoute> {

			val viewModel = koinViewModel<BLEServerViewModel>()
			val connectedClients by viewModel.connectedClients.collectAsStateWithLifecycle()
			val services by viewModel.connectedServices.collectAsStateWithLifecycle()
			val screenState by viewModel.screenState.collectAsStateWithLifecycle()

			UIEventsSideEffect(
				events = { viewModel.uiEvents },
				onPopBack = dropUnlessResumed { backStack.removeLastOrNull() }
			)

			BLEServerRoute(
				connectedClients = connectedClients,
				connectedServices = services,
				isServerRunning = screenState.isServerRunning,
				showConnectionCloseDialog = screenState.showServerRunningDialog,
				showServiceOptionSelector = screenState.showServiceSelector,
				serverServicesOptions = screenState.serverServices,
				onEvent = viewModel::onEvent,
				navigation = {
					val onBack = dropUnlessResumed {
						if (screenState.isServerRunning)
							viewModel.onEvent(BLEServerScreenEvents.OnShowServerRunningDialog)
						else backStack.removeLastOrNull()
					}
					IconButton(onClick = onBack) {
						Icon(
							imageVector = AppIconVectors.backArrow,
							contentDescription = stringResource(id = R.string.back_arrow)
						)
					}
				},
			)
		}
}
