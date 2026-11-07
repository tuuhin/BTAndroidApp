package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.settings.enums.BLEScanPeriodTimmings
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEScanPeriodSelector(
	scanPeriodTime: BLEScanPeriodTimmings,
	onScanPeriodChange: (BLEScanPeriodTimmings) -> Unit,
	modifier: Modifier = Modifier,
	colors: ListItemColors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
	shapes: ListItemShapes = ListItemDefaults.segmentedShapes(0, 1),
) {
	var showDialog by rememberSaveable { mutableStateOf(false) }

	SegmentedListItem(
		shapes = shapes,
		colors = colors,
		onClick = { showDialog = true },
		content = { Text(text = stringResource(id = R.string.ble_settings_scan_period_title)) },
		supportingContent = { Text(text = scanPeriodTime.textResource) },
		modifier = modifier
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
				BLEScanPeriodTimmings.entries.forEach { timmings ->
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.clip(MaterialTheme.shapes.medium)
							.clickable {
								onScanPeriodChange(timmings)
								showDialog = false
							}
					) {
						RadioButton(
							selected = scanPeriodTime == timmings,
							onClick = {
								onScanPeriodChange(timmings)
								showDialog = false
							},
							colors = RadioButtonDefaults
								.colors(selectedColor = MaterialTheme.colorScheme.secondary)
						)
						Text(
							text = timmings.textResource,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				}
			}
		},
	)
}

private val BLEScanPeriodTimmings.textResource: String
	@Composable
	get() = when (this) {

		BLEScanPeriodTimmings.TWELVE_SECONDS -> stringResource(
			R.string.ble_settings_scan_period_time_seconds,
			12
		)

		BLEScanPeriodTimmings.FOURTY_FIVE_SECONDS -> stringResource(
			R.string.ble_settings_scan_period_time_seconds,
			45
		)

		BLEScanPeriodTimmings.ONE_MINUTE -> stringResource(
			R.string.ble_settings_scan_period_time_minute,
			1
		)

		BLEScanPeriodTimmings.THREE_MINUTE -> stringResource(
			R.string.ble_settings_scan_period_time_minute,
			3
		)

		BLEScanPeriodTimmings.FIVE_MINUTES -> stringResource(
			R.string.ble_settings_scan_period_time_minute,
			5
		)
	}

@PreviewLightDark
@Composable
private fun BLEScanPeriodSelectorPreview() = BlueToothTerminalAppTheme {
	BLEScanPeriodSelector(
		scanPeriodTime = BLEScanPeriodTimmings.ONE_MINUTE,
		onScanPeriodChange = {},
	)
}