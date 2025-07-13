package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState

@Composable
fun ConnectReconnectButton(
	onDisconnect: () -> Unit,
	onReconnect: () -> Unit,
	modifier: Modifier = Modifier,
	connectionState: BLEConnectionState = BLEConnectionState.CONNECTING,
) {
	if (connectionState == BLEConnectionState.FAILED) return
	AnimatedContent(
		targetState = connectionState,
		label = "Connect and disconnect states",
		transitionSpec = { slideInOutAnimation() },
		contentAlignment = Alignment.Center,
		modifier = modifier,
	) { state ->

		when (state) {
			BLEConnectionState.CONNECTED -> TextButton(
				onClick = onDisconnect,
				enabled = !transition.isRunning
			) {
				Text(text = stringResource(id = R.string.disconnect_from_client))
			}

			BLEConnectionState.DISCONNECTED -> TextButton(
				onClick = onReconnect,
				enabled = !transition.isRunning
			) {
				Text(text = stringResource(id = R.string.connect_to_client))
			}

			else -> Box(modifier = Modifier.widthIn(min = 110.dp))
		}
	}
}

private fun AnimatedContentTransitionScope<BLEConnectionState>.slideInOutAnimation(): ContentTransform =
	when (targetState) {
		targetState -> {
			slideInVertically { height -> -height } + fadeIn(initialAlpha = .25f) togetherWith
					slideOutVertically { height -> height } + fadeOut(targetAlpha = .25f)
		}

		targetState -> {
			slideInVertically { height -> height } + fadeIn(initialAlpha = .25f) togetherWith
					slideOutVertically { height -> -height } + fadeOut(targetAlpha = .25f)
		}

		else -> fadeIn() togetherWith fadeOut()
	}