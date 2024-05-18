package com.eva.bluetoothterminalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.eva.bluetoothterminalapp.presentation.navigation.AppNavigation
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {

		installSplashScreen()

		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

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
						LocalSnackBarProvider provides snackBarHostState,
						LocalLifecycleOwner provides this
					) {
						Surface(
							color = MaterialTheme.colorScheme.background,
						) {
							AppNavigation()
						}
					}
				}
			}
		}
	}
}
