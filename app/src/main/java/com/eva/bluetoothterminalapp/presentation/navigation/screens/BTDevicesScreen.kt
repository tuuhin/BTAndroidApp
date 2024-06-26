package com.eva.bluetoothterminalapp.presentation.navigation.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.composables.BTAppNavigationDrawer
import com.eva.bluetoothterminalapp.presentation.feature_devices.BTDeviceViewmodel
import com.eva.bluetoothterminalapp.presentation.feature_devices.BTDevicesRoute
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.toArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.navigation.screens.destinations.BTDeviceProfileScreenDestination
import com.eva.bluetoothterminalapp.presentation.navigation.screens.destinations.BTLEClientScreenDestination
import com.eva.bluetoothterminalapp.presentation.navigation.screens.destinations.BTServerScreenDestination
import com.eva.bluetoothterminalapp.presentation.navigation.screens.destinations.InformationScreenDestination
import com.eva.bluetoothterminalapp.presentation.navigation.screens.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@RootNavGraph(start = true)
@Destination(
	route = Routes.DEVICES_ROUTE,
	style = RouteAnimation::class
)
@Composable
fun BTDevicesScreen(
	navigator: DestinationsNavigator
) {
	val viewModel = koinViewModel<BTDeviceViewmodel>()

	val state by viewModel.screenState.collectAsStateWithLifecycle()
	val isBTActive by viewModel.isBTActive.collectAsStateWithLifecycle()
	val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()

	UIEventsSideEffect(
		viewModel = viewModel,
		navigator = navigator
	)

	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val scope = rememberCoroutineScope()

	val onShowDrawer: () -> Unit = remember {
		{
			scope.launch {
				drawerState.open()
			}
		}
	}

	ModalNavigationDrawer(
		drawerState = drawerState,
		gesturesEnabled = true,
		drawerContent = {
			BTAppNavigationDrawer(
				modifier = Modifier.fillMaxWidth(.7f),
				onNavigateToFeedBackRoute = { navigator.navigate(InformationScreenDestination) },
				onNavigateToSettingsRoute = { navigator.navigate(SettingsScreenDestination) },
				onNavigateToClassicServer = { navigator.navigate(BTServerScreenDestination) }
			)
		},
	) {
		BTDevicesRoute(
			state = state,
			isBTActive = isBTActive,
			isScanning = isScanning,
			onEvent = viewModel::onEvents,
			onSelectDevice = { device ->
				val args = device.toArgs()
				navigator.navigate(BTDeviceProfileScreenDestination(args), onlyIfResumed = true)
			},
			onSelectLeDevice = { device ->
				val args = device.toArgs()
				navigator.navigate(BTLEClientScreenDestination(args), onlyIfResumed = true)
			},
			navigation = {
				IconButton(onClick = onShowDrawer) {
					Icon(
						imageVector = Icons.Default.Menu,
						contentDescription = stringResource(id = R.string.menu_option_more)
					)
				}
			},
		)
	}
}