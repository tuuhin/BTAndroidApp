package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_classic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.BTServerRoute
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.BTServerViewModel
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables.CloseServerConnectionDialog
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerEvents
import com.eva.bluetoothterminalapp.presentation.navigation.NavDestinationBuilder
import com.eva.bluetoothterminalapp.presentation.navigation.NavRoutes
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Single

@Single(binds = [NavDestinationBuilder::class])
class BTServerScreen : NavDestinationBuilder {

	override fun EntryProviderScope<NavKey>.navEntry(backStack: NavBackStack<NavKey>) =
		entry<NavRoutes.ClassicServerRoute> {
			val viewModel = koinViewModel<BTServerViewModel>()
			val state by viewModel.serverState.collectAsStateWithLifecycle()
			val settings by viewModel.btSettings.collectAsStateWithLifecycle()
			val device by viewModel.connectedDevice.collectAsStateWithLifecycle()

			UIEventsSideEffect(
				events = { viewModel.uiEvents },
				onPopBack = dropUnlessResumed { backStack.removeLastOrNull() }
			)

			CloseServerConnectionDialog(
				showDialog = state.showExitDialog,
				onEvent = viewModel::onEvent
			)

			BTServerRoute(
				deviceState = device,
				messagesState = state,
				btSettings = settings,
				onEvent = viewModel::onEvent,
				navigation = {
					val onBack = dropUnlessResumed {
						if (device.isRunning) viewModel.onEvent(BTServerEvents.OnOpenDisconnectDialog)
						else backStack.removeLastOrNull()
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

}
