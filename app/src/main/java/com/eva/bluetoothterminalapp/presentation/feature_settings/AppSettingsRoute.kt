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
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_settings.composables.BLESettingsContent
import com.eva.bluetoothterminalapp.presentation.feature_settings.composables.BTSettingsTabs
import com.eva.bluetoothterminalapp.presentation.feature_settings.util.BLESettingsEvent
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsRoute(
	bleSettings: BLESettingsModel,
	onBLEEvent: (BLESettingsEvent) -> Unit,
	modifier: Modifier = Modifier,
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
			classicTabContent = {},
			leTabContent = {
				BLESettingsContent(
					settings = bleSettings,
					onEvent = onBLEEvent,
					modifier = Modifier.fillMaxSize()
				)
			},
			contentPadding = scPadding
		)

	}
}

@PreviewLightDark
@Composable
private fun AppSettingsRoutePreview() = BlueToothTerminalAppTheme {
	AppSettingsRoute(
		bleSettings = BLESettingsModel(),
		onBLEEvent = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		},
	)
}