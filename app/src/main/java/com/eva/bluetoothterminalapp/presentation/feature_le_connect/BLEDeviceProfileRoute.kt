package com.eva.bluetoothterminalapp.presentation.feature_le_connect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEProfileWithServicesList
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileState
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTLEDeviceProfileRoute(
	profile: BLEDeviceProfileState,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	onProfileSelect: () -> Unit = {},
) {

	val scrollConnection = TopAppBarDefaults.pinnedScrollBehavior()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = "LE Device Profile") },
				scrollBehavior = scrollConnection,
				navigationIcon = navigation,
				actions = {
					IconButton(onClick = onProfileSelect) {
						Icon(
							imageVector = Icons.Default.Check,
							contentDescription = null
						)
					}
				}
			)
		},
		modifier = modifier
			.nestedScroll(scrollConnection.nestedScrollConnection)
	) { scPadding ->
		BLEProfileWithServicesList(
			state = profile,
			modifier = Modifier
				.padding(scPadding)
				.fillMaxSize(),
		)
	}
}

@PreviewLightDark
@Composable
private fun BTLEDevicesProfileRoute() = BlueToothTerminalAppTheme {
	BTLEDeviceProfileRoute(
		profile = PreviewFakes.FAKE_BLE_PROFILE_STATE,
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = "Back",
				modifier = Modifier.padding(ButtonDefaults.ContentPadding)
			)
		},
	)
}