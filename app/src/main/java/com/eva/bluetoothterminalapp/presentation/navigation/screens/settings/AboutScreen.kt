package com.eva.bluetoothterminalapp.presentation.navigation.screens.settings

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_settings.AboutRoute
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.eva.bluetoothterminalapp.presentation.util.LocalSharedTransitionVisibilityScopeProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>(
	route = Routes.INFORMATION_ROUTE,
	style = RouteAnimation::class,
)
@Composable
fun AnimatedVisibilityScope.AboutScreen(
	navigator: DestinationsNavigator,
) {
	CompositionLocalProvider(LocalSharedTransitionVisibilityScopeProvider provides this) {
		AboutRoute(
			navigation = {
				val onBackPress = dropUnlessResumed(block = navigator::popBackStack)
				IconButton(onClick = onBackPress) {
					Icon(
						imageVector = Icons.AutoMirrored.Default.ArrowBack,
						contentDescription = stringResource(id = R.string.back_arrow)
					)
				}
			},
		)
	}
}