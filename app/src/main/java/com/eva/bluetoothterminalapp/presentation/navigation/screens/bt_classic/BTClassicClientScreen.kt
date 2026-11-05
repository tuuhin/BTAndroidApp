package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_classic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.BTClientRoute
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.BTClientViewModel
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables.CloseConnectionDialog
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.EndConnectionEvents
import com.eva.bluetoothterminalapp.presentation.navigation.NavDestinationBuilder
import com.eva.bluetoothterminalapp.presentation.navigation.NavRoutes
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.util.SharedElementTransitionKeys
import com.eva.bluetoothterminalapp.presentation.util.sharedBoundsWrapper
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Single
import org.koin.core.parameter.parametersOf

@Single(binds = [NavDestinationBuilder::class])
class BTClassicClientScreen : NavDestinationBuilder {

	override fun EntryProviderScope<NavKey>.navEntry(backStack: NavBackStack<NavKey>) =
		entry<NavRoutes.ClassicClientRoute> { route ->

			val viewModel =
				koinViewModel<BTClientViewModel> { parametersOf(route.address, route.uuid) }
			val messagesState by viewModel.messagesState.collectAsStateWithLifecycle()
			val deviceState by viewModel.clientState.collectAsStateWithLifecycle()
			val showCloseDialog by viewModel.showCloseDialog.collectAsStateWithLifecycle()
			val btSettings by viewModel.btSettings.collectAsStateWithLifecycle()

			UIEventsSideEffect(
				events = { viewModel.uiEvents },
				onPopBack = dropUnlessResumed { backStack.removeLastOrNull() }
			)

			CloseConnectionDialog(
				showDialog = showCloseDialog,
				onEvent = viewModel::onCloseConnectionEvent
			)

			BTClientRoute(
				messages = messagesState,
				device = deviceState,
				btSettings = btSettings,
				onConnectionEvent = viewModel::onClientConnectionEvents,
				onBackPress = { viewModel.onCloseConnectionEvent(EndConnectionEvents.OnOpenDisconnectDialog) },
				navigation = {
					val onBack = dropUnlessResumed {
						// open close dialog if connection running
						if (deviceState.isConnected)
							viewModel.onCloseConnectionEvent(EndConnectionEvents.OnOpenDisconnectDialog)
						// else pop backstack
						else backStack.removeLastOrNull()
					}
					IconButton(onClick = onBack) {
						Icon(
							imageVector = Icons.AutoMirrored.Default.ArrowBack,
							contentDescription = stringResource(id = R.string.back_arrow)
						)
					}
				},
				modifier = Modifier.sharedBoundsWrapper(
					SharedElementTransitionKeys.btClientScreen(route.address)
				)
			)
		}
}
