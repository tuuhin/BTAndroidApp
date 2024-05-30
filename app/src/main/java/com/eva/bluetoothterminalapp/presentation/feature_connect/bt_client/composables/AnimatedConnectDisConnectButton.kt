package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState

@Composable
fun AnimatedConnectDisconnectButton(
	clientState: ClientConnectionState,
	onConnect: () -> Unit,
	onDisConnect: () -> Unit,
	modifier: Modifier = Modifier,
	colors: ButtonColors = ButtonDefaults.textButtonColors(),
	style: TextStyle = MaterialTheme.typography.titleMedium
) {
	ProvideTextStyle(value = style) {
		AnimatedContent(
			targetState = clientState,
			label = "Connect and disconnect states",
			transitionSpec = { slideInOutAnimation() },
			modifier = modifier,
			contentAlignment = Alignment.Center
		) { state ->
			when (state) {
				ClientConnectionState.CONNECTION_ACCEPTED -> {
					TextButton(onClick = onDisConnect, colors = colors) {
						Text(text = stringResource(id = R.string.disconnect_from_client))
					}
				}

				ClientConnectionState.CONNECTION_DENIED, ClientConnectionState.CONNECTION_DISCONNECTED -> {
					TextButton(onClick = onConnect, colors = colors) {
						Text(text = stringResource(id = R.string.connect_to_client))
					}
				}

				else -> {}
			}
		}
	}
}

private fun AnimatedContentTransitionScope<ClientConnectionState>.slideInOutAnimation(): ContentTransform =
	if (targetState == ClientConnectionState.CONNECTION_ACCEPTED || targetState == ClientConnectionState.CONNECTION_DEVICE_CONNECTED) {
		slideInVertically { height -> -height } + fadeIn(initialAlpha = .25f) togetherWith
				slideOutVertically { height -> height } + fadeOut(targetAlpha = .25f)
	} else {
		slideInVertically { height -> height } + fadeIn(initialAlpha = .25f) togetherWith
				slideOutVertically { height -> -height } + fadeOut(targetAlpha = .25f)
	}