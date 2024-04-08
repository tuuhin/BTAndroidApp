package com.eva.bluetoothterminalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.eva.bluetoothterminalapp.presentation.navigation.screens.NavGraphs
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine

@OptIn(
	ExperimentalMaterialNavigationApi::class,
	ExperimentalAnimationApi::class
)
class MainActivity : ComponentActivity() {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		installSplashScreen()

		WindowCompat.setDecorFitsSystemWindows(window, false)

		setContent {
			BlueToothTerminalAppTheme {
				// A surface container using the 'background' color from the theme
				val snackBarHostState = remember { SnackbarHostState() }
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					// A surface container using the 'background' color from the theme
					CompositionLocalProvider(
						LocalSnackBarProvider provides snackBarHostState
					) {
						Surface(color = MaterialTheme.colorScheme.background) {
							val engine = rememberAnimatedNavHostEngine()
							DestinationsNavHost(navGraph = NavGraphs.root, engine = engine)
						}
					}
				}
			}
		}
	}
}
