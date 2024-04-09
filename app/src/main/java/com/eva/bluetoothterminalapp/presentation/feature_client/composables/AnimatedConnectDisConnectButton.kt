package com.eva.bluetoothterminalapp.presentation.feature_client.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.eva.bluetoothterminalapp.domain.models.BTClientStatus

@Composable
fun AnimatedConnectDisconnectButton(
	connectionState: BTClientStatus,
	onConnect: () -> Unit,
	onDisConnect: () -> Unit,
	modifier: Modifier = Modifier
) {

	val showConnectButton by remember(connectionState) {
		derivedStateOf {
			connectionState == BTClientStatus.CONNECTION_ACCEPTED ||
					connectionState == BTClientStatus.CONNECTION_DISCONNECTED
		}
	}

	val isConnected by remember(connectionState) {
		derivedStateOf { connectionState == BTClientStatus.CONNECTION_ACCEPTED }
	}

	AnimatedVisibility(
		visible = showConnectButton,
		enter = slideInVertically(),
		exit = slideOutVertically(),
		modifier = modifier
	) {
		AnimatedContent(
			targetState = isConnected,
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
		) { normal ->
			if (normal)
				TextButton(
					onClick = onConnect,
					colors = ButtonDefaults
						.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
				) {
					Text(
						text = "Connect",
						style = MaterialTheme.typography.titleMedium
					)
				}
			else TextButton(
				onClick = onDisConnect,
				colors = ButtonDefaults
					.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
			) {
				Text(
					text = "Disconnect",
					style = MaterialTheme.typography.titleMedium
				)
			}
		}
	}
}