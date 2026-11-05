package com.eva.bluetoothterminalapp.presentation.navigation.screens.bt_classic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.BluetoothProfileRoute
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.BluetoothProfileViewModel
import com.eva.bluetoothterminalapp.presentation.navigation.NavDestinationBuilder
import com.eva.bluetoothterminalapp.presentation.navigation.NavRoutes
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Single
import org.koin.core.parameter.parametersOf
import kotlin.uuid.toKotlinUuid


@Single(binds = [NavDestinationBuilder::class])
class BTDeviceProfileScreen : NavDestinationBuilder {

	override fun EntryProviderScope<NavKey>.navEntry(backStack: NavBackStack<NavKey>) =
		entry<NavRoutes.DeviceProfileRoute> { route ->

			val viewmodel = koinViewModel<BluetoothProfileViewModel> { parametersOf(route.address) }
			val profile by viewmodel.profile.collectAsStateWithLifecycle()

			UIEventsSideEffect(
				events = { viewmodel.uiEvents },
				onPopBack = { backStack.removeLastOrNull() })

			BluetoothProfileRoute(
				address = route.address,
				state = profile,
				onEvent = viewmodel::onEvent,
				onConnect = { uuid ->
					backStack.removeLastOrNull()
					backStack.add(
						NavRoutes.ClassicClientRoute(
							route.address, uuid.toKotlinUuid()
						)
					)
				},
				navigation = {
					IconButton(onClick = dropUnlessResumed { backStack.removeLastOrNull() }) {
						Icon(
							imageVector = Icons.AutoMirrored.Default.ArrowBack,
							contentDescription = stringResource(id = R.string.back_arrow)
						)
					}
				}
			)
		}
}