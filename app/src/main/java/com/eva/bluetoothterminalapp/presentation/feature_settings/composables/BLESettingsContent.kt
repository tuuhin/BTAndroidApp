package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.settings.enums.BLEScanPeriodTimmings
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsScanMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsSupportedLayer
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

	val onScanPeriodChange: (BLEScanPeriodTimmings) -> Unit = remember {
		{ time -> onEvent(BLESettingsEvent.OnScanPeriodChange(time)) }
	}

	val onScanModeChange: (BLESettingsScanMode) -> Unit = remember {
		{ mode -> onEvent(BLESettingsEvent.OnScanModeChange(mode)) }
	}

	val onSupportedLayerChange: (BLESettingsSupportedLayer) -> Unit = remember {
		{ layer -> onEvent(BLESettingsEvent.OnPhyLayerChange(layer)) }
	}

	val onIsLegacyDeviceChange: (Boolean) -> Unit = remember {
		{ isLegacy -> onEvent(BLESettingsEvent.OnToggleIsLegacyAdvertisement(isLegacy)) }
	}

	LazyColumn(
		modifier = modifier.fillMaxSize(),
		contentPadding = contentPadding,
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		bTListItemTitle {
			Text(text = stringResource(id = R.string.ble_settings_category_scanner))
		}
		item {
			BLEScanPeriodSelector(
				scanPeriodTime = settings.scanPeriod,
				onScanPeriodChange = onScanPeriodChange,
			)
		}
		item {
			BLEScanModeSelector(
				scanMode = settings.scanMode,
				onScanModeChange = onScanModeChange,
			)
		}
		item {
			BLEPhysicalLayerSelector(
				selectedLayer = settings.supportedLayer,
				onLayerChange = onSupportedLayerChange
			)
		}
		item {
			BLECompatibilityModeSelector(
				isLegacyOnly = settings.isLegacyOnly,
				onChange = onIsLegacyDeviceChange
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