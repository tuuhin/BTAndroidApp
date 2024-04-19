package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
	AnimatedVisibility(
		visible = canShowScanOption,
		enter = slideInVertically(),
		exit = slideOutVertically(),
		modifier = modifier
	) {

		AnimatedContent(
			targetState = !isScanning,
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
		) { normal ->
			if (normal)
				TextButton(onClick = startScan, colors = colors) {
					Text(
						text = stringResource(id = R.string.start_bluetooth_scan),
						style = style
					)
				}
			else TextButton(onClick = stopScan, colors = colors) {
				Text(
					text = stringResource(id = R.string.stop_bluetooth_scan),
					style = style
				)
			}
		}
	}
}