package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState

@Composable
fun BLEConnectionChip(
	mode: BLEConnectionState,
	modifier: Modifier = Modifier,
	colors: ChipColors = AssistChipDefaults.assistChipColors(
		containerColor = MaterialTheme.colorScheme.primaryContainer,
	),
	elevation: ChipElevation = AssistChipDefaults.elevatedAssistChipElevation(),
) {

	ProvideTextStyle(value = MaterialTheme.typography.titleSmall) {
		AssistChip(
			onClick = {},
			label = {
				Crossfade(
					targetState = mode,
					label = "Current client state",
					animationSpec = tween(durationMillis = 300, easing = LinearEasing),
					modifier = Modifier.padding(4.dp)
				) { state ->
					Text(
						text = state.name,
						textAlign = TextAlign.Center
					)
				}
			},
			shape = MaterialTheme.shapes.medium,
			colors = colors,
			elevation = elevation,
			modifier = modifier,
		)
	}
}