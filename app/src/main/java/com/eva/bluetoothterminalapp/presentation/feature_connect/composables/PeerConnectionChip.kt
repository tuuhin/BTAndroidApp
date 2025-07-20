package com.eva.bluetoothterminalapp.presentation.feature_connect.composables

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.PeerConnectionState

@Composable
fun PeerConnectionChip(
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
	connectionState: PeerConnectionState = PeerConnectionState.PEER_DISCONNECTED,
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

private val PeerConnectionState.textResources: String
	@Composable
	get() = when (this) {
		PeerConnectionState.PEER_CONNECTED -> stringResource(R.string.connection_accepted)
		PeerConnectionState.PEER_DISCONNECTED -> stringResource(R.string.connection_disconnected)
		PeerConnectionState.PEER_NOT_FOUND -> stringResource(R.string.connected_device_not_found)
	}