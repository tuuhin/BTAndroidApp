package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_settings.util.BLESettingsEvent
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLESettingsContent(
	settings: BLESettingsModel,
	onEvent: (BLESettingsEvent) -> Unit,
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp)
) {
	val colors =
		ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

	LazyColumn(
		modifier = modifier.fillMaxSize(),
		contentPadding = contentPadding,
		verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
	) {
		bTListItemTitle {
			Text(text = stringResource(id = R.string.ble_settings_category_scanner))
		}
		item {
			BLEScanPeriodSelector(
				shapes = ListItemDefaults.segmentedShapes(0, 4),
				colors = colors,
				scanPeriodTime = settings.scanPeriod,
				onScanPeriodChange = { time -> onEvent(BLESettingsEvent.OnScanPeriodChange(time)) },
			)
		}
		item {
			BLEScanModeSelector(
				shapes = ListItemDefaults.segmentedShapes(1, 4),
				colors = colors,
				scanMode = settings.scanMode,
				onScanModeChange = { mode -> onEvent(BLESettingsEvent.OnScanModeChange(mode)) },
			)
		}
		item {
			BLEPhysicalLayerSelector(
				shapes = ListItemDefaults.segmentedShapes(2, 4),
				colors = colors,
				selectedLayer = settings.supportedLayer,
				onLayerChange = { layer -> onEvent(BLESettingsEvent.OnPhyLayerChange(layer)) }
			)
		}
		item {
			SegmentedListItem(
				shapes = ListItemDefaults.segmentedShapes(3, 4),
				colors = colors,
				checked = settings.isLegacyOnly,
				onCheckedChange = { isLegacy ->
					onEvent(BLESettingsEvent.OnToggleIsLegacyAdvertisement(isLegacy))
				},
				trailingContent = {
					Switch(
						checked = settings.isLegacyOnly,
						onCheckedChange = null
					)
				},
				content = { Text(text = stringResource(id = R.string.ble_settings_compatibility_mode_title)) },
				supportingContent = {
					if (settings.isLegacyOnly) Text(text = stringResource(id = R.string.ble_settings_compatibility_mode_legacy_only))
					else Text(text = stringResource(id = R.string.ble_settings_compatibility_mode_all))
				},
			)
		}
	}
}

class BLESettingsPreviewParams : CollectionPreviewParameterProvider<BLESettingsModel>(
	listOf(
		PreviewFakes.FAKE_BLE_SETTINGS,
		PreviewFakes.FAKE_BLE_SETTINGS_2
	)
)


@PreviewLightDark
@Composable
private fun BLESettingsContentPreview(
	@PreviewParameter(BLESettingsPreviewParams::class)
	settings: BLESettingsModel,
) = BlueToothTerminalAppTheme {
	Surface {
		BLESettingsContent(
			settings = settings,
			onEvent = {},
			contentPadding = PaddingValues(12.dp),
			modifier = Modifier.fillMaxSize()
		)
	}
}