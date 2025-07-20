package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.color
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.textResource
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
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.SemiBold,
					textAlign = TextAlign.Center,
				)
			}
		},
		border = AssistChipDefaults.assistChipBorder(
			enabled = true,
			borderColor = contentColorFor(state.color),
		),
		shape = shape,
		colors = AssistChipDefaults.assistChipColors(
			containerColor = animatedColor,
			labelColor = contentColorFor(backgroundColor = state.color)
		),
		elevation = AssistChipDefaults.assistChipElevation(),
		modifier = modifier,
	)
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