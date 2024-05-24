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
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTNewLineSelector(
	newLineChar: BTTerminalNewLineChar,
	onNewLineCharChange: (BTTerminalNewLineChar) -> Unit,
	modifier: Modifier = Modifier
) {
	var showDialog by remember { mutableStateOf(false) }

	ListItem(
		headlineContent = {
			Text(text = stringResource(id = R.string.bt_classic_settings_new_line_title))
		},
		supportingContent = {
			Text(text = newLineChar.textResource)
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
		title = { Text(text = stringResource(id = R.string.bt_classic_settings_new_line_title)) },
		text = {
			Column(
				verticalArrangement = Arrangement.spacedBy(2.dp)
			) {
				BTTerminalNewLineChar.entries.forEach { eof ->
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.clip(MaterialTheme.shapes.medium)
							.clickable {
								onNewLineCharChange(eof)
								showDialog = false
							}
					) {
						RadioButton(
							selected = newLineChar == eof,
							onClick = {
								onNewLineCharChange(eof)
								showDialog = false
							},
							colors = RadioButtonDefaults
								.colors(selectedColor = MaterialTheme.colorScheme.secondary)
						)
						Text(
							text = eof.textResource,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
			}
		},
	)
}

private val BTTerminalNewLineChar.textResource: String
	@Composable
	get() = when (this) {
		BTTerminalNewLineChar.NEW_LINE_CR_LF -> stringResource(id = R.string.bt_classic_settings_new_line_char_cr_lf)
		BTTerminalNewLineChar.NEW_LINE_CR -> stringResource(id = R.string.bt_classic_settings_new_line_char_cr)
		BTTerminalNewLineChar.NEW_LINE_LF -> stringResource(id = R.string.bt_classic_settings_new_line_char_lf)
		BTTerminalNewLineChar.NEW_LINE_END_OF_TEXT -> stringResource(id = R.string.bt_classic_settings_new_line_char_etx)
		BTTerminalNewLineChar.NEW_LINE_NULL_CHR -> stringResource(id = R.string.bt_classic_settings_new_line_char_null)
		BTTerminalNewLineChar.NEW_LINE_NONE -> stringResource(id = R.string.bt_classic_settings_new_line_none)
	}


@PreviewLightDark
@Composable
private fun BTNewLineSelectorPreview() = BlueToothTerminalAppTheme {
	BTNewLineSelector(
		newLineChar = BTTerminalNewLineChar.NEW_LINE_CR,
		onNewLineCharChange = {},
	)
}