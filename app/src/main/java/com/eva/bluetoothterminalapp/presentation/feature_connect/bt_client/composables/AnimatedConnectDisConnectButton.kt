package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState

@Composable
fun AnimatedConnectDisconnectButton(
	clientState: ClientConnectionState,
	onConnect: () -> Unit,
	onDisConnect: () -> Unit,
	modifier: Modifier = Modifier,
) {
	AnimatedContent(
		targetState = clientState,
		label = "Connect and disconnect states",
		transitionSpec = { slideInOutAnimation() },
		modifier = modifier.sizeIn(minWidth = 60.dp, minHeight = 40.dp),
		contentAlignment = Alignment.Center
	) { state ->
		when (state) {
			ClientConnectionState.CONNECTION_CONNECTED -> {
				FilledTonalButton(
					onClick = onDisConnect,
					contentPadding = ButtonDefaults.contentPaddingFor(
						ButtonDefaults.ExtraSmallContainerHeight,
						hasStartIcon = true
					),
					shapes = ButtonDefaults.shapes(
						pressedShape = ButtonDefaults.pressedShape,
						shape = ButtonDefaults.shape
					),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.tertiaryContainer,
						contentColor = MaterialTheme.colorScheme.onTertiaryContainer
					)
				) {
					Text(
						text = stringResource(id = R.string.disconnect_from_client),
						style = ButtonDefaults.textStyleFor(ButtonDefaults.ExtraSmallContainerHeight)
					)
				}
			}

			ClientConnectionState.CONNECTION_DENIED, ClientConnectionState.CONNECTION_DISCONNECTED -> {
				FilledTonalButton(
					onClick = onConnect,
					contentPadding = ButtonDefaults.contentPaddingFor(
						ButtonDefaults.ExtraSmallContainerHeight,
						hasStartIcon = true
					),
					shapes = ButtonDefaults.shapes(
						pressedShape = ButtonDefaults.pressedShape,
						shape = ButtonDefaults.shape
					),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.tertiaryContainer,
						contentColor = MaterialTheme.colorScheme.onTertiaryContainer
					)
				) {
					Text(
						text = stringResource(id = R.string.connect_to_client),
						style = ButtonDefaults.textStyleFor(ButtonDefaults.ExtraSmallContainerHeight)
					)
				}
			}

			else -> {}
		}
	}
}

private fun AnimatedContentTransitionScope<ClientConnectionState>.slideInOutAnimation(): ContentTransform =
	if (targetState == ClientConnectionState.CONNECTION_CONNECTED || targetState == ClientConnectionState.CONNECTION_PEER_FOUND) {
		slideInVertically { height -> -height } + fadeIn(initialAlpha = .25f) togetherWith
				slideOutVertically { height -> height } + fadeOut(targetAlpha = .25f)
	} else {
		slideInVertically { height -> height } + fadeIn(initialAlpha = .25f) togetherWith
				slideOutVertically { height -> -height } + fadeOut(targetAlpha = .25f)
	}