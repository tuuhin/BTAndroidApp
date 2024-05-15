package com.eva.bluetoothterminalapp.presentation.navigation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.BLEDeviceRoute
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.BLEDeviceViewModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLECharacteristicSheet
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.WriteCharactertisticDialog
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
	val viewmodel = koinViewModel<BLEDeviceViewModel>()
	val bleProfile by viewmodel.bLEProfile.collectAsStateWithLifecycle()
	val readableCharacteristic by viewmodel.readCharacteristic.collectAsStateWithLifecycle()
	val selectedCharcteristics by viewmodel.selectedCharacteristic.collectAsStateWithLifecycle()
	val writeDialogState by viewmodel.writeDialogState.collectAsStateWithLifecycle()

	BLECharacteristicSheet(
		isExpanded = selectedCharcteristics.isSheetExpanded,
		characteristic = readableCharacteristic,
		onEvent = viewmodel::onCharacteristicEvent,
	)

	WriteCharactertisticDialog(
		state = writeDialogState,
		onEvent = viewmodel::onWriteEvent
	)

	UIEventsSideEffect(
		viewModel = viewmodel,
		navigator = navigator
	)

	BLEDeviceRoute(
		profile = bleProfile,
		selectedCharacteristic = selectedCharcteristics,
		onSelectEvent = viewmodel::onCharacteristicEvent,
		onConfigEvent = viewmodel::onConfigEvents,
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