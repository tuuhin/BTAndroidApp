package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R

@Composable
fun BTShowTimestampSelector(
	showTimeStamp: Boolean,
	onChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier
) {
	ListItem(
		headlineContent = { Text(text = stringResource(id = R.string.bt_classic_settings_show_time_title)) },
		supportingContent = { Text(text = stringResource(id = R.string.bt_classic_settings_show_time_desc)) },
		trailingContent = {
			Switch(
				checked = showTimeStamp,
				onCheckedChange = onChange,
				colors = SwitchDefaults.colors(
					checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
					checkedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
					checkedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer
				)
			)
		},
		tonalElevation = 2.dp,
		modifier = modifier
			.clip(MaterialTheme.shapes.medium)
			.clickable { onChange(!showTimeStamp) }
	)
}