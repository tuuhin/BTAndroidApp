package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
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
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEConnectionStatusChip(
	state: BLEConnectionState,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
) {
	val animatedColor by animateColorAsState(
		targetValue = state.color,
		label = "Chip color animation"
	)

	ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
		AssistChip(
			onClick = {},
			label = {
				Crossfade(
					targetState = state,
					label = "Current device connection state",
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
				containerColor = animatedColor,
				labelColor = contentColorFor(backgroundColor = animatedColor)
			),
			elevation = AssistChipDefaults.assistChipElevation(),
			modifier = modifier,
		)
	}
}

private val BLEConnectionState.color: Color
	@Composable
	get() = when (this) {
		BLEConnectionState.CONNECTING -> MaterialTheme.colorScheme.surfaceContainer
		BLEConnectionState.CONNECTED -> MaterialTheme.colorScheme.primaryContainer
		BLEConnectionState.DISCONNECTED -> MaterialTheme.colorScheme.tertiaryContainer
		BLEConnectionState.DISCONNECTING -> MaterialTheme.colorScheme.secondaryContainer
		BLEConnectionState.FAILED -> MaterialTheme.colorScheme.surfaceContainer
	}

private val BLEConnectionState.textResource: String
	@Composable
	get() = when (this) {
		BLEConnectionState.DISCONNECTED -> stringResource(id = R.string.ble_connection_disconnnected)
		BLEConnectionState.FAILED -> stringResource(id = R.string.ble_connection_failed)
		BLEConnectionState.DISCONNECTING -> stringResource(id = R.string.ble_connection_disconnecting)
		BLEConnectionState.CONNECTING -> stringResource(R.string.ble_connection_connecting)
		BLEConnectionState.CONNECTED -> stringResource(id = R.string.ble_connection_connected)
	}

class BLEConnectionStatesPreviewParams :
	CollectionPreviewParameterProvider<BLEConnectionState>(BLEConnectionState.entries)

@PreviewLightDark
@Composable
private fun BLEConnectionStatusChipPreview(
	@PreviewParameter(BLEConnectionStatesPreviewParams::class)
	state: BLEConnectionState,
) = BlueToothTerminalAppTheme {
	BLEConnectionStatusChip(state = state)
}