package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
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
	AssistChip(
		onClick = {},
		label = { Text(text = connectionState.textResources) },
		shape = shape,
		colors = AssistChipDefaults.assistChipColors(
			containerColor = MaterialTheme.colorScheme.tertiaryContainer,
			labelColor = MaterialTheme.colorScheme.onTertiaryContainer
		),
		elevation = elevation,
		modifier = modifier,
	)

}

private val ServerConnectionState.textResources: String
	@Composable
	get() = when (this) {
		ServerConnectionState.SERVER_STARTING -> stringResource(id = R.string.connection_init_server)
		ServerConnectionState.SERVER_LISTENING -> stringResource(id = R.string.connection_server_listening)
		ServerConnectionState.SERVER_STOPPED -> stringResource(id = R.string.connection_server_stopped)
		ServerConnectionState.PEER_CONNECTION_ACCEPTED -> stringResource(id = R.string.connection_accepted)
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