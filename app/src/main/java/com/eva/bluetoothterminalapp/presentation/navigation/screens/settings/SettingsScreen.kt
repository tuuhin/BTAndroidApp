package com.eva.bluetoothterminalapp.presentation.navigation.screens.settings

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
import com.eva.bluetoothterminalapp.presentation.feature_settings.AppSettingsRoute
import com.eva.bluetoothterminalapp.presentation.feature_settings.AppSettingsViewModel
import com.eva.bluetoothterminalapp.presentation.navigation.NavDestinationBuilder
import com.eva.bluetoothterminalapp.presentation.navigation.NavRoutes
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.Single

@Single(binds = [NavDestinationBuilder::class])
class SettingsScreen : NavDestinationBuilder {

	override fun EntryProviderScope<NavKey>.navEntry(backStack: NavBackStack<NavKey>) =
		entry<NavRoutes.SettingsRoute> {
			val viewModel = koinViewModel<AppSettingsViewModel>()

			val bleSettings by viewModel.bleSettings.collectAsStateWithLifecycle()
			val classicSettings by viewModel.btSettings.collectAsStateWithLifecycle()

			UIEventsSideEffect(
				events = { viewModel.uiEvents },
				onPopBack = dropUnlessResumed { backStack.removeLastOrNull() }
			)

			AppSettingsRoute(
				bleSettings = bleSettings,
				btSettings = classicSettings,
				onBLEEvent = viewModel::onBLEEvents,
				onBTEvent = viewModel::onBTClassicEvents,
				navigation = {
					IconButton(onClick = dropUnlessResumed { backStack.removeLastOrNull() }) {
						Icon(
							imageVector = Icons.AutoMirrored.Default.ArrowBack,
							contentDescription = stringResource(id = R.string.back_arrow)
						)
					}
				},
			)
		}
}
