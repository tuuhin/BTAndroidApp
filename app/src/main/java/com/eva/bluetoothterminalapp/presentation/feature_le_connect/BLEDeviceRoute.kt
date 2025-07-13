package com.eva.bluetoothterminalapp.presentation.feature_le_connect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEDeviceProfile
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEDeviceRouteTopBar
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEServicesList
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLECharacteristicEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLEDeviceConfigEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLEDeviceProfileState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.SelectedCharacteristicState
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLEDeviceRoute(
	profile: BLEDeviceProfileState,
	selectedCharacteristic: SelectedCharacteristicState,
	onSelectEvent: (BLECharacteristicEvent) -> Unit,
	modifier: Modifier = Modifier,
	onConfigEvent: (BLEDeviceConfigEvent) -> Unit = {},
	navigation: @Composable () -> Unit = {},
) {
	val scrollConnection = TopAppBarDefaults.pinnedScrollBehavior()

	Scaffold(
		topBar = {
			BLEDeviceRouteTopBar(
				connectionState = profile.connectionState,
				onConfigEvent = onConfigEvent,
				navigation = navigation,
				scrollConnection = scrollConnection,
			)
		},
		modifier = modifier.nestedScroll(scrollConnection.nestedScrollConnection)
	) { scPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(scPadding)
				.padding(horizontal = dimensionResource(id = R.dimen.sc_padding)),
			verticalArrangement = Arrangement.spacedBy(6.dp)
		) {
			BLEDeviceProfile(
				device = profile.device,
				connectionState = profile.connectionState,
				rssi = profile.signalStrength,
			)
			AnimatedVisibility(
				visible = profile.connectionState == BLEConnectionState.CONNECTED,
				enter = slideInVertically(animationSpec = tween(easing = FastOutLinearInEasing)) { height -> height } + fadeIn(),
				exit = slideOutVertically(animationSpec = tween(easing = FastOutLinearInEasing)) { height -> height } + fadeOut(),
			) {
				BLEServicesList(
					services = profile.services,
					selectedCharacteristic = selectedCharacteristic.characteristic,
					onCharacteristicSelect = { service, characteristics ->
						val event = BLECharacteristicEvent.OnSelectCharacteristic(
							service,
							characteristics
						)
						onSelectEvent(event)
					},
					modifier = Modifier.fillMaxSize()
				)
			}
		}
	}
}

private class BTLEDevicesProfilePreviewParams
	: CollectionPreviewParameterProvider<BLEDeviceProfileState>(
	listOf(
		PreviewFakes.FAKE_BLE_PROFILE_STATE,
		PreviewFakes.FAKE_BLE_PROFILE_STATE_CONNECTING
	)
)

@PreviewLightDark
@Composable
private fun BLEDevicesRoutePreview(
	@PreviewParameter(BTLEDevicesProfilePreviewParams::class)
	profile: BLEDeviceProfileState,
) = BlueToothTerminalAppTheme {
	BLEDeviceRoute(
		profile = profile,
		selectedCharacteristic = SelectedCharacteristicState(),
		onSelectEvent = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = "Back",
				modifier = Modifier.padding(ButtonDefaults.ContentPadding)
			)
		},
	)
}