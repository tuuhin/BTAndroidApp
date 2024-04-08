package com.eva.bluetoothterminalapp.presentation.navigation.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.eva.bluetoothterminalapp.presentation.feature_devices.BTDeviceViewmodel
import com.eva.bluetoothterminalapp.presentation.feature_devices.BTDevicesRoute
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
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
	navController: DestinationsNavigator
) {

	val lifecyleOwner = LocalLifecycleOwner.current
	val snackBarHostState = LocalSnackBarProvider.current
	val context = LocalContext.current

	val viewModel = koinViewModel<BTDeviceViewmodel>()

	val state by viewModel.screenState.collectAsStateWithLifecycle()

	LaunchedEffect(lifecyleOwner) {
		lifecyleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			viewModel.uiEvents.collect { event ->
				when (event) {
					is UiEvents.ShowSnackBar -> snackBarHostState
						.showSnackbar(message = event.message)

					is UiEvents.ShowToast -> Toast
						.makeText(context, event.message, Toast.LENGTH_SHORT)
						.show()
				}
			}
		}
	}

	BTDevicesRoute(
		state = state,
		onEvent = viewModel::onEvents,
		onSelectDevice = { device ->

		},
		onConnectAsServer = {

		},
	)
}