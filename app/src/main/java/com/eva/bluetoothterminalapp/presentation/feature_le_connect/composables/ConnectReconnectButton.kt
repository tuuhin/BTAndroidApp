package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState

@Composable
fun ConnectReconnectButton(
	connctionState: BLEConnectionState,
	onDisconnect: () -> Unit,
	onReconnect: () -> Unit,
	modifier: Modifier = Modifier
) {
	if (connctionState == BLEConnectionState.FAILED) return
	AnimatedContent(
		targetState = connctionState,
		label = "Connect and disconnect states",
		transitionSpec = { slideInOutAnimation() },
		modifier = modifier,
		contentAlignment = Alignment.Center
	) { state ->
		when (state) {
			BLEConnectionState.CONNECTED -> TextButton(onClick = onDisconnect) {
				Text(text = stringResource(id = R.string.disconnect_from_client))
			}

			BLEConnectionState.DISCONNECTED -> TextButton(onClick = onReconnect) {
				Text(text = stringResource(id = R.string.connect_to_client))
			}

			else -> {}
		}
	}
}

private fun AnimatedContentTransitionScope<BLEConnectionState>.slideInOutAnimation(): ContentTransform =
	if (targetState == BLEConnectionState.CONNECTED) {
		slideInVertically { height -> -height } + fadeIn(initialAlpha = .25f) togetherWith
				slideOutVertically { height -> height } + fadeOut(targetAlpha = .25f)
	} else if (targetState == BLEConnectionState.DISCONNECTED) {
		slideInVertically { height -> height } + fadeIn(initialAlpha = .25f) togetherWith
				slideOutVertically { height -> -height } + fadeOut(targetAlpha = .25f)
	} else fadeIn() togetherWith fadeOut()