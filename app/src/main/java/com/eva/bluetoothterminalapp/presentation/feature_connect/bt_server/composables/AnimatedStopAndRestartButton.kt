package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState

@Composable
fun AnimatedStopAndRestartButton(
	state: ServerConnectionState,
	onRestart: () -> Unit,
	onStop: () -> Unit,
	modifier: Modifier = Modifier
) {
	val isConnectionAccepted by remember(state) {
		derivedStateOf {
			state == ServerConnectionState.CONNECTION_ACCEPTED
		}
	}

	val isConnectedOrDisconnected by remember(state) {
		derivedStateOf {
			state == ServerConnectionState.CONNECTION_ACCEPTED ||
					state == ServerConnectionState.CONNECTION_DISCONNECTED
		}
	}

	AnimatedVisibility(
		visible = isConnectedOrDisconnected,
		enter = slideInVertically(),
		exit = slideOutVertically(),
		modifier = modifier
	) {
		AnimatedContent(
			targetState = isConnectionAccepted,
			label = "Device is accepted or disconnected",
			transitionSpec = {
				if (targetState > initialState) {
					slideInVertically { height -> -height } + fadeIn(initialAlpha = .25f) togetherWith
							slideOutVertically { height -> height } + fadeOut(targetAlpha = .25f)
				} else {
					slideInVertically { height -> height } + fadeIn(initialAlpha = .25f) togetherWith
							slideOutVertically { height -> -height } + fadeOut(targetAlpha = .25f)
				}
			},
		) { isConnected ->
			if (isConnected) TextButton(onClick = onStop) {
				Text(
					text = stringResource(id = R.string.topbar_action_stop),
					style = MaterialTheme.typography.titleMedium
				)
			}
			else TextButton(onClick = onRestart) {
				Text(
					text = stringResource(id = R.string.topbar_action_restart),
					style = MaterialTheme.typography.titleMedium
				)
			}
		}
	}
}