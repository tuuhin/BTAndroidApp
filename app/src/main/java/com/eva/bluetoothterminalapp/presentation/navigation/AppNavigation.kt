package com.eva.bluetoothterminalapp.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.eva.bluetoothterminalapp.presentation.util.LocalSharedTransitionScopeProvider
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.rememberNavHostEngine


@OptIn(
	ExperimentalAnimationApi::class,
	ExperimentalSharedTransitionApi::class
)
@Composable
fun AppNavigation(
	modifier: Modifier = Modifier
) {
	val engine = rememberNavHostEngine()
	val snackBarHostState = remember { SnackbarHostState() }

	SharedTransitionLayout {
		CompositionLocalProvider(
			LocalSnackBarProvider provides snackBarHostState,
			LocalSharedTransitionScopeProvider provides this,
		) {
			DestinationsNavHost(
				engine = engine,
				navGraph = NavGraphs.root,
				modifier = modifier
			)
		}
	}
}