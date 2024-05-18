package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_classic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.BluetoothProfileRoute
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.BluetoothProfileViewModel
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.navigation.screens.destinations.BTClassicClientScreenDestination
import com.eva.bluetoothterminalapp.presentation.navigation.screens.destinations.BTDeviceProfileScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import org.koin.androidx.compose.koinViewModel

@Destination(
	route = Routes.CLIENT_PROFILE_ROUTE,
	style = RouteAnimation::class,
	navArgsDelegate = BluetoothDeviceArgs::class
)
@Composable
fun BTDeviceProfileScreen(
	navigator: DestinationsNavigator,
	args: BluetoothDeviceArgs,
) {

	val viewmodel = koinViewModel<BluetoothProfileViewModel>()
	val profile by viewmodel.profile.collectAsStateWithLifecycle()

	UIEventsSideEffect(
		viewModel = viewmodel,
		navigator = navigator
	)

	BluetoothProfileRoute(
		state = profile,
		onEvent = viewmodel::onEvent,
		onConnect = { uuid ->
			navigator.navigate(
				direction = BTClassicClientScreenDestination(address = args.address, uuid = uuid),
				onlyIfResumed = true
			) {
				// pop this scrren as its not required any more
				popUpTo(BTDeviceProfileScreenDestination) {
					inclusive = true
				}
			}
		},
		navigation = {
			val onBack = dropUnlessResumed(block = navigator::popBackStack)
			IconButton(onClick = onBack) {
				Icon(
					imageVector = Icons.AutoMirrored.Default.ArrowBack,
					contentDescription = stringResource(id = R.string.back_arrow)
				)
			}
		}
	)
}