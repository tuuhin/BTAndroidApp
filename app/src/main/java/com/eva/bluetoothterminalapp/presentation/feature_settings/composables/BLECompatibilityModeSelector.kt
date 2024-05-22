package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R

@Composable
fun BLECompatibilityModeSelector(
	isLegacyOnly: Boolean,
	onChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier
) {
	ListItem(
		headlineContent = { Text(text = stringResource(id = R.string.ble_settings_compatibilty_mode_title)) },
		supportingContent = {
			AnimatedContent(
				targetState = isLegacyOnly,
				transitionSpec = {
					if (targetState > initialState) {
						slideInVertically { height -> -height } + fadeIn(initialAlpha = .25f) togetherWith
								slideOutVertically { height -> height } + fadeOut(targetAlpha = .25f)
					} else {
						slideInVertically { height -> height } + fadeIn(initialAlpha = .25f) togetherWith
								slideOutVertically { height -> -height } + fadeOut(targetAlpha = .25f)
					}
				},
			) { isSelected ->
				if (isSelected) {
					Text(text = stringResource(id = R.string.ble_settings_compatibilty_mode_legacy_only))
				} else {
					Text(text = stringResource(id = R.string.ble_settings_compatibilty_mode_all))
				}
			}
		},
		trailingContent = {
			Switch(
				checked = isLegacyOnly,
				onCheckedChange = onChange
			)
		},
		tonalElevation = 2.dp,
		modifier = modifier
			.clip(MaterialTheme.shapes.medium)
			.clickable { onChange(!isLegacyOnly) }
	)
}