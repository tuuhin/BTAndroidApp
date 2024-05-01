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
import androidx.compose.material3.Surface
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
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEClientConnectionBanner(
	connectionState: BLEConnectionState,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium
) {

	val animatedCardColors by animateColorAsState(
		targetValue = connectionState.color,
		label = "Card Colors Animation",
	)

	val contentColor = contentColorFor(backgroundColor = animatedCardColors)

	ProvideTextStyle(value = MaterialTheme.typography.titleSmall) {
		AssistChip(
			onClick = {},
			label = {
				Crossfade(
					targetState = connectionState,
					label = "Current client state",
					animationSpec = tween(durationMillis = 300, easing = LinearEasing),
				) { state ->
					Text(
						text = state.textResource,
						style = MaterialTheme.typography.titleMedium,
						color = contentColor,
						modifier = Modifier.padding(12.dp)
					)
				}
			},
			shape = shape,
			modifier = modifier,
			colors = AssistChipDefaults.assistChipColors(
				containerColor = animatedCardColors,
				labelColor = contentColor
			)
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
		BLEConnectionState.UNKNOWN -> MaterialTheme.colorScheme.surfaceContainer
	}

private val BLEConnectionState.textResource: String
	@Composable
	get() = when (this) {
		BLEConnectionState.DISCONNECTED -> stringResource(id = R.string.ble_connection_disconnnected)
		BLEConnectionState.UNKNOWN -> stringResource(id = R.string.ble_connection_unknown)
		BLEConnectionState.DISCONNECTING -> stringResource(id = R.string.ble_connection_disconnecting)
		BLEConnectionState.CONNECTING -> stringResource(R.string.ble_connection_connecting)
		BLEConnectionState.CONNECTED -> stringResource(id = R.string.ble_connection_connected)
	}

private class ConnectionStatesPreviews :
	CollectionPreviewParameterProvider<BLEConnectionState>(BLEConnectionState.entries)

@PreviewLightDark
@Composable
private fun BLEClientConnectionBannerPreview(
	@PreviewParameter(ConnectionStatesPreviews::class)
	connectionState: BLEConnectionState
) = BlueToothTerminalAppTheme {
	Surface {
		BLEClientConnectionBanner(connectionState = connectionState)
	}
}