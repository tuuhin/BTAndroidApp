package com.eva.bluetoothterminalapp.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.generated.NavGraphs


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
	modifier: Modifier = Modifier
) {
	val engine = rememberAnimatedNavHostEngine()
	val snackBarHostState = remember { SnackbarHostState() }

	CompositionLocalProvider(
		LocalSnackBarProvider provides snackBarHostState,
	) {
		DestinationsNavHost(
			engine = engine,
			navGraph = NavGraphs.root, modifier = modifier
		)
	}
}