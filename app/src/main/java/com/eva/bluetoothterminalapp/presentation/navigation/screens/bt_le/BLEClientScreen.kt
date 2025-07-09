package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_le

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.BLEDeviceRoute
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.BLEDeviceViewModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLECharacteristicSheet
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.CloseConnectionDialog
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.WriteCharactertisticDialog
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.CloseConnectionEvents
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>(
	route = Routes.BLE_CLIENT_ROUTE,
	style = RouteAnimation::class,
	navArgs = BluetoothDeviceArgs::class
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
	val showEndConnectionDialog by viewmodel.showConnectionDialog.collectAsStateWithLifecycle()


	UIEventsSideEffect(
		viewModel = viewmodel,
		navigator = navigator
	)

	val isBackHandlerEnabled by remember(bleProfile.connectionState) {
		derivedStateOf { bleProfile.connectionState == BLEConnectionState.CONNECTED }
	}

	val onBackCallBack: () -> Unit = remember {
		{
			viewmodel.onCloseConnectionEvent(CloseConnectionEvents.ShowCloseConnectionDialog)
		}
	}

	BackHandler(
		enabled = isBackHandlerEnabled,
		onBack = onBackCallBack
	)

	CloseConnectionDialog(
		showDialog = showEndConnectionDialog,
		onEvent = viewmodel::onCloseConnectionEvent
	)

	BLECharacteristicSheet(
		isExpanded = selectedCharcteristics.isSheetExpanded,
		characteristic = readableCharacteristic,
		onEvent = viewmodel::onCharacteristicEvent,
	)

	WriteCharactertisticDialog(
		state = writeDialogState,
		onEvent = viewmodel::onWriteEvent
	)

	BLEDeviceRoute(
		profile = bleProfile,
		selectedCharacteristic = selectedCharcteristics,
		onSelectEvent = viewmodel::onCharacteristicEvent,
		onConfigEvent = viewmodel::onConfigEvents,
		navigation = {
			val onBack = dropUnlessResumed {
				if (isBackHandlerEnabled) onBackCallBack()
				else navigator.popBackStack()
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