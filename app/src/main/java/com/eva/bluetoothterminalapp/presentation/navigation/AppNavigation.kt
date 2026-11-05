package com.eva.bluetoothterminalapp.presentation.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.eva.bluetoothterminalapp.presentation.navigation.scene.BottomSheetSceneStrategy
import com.eva.bluetoothterminalapp.presentation.util.LocalSharedTransitionScopeProvider
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation(
	modifier: Modifier = Modifier
) {

	val backStack = rememberNavBackStack(NavRoutes.DevicesRoute)
	val snackBarHostState = remember { SnackbarHostState() }
	val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

	val koin = getKoin()
	val destinations = remember(koin) { koin.getAll<NavDestinationBuilder>() }

	SharedTransitionLayout {
		CompositionLocalProvider(
			LocalSnackBarProvider provides snackBarHostState,
			LocalSharedTransitionScopeProvider provides this
		) {
			NavDisplay(
				backStack = backStack,
				modifier = modifier,
				sharedTransitionScope = this,
				sceneStrategies = listOf(
					DialogSceneStrategy(),
					BottomSheetSceneStrategy(),
					listDetailStrategy
				),
				entryDecorators = listOf(
					rememberSaveableStateHolderNavEntryDecorator(),
					rememberViewModelStoreNavEntryDecorator(),
				),
				contentAlignment = Alignment.Center,
				entryProvider = entryProvider {
					destinations.forEach { builder ->
						builder.apply { navEntry(backStack) }
					}
				}
			)
		}
	}
}