package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_le

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.WriteCharacteristicsDialog
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.CloseConnectionEvents
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.util.LocalSharedTransitionVisibilityScopeProvider
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
fun AnimatedVisibilityScope.BTLEClientScreen(
	navigator: DestinationsNavigator,
) {
	val viewModel = koinViewModel<BLEDeviceViewModel>()
	val bleProfile by viewModel.bLEProfile.collectAsStateWithLifecycle()
	val readableCharacteristic by viewModel.readCharacteristic.collectAsStateWithLifecycle()
	val selectedCharcteristics by viewModel.selectedCharacteristic.collectAsStateWithLifecycle()
	val writeDialogState by viewModel.writeDialogState.collectAsStateWithLifecycle()
	val showEndConnectionDialog by viewModel.showConnectionDialog.collectAsStateWithLifecycle()


	UIEventsSideEffect(
		events = { viewModel.uiEvents },
		onPopBack = dropUnlessResumed { navigator.popBackStack() }
	)

	val isBackHandlerEnabled by remember(bleProfile.connectionState) {
		derivedStateOf { bleProfile.connectionState == BLEConnectionState.CONNECTED }
	}

	BackHandler(
		enabled = isBackHandlerEnabled,
		onBack = {
			viewModel.onCloseConnectionEvent(CloseConnectionEvents.ShowCloseConnectionDialog)
		}
	)

	CloseConnectionDialog(
		showDialog = showEndConnectionDialog,
		onEvent = viewModel::onCloseConnectionEvent
	)

	BLECharacteristicSheet(
		isExpanded = selectedCharcteristics.isSheetExpanded,
		characteristic = readableCharacteristic,
		onEvent = viewModel::onCharacteristicEvent,
	)

	WriteCharacteristicsDialog(
		state = writeDialogState,
		onEvent = viewModel::onWriteEvent
	)

	CompositionLocalProvider(LocalSharedTransitionVisibilityScopeProvider provides this) {
		BLEDeviceRoute(
			profile = bleProfile,
			selectedCharacteristic = selectedCharcteristics,
			onSelectEvent = viewModel::onCharacteristicEvent,
			onConfigEvent = viewModel::onConfigEvents,
			navigation = {
				val onBack = dropUnlessResumed {
					if (isBackHandlerEnabled) {
						viewModel.onCloseConnectionEvent(CloseConnectionEvents.ShowCloseConnectionDialog)
					} else navigator.popBackStack()
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
}