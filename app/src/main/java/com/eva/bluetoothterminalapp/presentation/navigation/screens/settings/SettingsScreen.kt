package com.eva.bluetoothterminalapp.presentation.navigation.screens.settings

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
import com.eva.bluetoothterminalapp.presentation.feature_settings.AppSettingsRoute
import com.eva.bluetoothterminalapp.presentation.feature_settings.AppSettingsViewModel
import com.eva.bluetoothterminalapp.presentation.navigation.UIEventsSideEffect
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.util.LocalSharedTransitionVisibilityScopeProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>(
	route = Routes.SETTINGS_ROUTE,
	style = RouteAnimation::class
)
@Composable
fun AnimatedVisibilityScope.SettingsScreen(
	navigator: DestinationsNavigator,
) {
	val viewModel = koinViewModel<AppSettingsViewModel>()

	val bleSettings by viewModel.bleSettings.collectAsStateWithLifecycle()
	val classicSettings by viewModel.btSettings.collectAsStateWithLifecycle()

	UIEventsSideEffect(
		events = { viewModel.uiEvents },
		onPopBack = dropUnlessResumed { navigator.popBackStack() }
	)

	CompositionLocalProvider(LocalSharedTransitionVisibilityScopeProvider provides this) {
		AppSettingsRoute(
			bleSettings = bleSettings,
			btSettings = classicSettings,
			onBLEEvent = viewModel::onBLEEvents,
			onBTEvent = viewModel::onBTClassicEvents,
			navigation = {
				IconButton(onClick = dropUnlessResumed(block = navigator::popBackStack)) {
					Icon(
						imageVector = Icons.AutoMirrored.Default.ArrowBack,
						contentDescription = stringResource(id = R.string.back_arrow)
					)
				}
			},
		)
	}
}