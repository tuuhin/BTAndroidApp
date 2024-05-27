package com.eva.bluetoothterminalapp.presentation.feature_settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_settings.composables.BLESettingsContent
import com.eva.bluetoothterminalapp.presentation.feature_settings.composables.BTSettingsContent
import com.eva.bluetoothterminalapp.presentation.feature_settings.composables.BTSettingsTabs
import com.eva.bluetoothterminalapp.presentation.feature_settings.util.BLESettingsEvent
import com.eva.bluetoothterminalapp.presentation.feature_settings.util.BTSettingsEvent
import com.eva.bluetoothterminalapp.presentation.util.BluetoothTypes
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsRoute(
	bleSettings: BLESettingsModel,
	btSettings: BTSettingsModel,
	onBLEEvent: (BLESettingsEvent) -> Unit,
	onBTEvent: (BTSettingsEvent) -> Unit,
	modifier: Modifier = Modifier,
	initialTab: BluetoothTypes = BluetoothTypes.CLASSIC,
	navigation: @Composable () -> Unit = {},
) {
	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(id = R.string.settings_route_title)) },
				scrollBehavior = scrollBehavior,
				navigationIcon = navigation
			)
		},
		modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
	) { scPadding ->
		BTSettingsTabs(
			initialTab = initialTab,
			contentPadding = scPadding,
			classicTabContent = {
				BTSettingsContent(
					settings = btSettings,
					onEvent = onBTEvent,
					modifier = Modifier.fillMaxSize()
				)
			},
			leTabContent = {
				BLESettingsContent(
					settings = bleSettings,
					onEvent = onBLEEvent,
					modifier = Modifier.fillMaxSize()
				)
			},
		)

	}
}

private class BluetoothTypesPreviewParams : CollectionPreviewParameterProvider<BluetoothTypes>(
	listOf(
		BluetoothTypes.LOW_ENERGY,
		BluetoothTypes.CLASSIC
	)
)

@PreviewLightDark
@Composable
private fun AppSettingsRoutePreview(
	@PreviewParameter(BluetoothTypesPreviewParams::class)
	initialTab: BluetoothTypes,
) = BlueToothTerminalAppTheme {
	AppSettingsRoute(
		initialTab = initialTab,
		bleSettings = PreviewFakes.FAKE_BLE_SETTINGS,
		btSettings = PreviewFakes.FAKE_BT_SETTINGS,
		onBTEvent = {},
		onBLEEvent = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		},
	)
}