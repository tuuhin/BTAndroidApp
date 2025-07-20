package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.PeerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState

@Composable
fun AnimatedStopAndRestartButton(
	serverState: ServerConnectionState,
	peerState: PeerConnectionState,
	onRestart: () -> Unit,
	onStop: () -> Unit,
	modifier: Modifier = Modifier
) {
	val actionState by remember(serverState, peerState) {
		derivedStateOf {
			when {
				serverState == ServerConnectionState.PEER_CONNECTION_ACCEPTED && peerState == PeerConnectionState.PEER_CONNECTED -> ActionState.STOP_SERVER
				serverState == ServerConnectionState.SERVER_STOPPED || peerState == PeerConnectionState.PEER_DISCONNECTED -> ActionState.RESTART_SERVER
				else -> ActionState.NONE
			}
		}
	}

	AnimatedContent(
		targetState = actionState,
		label = "Device is accepted or disconnected",
		transitionSpec = {
			slideInVertically() + fadeIn(initialAlpha = .25f) togetherWith
					slideOutVertically() + fadeOut(targetAlpha = .25f)
		},
		contentAlignment = Alignment.Center,
		modifier = modifier,
	) { state ->
		when (state) {
			ActionState.STOP_SERVER -> TextButton(onClick = onStop) {
				Text(
					text = stringResource(id = R.string.topbar_action_stop),
					style = MaterialTheme.typography.titleMedium
				)
			}

			ActionState.RESTART_SERVER -> TextButton(onClick = onRestart) {
				Text(
					text = stringResource(id = R.string.topbar_action_restart),
					style = MaterialTheme.typography.titleMedium
				)
			}

			else -> Box(modifier = Modifier.size(60.dp, 40.dp))
		}
	}
}

private enum class ActionState {
	NONE, STOP_SERVER, RESTART_SERVER
}