package com.eva.bluetoothterminalapp.presentation.navigation.screens

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.eva.bluetoothterminalapp.presentation.feature_client.BTClientRoute
import com.eva.bluetoothterminalapp.presentation.feature_client.BTClientViewModel
import com.eva.bluetoothterminalapp.presentation.navigation.args.ConnectionRouteArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
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

	val context = LocalContext.current
	val lifecyleOwner = LocalLifecycleOwner.current
	val snackBarHostState = LocalSnackBarProvider.current

	val viewModel = koinViewModel<BTClientViewModel>()

	val isConnected by viewModel.clientState.collectAsStateWithLifecycle()

	LaunchedEffect(lifecyleOwner, viewModel) {
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

	BTClientRoute(
		state = isConnected,
		onEvent = viewModel::onEvent,
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