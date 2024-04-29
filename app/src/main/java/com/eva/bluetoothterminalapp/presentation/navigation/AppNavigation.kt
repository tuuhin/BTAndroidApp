package com.eva.bluetoothterminalapp.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.eva.bluetoothterminalapp.presentation.navigation.screens.NavGraphs
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine

@OptIn(
	ExperimentalMaterialNavigationApi::class,
	ExperimentalAnimationApi::class
)
@Composable
fun AppNavigation(
	engine: NavHostEngine = rememberAnimatedNavHostEngine()
) {
	DestinationsNavHost(
		engine = engine,
		navGraph = NavGraphs.root,
	)
}