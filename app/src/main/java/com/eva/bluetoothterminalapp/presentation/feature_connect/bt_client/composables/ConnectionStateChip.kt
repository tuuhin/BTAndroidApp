package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun ClientConnectionStateChip(
	connectionState: ClientConnectionState,
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {},
	shape: Shape = MaterialTheme.shapes.small,
	elevation: ChipElevation = AssistChipDefaults.elevatedAssistChipElevation(
		elevation = 6.dp,
		pressedElevation = 8.dp
	),
) {

	val animatedCardColors by animateColorAsState(
		targetValue = connectionState.color,
		label = "Card Colors Animation",
	)

	ProvideTextStyle(value = MaterialTheme.typography.titleSmall) {
		AssistChip(
			onClick = onClick,
			label = {
				Crossfade(
					targetState = connectionState,
					label = "Current client state",
					animationSpec = tween(durationMillis = 300, easing = LinearEasing),
					modifier = Modifier.padding(4.dp)
				) { state ->
					Text(
						text = state.textResource,
						textAlign = TextAlign.Center
					)
				}
			},
			shape = shape,
			colors = AssistChipDefaults.assistChipColors(
				containerColor = animatedCardColors,
				labelColor = contentColorFor(connectionState.color)
			),
			border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
			elevation = elevation,
			modifier = modifier,
		)
	}
}

private val ClientConnectionState.textResource: String
	@Composable
	get() = when (this) {
		ClientConnectionState.CONNECTION_DEVICE_FOUND -> stringResource(id = R.string.connection_device_found)
		ClientConnectionState.CONNECTION_DENIED -> stringResource(id = R.string.connection_denied)
		ClientConnectionState.CONNECTION_DISCONNECTED -> stringResource(id = R.string.connection_disconnected)
		ClientConnectionState.CONNECTION_BONDING -> stringResource(id = R.string.connection_device_bonding)
		ClientConnectionState.CONNECTION_BONDED -> stringResource(id = R.string.connection_bonded)
		ClientConnectionState.CONNECTION_INITIALIZING -> stringResource(R.string.connection_init)
		ClientConnectionState.CONNECTION_ACCEPTED -> stringResource(id = R.string.connection_accepted)
	}

private val ClientConnectionState.color: Color
	@Composable
	get() = when (this) {
		ClientConnectionState.CONNECTION_INITIALIZING -> MaterialTheme.colorScheme.surfaceContainer
		ClientConnectionState.CONNECTION_ACCEPTED, ClientConnectionState.CONNECTION_DEVICE_FOUND ->
			MaterialTheme.colorScheme.primaryContainer

		ClientConnectionState.CONNECTION_DENIED, ClientConnectionState.CONNECTION_DISCONNECTED ->
			MaterialTheme.colorScheme.tertiaryContainer

		ClientConnectionState.CONNECTION_BONDING, ClientConnectionState.CONNECTION_BONDED ->
			MaterialTheme.colorScheme.secondaryContainer
	}

class ConnectionStatePreviews
	: CollectionPreviewParameterProvider<ClientConnectionState>(ClientConnectionState.entries)

@PreviewLightDark
@Composable
private fun ClientConnectionStateChipPreview(
	@PreviewParameter(ConnectionStatePreviews::class)
	state: ClientConnectionState,
) = BlueToothTerminalAppTheme {
	Surface {
		ClientConnectionStateChip(
			connectionState = state,
			modifier = Modifier
				.padding(all = 4.dp)
		)
	}
}