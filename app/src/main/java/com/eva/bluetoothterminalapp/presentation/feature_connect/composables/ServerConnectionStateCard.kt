package com.eva.bluetoothterminalapp.presentation.feature_connect.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.ServerConnectionState
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun ServerConnectionStateCard(
	connectionState: ServerConnectionState,
	modifier: Modifier = Modifier
) {
	Card(
		colors = CardDefaults
			.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
		elevation = CardDefaults
			.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
		shape = MaterialTheme.shapes.medium,
		modifier = modifier
	) {
		Crossfade(
			targetState = connectionState,
			label = "Current state",
			animationSpec = tween(durationMillis = 300, easing = LinearEasing),
			modifier = Modifier
				.padding(10.dp)
				.align(Alignment.CenterHorizontally)
		) { state ->
			when (state) {
				ServerConnectionState.CONNECTION_INITIALIZING -> {
					Text(text = stringResource(id = R.string.connection_init_server))
				}

				ServerConnectionState.CONNECTION_LISTENING -> {
					Row(
						horizontalArrangement = Arrangement.spacedBy(6.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(
							imageVector = Icons.Default.Wifi,
							contentDescription = null,
							modifier = Modifier.graphicsLayer { rotationZ = -90f },
						)
						Text(text = stringResource(id = R.string.connection_server_listening))
						Icon(
							imageVector = Icons.Default.Wifi,
							contentDescription = null,
							modifier = Modifier.graphicsLayer { rotationZ = 90f },
						)
					}
				}

				ServerConnectionState.CONNECTION_ACCEPTED -> {
					Row(
						horizontalArrangement = Arrangement.spacedBy(6.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_connected),
							contentDescription = stringResource(id = R.string.connection_init)
						)
						Text(text = stringResource(id = R.string.connection_accepted))
					}
				}

				ServerConnectionState.CONNECTION_DISCONNECTED -> {
					Row(
						horizontalArrangement = Arrangement.spacedBy(6.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_dead),
							contentDescription = stringResource(id = R.string.connection_disconnected)
						)
						Text(text = stringResource(id = R.string.connection_disconnected))
					}
				}
			}
		}
	}
}

private class ServerStateTypesPreview :
	CollectionPreviewParameterProvider<ServerConnectionState>(ServerConnectionState.entries)

@PreviewLightDark
@Composable
private fun ServerConnectionStateCardPreview(
	@PreviewParameter(ServerStateTypesPreview::class)
	serverConnectionState: ServerConnectionState
) = BlueToothTerminalAppTheme {
	ServerConnectionStateCard(connectionState = serverConnectionState)
}