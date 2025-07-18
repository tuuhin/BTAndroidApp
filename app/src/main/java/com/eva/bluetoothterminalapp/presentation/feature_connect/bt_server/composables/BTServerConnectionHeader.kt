package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerDeviceState
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.BTConnectedDeviceProfile

@Composable
fun BTServerConnectionHeader(
	device: BTServerDeviceState,
	modifier: Modifier = Modifier
) {
	AnimatedContent(
		targetState = device.status,
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
		contentAlignment = Alignment.Center,
		modifier = modifier,
	) { connectionState ->
		when (connectionState) {
			ServerConnectionState.CONNECTION_INITIALIZING, ServerConnectionState.CONNECTION_LISTENING ->
				ServerConnectionStateChip(connectionState)

			ServerConnectionState.CONNECTION_ACCEPTED, ServerConnectionState.CONNECTION_DISCONNECTED ->
				BTConnectedDeviceProfile(device.device, connectionState = connectionState)
		}
	}
}