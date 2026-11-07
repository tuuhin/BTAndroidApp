package com.eva.bluetoothterminalapp.presentation.navigation.screens

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors
import com.eva.bluetoothterminalapp.presentation.composables.BTAppNavigationDrawer
import com.eva.bluetoothterminalapp.presentation.feature_devices.BTDeviceViewmodel
import com.eva.bluetoothterminalapp.presentation.feature_devices.BTDevicesRoute
import com.eva.bluetoothterminalapp.presentation.navigation.NavDestinationBuilder
import com.eva.bluetoothterminalapp.presentation.navigation.NavRoutes
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Single

@Single(binds = [NavDestinationBuilder::class])
class BTDevicesScreen : NavDestinationBuilder {

	override fun EntryProviderScope<NavKey>.navEntry(backStack: NavBackStack<NavKey>) =
		entry<NavRoutes.DevicesRoute> {
			val viewModel = koinViewModel<BTDeviceViewmodel>()

			val state by viewModel.screenState.collectAsStateWithLifecycle()
			val isBTActive by viewModel.isBTActive.collectAsStateWithLifecycle()
			val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()

			UIEventsSideEffect(events = { viewModel.uiEvents })

			val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
			val scope = rememberCoroutineScope()

			ModalNavigationDrawer(
				drawerState = drawerState,
				gesturesEnabled = true,
				drawerContent = {
					BTAppNavigationDrawer(
						onNavigateToBLEServer = { backStack.add(NavRoutes.BLEServerRoute) },
						onNavigateToClassicServer = { backStack.add(NavRoutes.ClassicServerRoute) },
						onNavigateToSettingsRoute = { backStack.add(NavRoutes.SettingsRoute) },
						onNavigateToFeedBackRoute = { backStack.add(NavRoutes.AboutRoute) }
					)
				},
			) {
				BTDevicesRoute(
					state = state,
					isBTActive = isBTActive,
					isScanning = isScanning,
					onEvent = viewModel::onEvents,
					navigation = {
						IconButton(onClick = { scope.launch { drawerState.open() } }) {
							Icon(imageVector = AppIconVectors.menu_open, "")
						}
					},
					onSelectDevice = { device ->
						backStack.add(NavRoutes.DeviceProfileRoute(device.address, device.name))
					},
					onSelectLeDevice = { device ->
						backStack.add(
							NavRoutes.BLEClientRoute(
								device.deviceModel.address,
								device.deviceName
							)
						)
					},
				)
			}
		}
}