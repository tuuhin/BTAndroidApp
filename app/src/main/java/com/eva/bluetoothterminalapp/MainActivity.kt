package com.eva.bluetoothterminalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.eva.bluetoothterminalapp.presentation.navigation.AppNavigation
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {

		installSplashScreen()

		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		setContent {
			BlueToothTerminalAppTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					AppNavigation()
				}
			}
		}
	}
}
