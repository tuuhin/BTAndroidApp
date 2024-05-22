package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_settings.util.BLESettingsEvent
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLESettingsContent(
	settings: BLESettingsModel,
	onEvent: (BLESettingsEvent) -> Unit,
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp)
) {
	LazyColumn(
		modifier = modifier,
		contentPadding = contentPadding,
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {

		item {
			BLEScanPeriodSelector(
				scanPeriodTime = settings.scanPeriod,
				onScanPeriodChange = { time -> onEvent(BLESettingsEvent.OnScanPeriodChange(time)) },
			)
		}
		item {
			BLEScanModeSelector(
				scanMode = settings.scanMode,
				onScanModeChange = { mode ->
					onEvent(BLESettingsEvent.OnScanModeChange(mode))
				},
			)
		}
		item {
			BLEPhysicalLayerSelector(
				selectedLayer = settings.supportedLayer,
				onLayerChange = { layer ->
					onEvent(BLESettingsEvent.OnPhyLayerChange(layer))
				}
			)
		}
		item {
			BLECompatibilityModeSelector(
				isLegacyOnly = settings.isLegacyOnly,
				onChange = { onEvent(BLESettingsEvent.OnToggleIsLegacyAdvertisement(it)) }
			)
		}
	}
}


@PreviewLightDark
@Composable
private fun BLESettingsContentPreview() = BlueToothTerminalAppTheme {
	Surface {
		BLESettingsContent(
			settings = BLESettingsModel(),
			onEvent = {},
			contentPadding = PaddingValues(12.dp),
			modifier = Modifier.fillMaxSize()
		)
	}
}