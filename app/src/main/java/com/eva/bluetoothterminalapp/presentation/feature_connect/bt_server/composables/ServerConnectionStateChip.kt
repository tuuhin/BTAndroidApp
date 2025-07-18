package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun ServerConnectionStateChip(
	connectionState: ServerConnectionState,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
	elevation: ChipElevation = AssistChipDefaults.elevatedAssistChipElevation()
) {

	val animatedCardColors by animateColorAsState(
		targetValue = connectionState.color,
		label = "Chip color animation"
	)

	AssistChip(
		onClick = {},
		label = { Text(text = connectionState.textResources) },
		shape = shape,
		colors = AssistChipDefaults.assistChipColors(
			containerColor = animatedCardColors,
			labelColor = contentColorFor(connectionState.color)
		),
		border = BorderStroke(1.dp, contentColorFor(connectionState.color)),
		elevation = elevation,
		modifier = modifier,
	)

}

private val ServerConnectionState.textResources: String
	@Composable
	get() = when (this) {
		ServerConnectionState.CONNECTION_INITIALIZING -> stringResource(id = R.string.connection_init_server)
		ServerConnectionState.CONNECTION_LISTENING -> stringResource(id = R.string.connection_server_listening)
		ServerConnectionState.CONNECTION_ACCEPTED -> stringResource(id = R.string.connection_accepted)
		ServerConnectionState.CONNECTION_DISCONNECTED -> stringResource(id = R.string.connection_disconnected)
	}

private val ServerConnectionState.color: Color
	@Composable
	get() = when (this) {
		ServerConnectionState.CONNECTION_INITIALIZING, ServerConnectionState.CONNECTION_LISTENING -> MaterialTheme.colorScheme.tertiaryContainer
		ServerConnectionState.CONNECTION_ACCEPTED -> MaterialTheme.colorScheme.primaryContainer
		ServerConnectionState.CONNECTION_DISCONNECTED -> MaterialTheme.colorScheme.secondaryContainer
	}

private class ServerStateTypesPreview :
	CollectionPreviewParameterProvider<ServerConnectionState>(ServerConnectionState.entries)

@PreviewLightDark
@Composable
private fun ServerConnectionStateChipPreview(
	@PreviewParameter(ServerStateTypesPreview::class)
	serverConnectionState: ServerConnectionState
) = BlueToothTerminalAppTheme {
	ServerConnectionStateChip(connectionState = serverConnectionState)
}