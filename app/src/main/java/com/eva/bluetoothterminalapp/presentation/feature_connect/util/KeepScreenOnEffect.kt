package com.eva.bluetoothterminalapp.presentation.feature_connect.util

import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState

@Composable
fun KeepScreenOnEffect(
	connectionState: ClientConnectionState,
	isKeepScreenOn: Boolean
) {

	if (!isKeepScreenOn) return

	val context = LocalContext.current

	DisposableEffect(connectionState, isKeepScreenOn) {
		// otherwise
		val isAccepted = connectionState == ClientConnectionState.CONNECTION_ACCEPTED
		val window = (context as? Activity)?.window ?: return@DisposableEffect onDispose { }

		when {
			isAccepted -> window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
			else -> window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		}

		val hasFlag = window.attributes.flags and
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON != 0

		if (hasFlag) {
			val message = context.getString(R.string.toast_screen_will_be_on)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
		}

		onDispose {
			// clear the flag before disposing if it has one
			if (hasFlag) window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		}
	}
}

@Composable
fun KeepScreenOnEffect(
	connectionState: ServerConnectionState,
	isKeepScreenOn: Boolean
) {

	if (!isKeepScreenOn) return

	val context = LocalContext.current

	DisposableEffect(connectionState, isKeepScreenOn) {
		// otherwise
		val isAccepted = connectionState == ServerConnectionState.CONNECTION_ACCEPTED
		val window = (context as? Activity)?.window ?: return@DisposableEffect onDispose { }

		when {
			isAccepted -> window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
			else -> window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		}

		val hasFlag = window.attributes.flags and
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON != 0

		if (hasFlag) {
			val message = context.getString(R.string.toast_screen_will_be_on)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
		}

		onDispose {
			// clear the flag before disposing if it has one
			if (hasFlag) window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		}
	}
}