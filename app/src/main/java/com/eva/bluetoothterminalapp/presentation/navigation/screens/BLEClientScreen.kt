package com.eva.bluetoothterminalapp.presentation.navigation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.BLEConnectionViewmodel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.BTLEDeviceProfileRoute
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination(
	route = Routes.BLE_CLIENT_ROUTE,
	style = RouteAnimation::class,
	navArgsDelegate = BluetoothDeviceArgs::class
)
@Composable
fun BTLEClientScreen(
	navigator: DestinationsNavigator,
) {
	val viewmodel = koinViewModel<BLEConnectionViewmodel>()
	val bleProfile by viewmodel.bLEProfile.collectAsStateWithLifecycle()
	val selectedCharacteristic by viewmodel.selectedCharacteric.collectAsStateWithLifecycle()

	UIEventsSideEffect(
		viewModel = viewmodel,
		navigator = navigator
	)

	BTLEDeviceProfileRoute(
		profile = bleProfile,
		selectedCharacteristic = selectedCharacteristic,
		onProfileEvent = viewmodel::onEvent,
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