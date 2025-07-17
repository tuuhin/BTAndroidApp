package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_classic

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
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.BluetoothProfileRoute
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.BluetoothProfileViewModel
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.util.LocalSharedTransitionVisibilityScopeProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.BtProfileDestination
import com.ramcosta.composedestinations.generated.destinations.ClientRouteDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>(
	route = Routes.CLIENT_PROFILE_ROUTE,
	style = RouteAnimation::class,
	navArgs = BluetoothDeviceArgs::class
)
@Composable
fun AnimatedVisibilityScope.BTDeviceProfileScreen(
	navigator: DestinationsNavigator,
	args: BluetoothDeviceArgs,
) {

	val viewmodel = koinViewModel<BluetoothProfileViewModel>()
	val profile by viewmodel.profile.collectAsStateWithLifecycle()

	UIEventsSideEffect(
		events = { viewmodel.uiEvents },
		onPopBack = dropUnlessResumed { navigator.popBackStack() }
	)

	CompositionLocalProvider(LocalSharedTransitionVisibilityScopeProvider provides this) {
		BluetoothProfileRoute(
			address = args.address,
			state = profile,
			onEvent = viewmodel::onEvent,
			onConnect = { uuid ->
				navigator.navigate(
					direction = ClientRouteDestination(address = args.address, uuid = uuid),
				) {
					popUpTo(BtProfileDestination) {
						inclusive = true
					}
				}
			},
			navigation = {
				IconButton(onClick = dropUnlessResumed(block = navigator::popBackStack)) {
					Icon(
						imageVector = Icons.AutoMirrored.Default.ArrowBack,
						contentDescription = stringResource(id = R.string.back_arrow)
					)
				}
			}
		)
	}
}