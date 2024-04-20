package com.eva.bluetoothterminalapp.presentation.navigation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eva.bluetoothterminalapp.presentation.feature_devices.BTDeviceViewmodel
import com.eva.bluetoothterminalapp.presentation.feature_devices.BTDevicesRoute
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.toArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.navigation.screens.destinations.BTClientScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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

	BTDevicesRoute(
		state = state,
		isBTActive = isBTActive,
		isScanning = isScanning,
		onEvent = viewModel::onEvents,
		onSelectDevice = { device ->
			val args = device.toArgs()
			navigator.navigate(BTClientScreenDestination(args), onlyIfResumed = true)
		},
	)
}