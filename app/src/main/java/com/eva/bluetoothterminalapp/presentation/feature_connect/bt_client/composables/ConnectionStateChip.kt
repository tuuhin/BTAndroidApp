package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun ClientConnectionStateChip(
	connectionState: ClientConnectionState,
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {},
	elevation: ChipElevation = AssistChipDefaults.elevatedAssistChipElevation(),
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
			shape = MaterialTheme.shapes.medium,
			colors = AssistChipDefaults.assistChipColors(
				containerColor = animatedCardColors,
				labelColor = contentColorFor(connectionState.color)
			),
			elevation = elevation,
			modifier = modifier,
		)
	}
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