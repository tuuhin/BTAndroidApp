package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalCharSet
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTCharsetSelector(
	selectedCharset: BTTerminalCharSet,
	onCharsetChange: (BTTerminalCharSet) -> Unit,
	modifier: Modifier = Modifier
) {
	var showDialog by remember { mutableStateOf(false) }

	ListItem(
		headlineContent = {
			Text(text = stringResource(id = R.string.bt_classic_settings_char_sets_title))
		},
		supportingContent = {
			Text(text = selectedCharset.textResource)
		},
		tonalElevation = 2.dp,
		modifier = modifier
			.clip(MaterialTheme.shapes.medium)
			.clickable { showDialog = true }
	)

	if (!showDialog) return

	AlertDialog(
		onDismissRequest = { showDialog = false },
		confirmButton = {
			TextButton(
				onClick = { showDialog = false },
				colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
			) {
				Text(text = stringResource(id = R.string.dialog_action_cancel))
			}
		},
		title = { Text(text = stringResource(id = R.string.ble_settings_layer_title)) },
		text = {
			Column(
				verticalArrangement = Arrangement.spacedBy(2.dp)
			) {
				BTTerminalCharSet.entries.forEach { charset ->
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.clip(MaterialTheme.shapes.medium)
							.clickable {
								onCharsetChange(charset)
								showDialog = false
							}
					) {
						RadioButton(
							selected = selectedCharset == charset,
							onClick = {
								onCharsetChange(charset)
								showDialog = false
							},
							colors = RadioButtonDefaults
								.colors(selectedColor = MaterialTheme.colorScheme.secondary)
						)
						Text(
							text = charset.textResource,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
			}
		},
	)
}

private val BTTerminalCharSet.textResource: String
	@Composable
	get() = when (this) {
		BTTerminalCharSet.CHAR_SET_UTF_8 -> stringResource(id = R.string.bt_classic_settings_char_set_utf_8)
		BTTerminalCharSet.CHAR_SET_UTF_16 -> stringResource(id = R.string.bt_classic_settings_char_set_utf_16)
		BTTerminalCharSet.CHAR_SET_UTF_32 -> stringResource(id = R.string.bt_classic_settings_char_set_utf_32)
		BTTerminalCharSet.CHAR_SET_US_ASCII -> stringResource(id = R.string.bt_classic_settings_char_set_us_ascii)
		BTTerminalCharSet.CHAR_SET_ISO_8859_1 -> stringResource(id = R.string.bt_classic_settings_char_set_iso_8859)
	}

@PreviewLightDark
@Composable
private fun BTCharsetSelectorPreview() = BlueToothTerminalAppTheme {
	BTCharsetSelector(
		selectedCharset = BTTerminalCharSet.CHAR_SET_UTF_8,
		onCharsetChange = {},
	)
}