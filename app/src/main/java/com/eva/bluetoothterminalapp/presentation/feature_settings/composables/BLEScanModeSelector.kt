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
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsScanMode
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEScanModeSelector(
	scanMode: BLESettingsScanMode,
	onScanModeChange: (BLESettingsScanMode) -> Unit,
	modifier: Modifier = Modifier,
) {

	var showDialog by remember { mutableStateOf(false) }

	ListItem(
		headlineContent = { Text(text = stringResource(id = R.string.ble_settings_scan_mode_title)) },
		supportingContent = { Text(text = scanMode.textResource) },
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
		title = { Text(text = stringResource(id = R.string.ble_settings_scan_mode_title)) },
		text = {
			Column(
				verticalArrangement = Arrangement.spacedBy(2.dp)
			) {
				BLESettingsScanMode.entries.forEach { mode ->
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.clip(MaterialTheme.shapes.medium)
							.clickable {
								onScanModeChange(mode)
								showDialog = false
							}
					) {
						RadioButton(
							selected = scanMode == mode,
							onClick = {
								onScanModeChange(mode)
								showDialog = false
							},
							colors = RadioButtonDefaults
								.colors(selectedColor = MaterialTheme.colorScheme.secondary)
						)
						Text(
							text = mode.textResource,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
			}
		},
	)
}

private val BLESettingsScanMode.textResource: String
	@Composable
	get() = when (this) {
		BLESettingsScanMode.LOW_POWER -> stringResource(id = R.string.ble_settings_scan_mode_low_power)
		BLESettingsScanMode.BALANCED -> stringResource(id = R.string.ble_settings_scan_mode_balanced)
		BLESettingsScanMode.LOW_LATENCY -> stringResource(id = R.string.ble_settings_scan_mode_low_latency)
	}


@PreviewLightDark
@Composable
private fun BLEScanmodeSelectorPreview() = BlueToothTerminalAppTheme {
	BLEScanModeSelector(scanMode = BLESettingsScanMode.BALANCED, onScanModeChange = {})
}