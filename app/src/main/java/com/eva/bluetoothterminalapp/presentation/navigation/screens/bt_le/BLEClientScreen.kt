package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_le

import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.BLEDeviceRoute
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.BLEDeviceViewModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLECharacteristicSheet
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.CloseConnectionDialog
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.WriteCharacteristicsDialog
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.CloseConnectionEvents
import com.eva.bluetoothterminalapp.presentation.navigation.NavDestinationBuilder
import com.eva.bluetoothterminalapp.presentation.navigation.NavRoutes
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Single
import org.koin.core.parameter.parametersOf

@Single(binds = [NavDestinationBuilder::class])
class BLEClientScreen : NavDestinationBuilder {

	@OptIn(ExperimentalMaterial3Api::class)
	override fun EntryProviderScope<NavKey>.navEntry(backStack: NavBackStack<NavKey>) =
		entry<NavRoutes.BLEClientRoute> { args ->

			val address = args.address

			val viewModel = koinViewModel<BLEDeviceViewModel> { parametersOf(address) }

			val bleProfile by viewModel.bLEProfile.collectAsStateWithLifecycle()
			val readableCharacteristic by viewModel.readCharacteristic.collectAsStateWithLifecycle()
			val selectedCharacteristics by viewModel.selectedCharacteristic.collectAsStateWithLifecycle()
			val writeDialogState by viewModel.writeDialogState.collectAsStateWithLifecycle()
			val showEndConnectionDialog by viewModel.showConnectionDialog.collectAsStateWithLifecycle()

			UIEventsSideEffect(
				events = { viewModel.uiEvents },
				onPopBack = dropUnlessResumed { backStack.removeLastOrNull() }
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
				isExpanded = selectedCharacteristics.isSheetExpanded,
				characteristic = readableCharacteristic,
				onEvent = viewModel::onCharacteristicEvent,
			)

			WriteCharacteristicsDialog(
				state = writeDialogState,
				onEvent = viewModel::onWriteEvent
			)

			BLEDeviceRoute(
				deviceAddress = args.address,
				profile = bleProfile,
				selectedCharacteristic = selectedCharacteristics,
				onSelectEvent = viewModel::onCharacteristicEvent,
				onConfigEvent = viewModel::onConfigEvents,
				navigation = {
					val onBack = dropUnlessResumed {
						if (isBackHandlerEnabled) {
							viewModel.onCloseConnectionEvent(CloseConnectionEvents.ShowCloseConnectionDialog)
						} else backStack.removeLastOrNull()
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
