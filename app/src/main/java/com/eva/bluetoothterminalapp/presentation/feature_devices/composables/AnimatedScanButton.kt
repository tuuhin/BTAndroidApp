package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R

@Composable
fun AnimatedScanButton(
	isScanning: Boolean,
	canShowScanOption: Boolean,
	modifier: Modifier = Modifier,
	startScan: () -> Unit = {},
	stopScan: () -> Unit = {},
	style: TextStyle = MaterialTheme.typography.titleMedium,
	colors: ButtonColors = ButtonDefaults
		.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
) {

	val actionState by remember(canShowScanOption, isScanning) {
		derivedStateOf {
			when {
				canShowScanOption && isScanning -> ScanButtonState.STOP_SCAN
				canShowScanOption && !isScanning -> ScanButtonState.START_SCAN
				else -> ScanButtonState.BLANK
			}
		}
	}

	AnimatedContent(
		targetState = actionState,
		label = "Start or stop scan button",
		transitionSpec = {
			if (targetState > initialState) {
				slideInVertically { height -> -height } + fadeIn(initialAlpha = .25f) togetherWith
						slideOutVertically { height -> height } + fadeOut(targetAlpha = .25f)
			} else {
				slideInVertically { height -> height } + fadeIn(initialAlpha = .25f) togetherWith
						slideOutVertically { height -> -height } + fadeOut(targetAlpha = .25f)
			}
		},
		contentAlignment = Alignment.Center,
		modifier = modifier,
	) { state ->
		when (state) {
			ScanButtonState.STOP_SCAN -> TextButton(
				onClick = stopScan,
				colors = colors,
				enabled = !transition.isRunning
			) {
				Text(
					text = stringResource(id = R.string.stop_bluetooth_scan),
					style = style
				)
			}

			ScanButtonState.START_SCAN -> TextButton(
				onClick = startScan,
				colors = colors,
				enabled = !transition.isRunning
			) {
				Text(
					text = stringResource(id = R.string.start_bluetooth_scan),
					style = style
				)
			}

			ScanButtonState.BLANK -> Box(modifier = Modifier.size(60.dp, 40.dp))
		}
	}
}

private enum class ScanButtonState {
	BLANK,
	STOP_SCAN,
	START_SCAN
}