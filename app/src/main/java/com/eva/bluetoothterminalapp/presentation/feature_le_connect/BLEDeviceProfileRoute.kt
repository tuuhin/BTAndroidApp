package com.eva.bluetoothterminalapp.presentation.feature_le_connect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEConnectionChip
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEDeviceProfile
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEProfileTopBar
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEServicesList
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileState
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTLEDeviceProfileRoute(
	profile: BLEDeviceProfileState,
	onProfileEvent: (BLEDeviceProfileEvent) -> Unit,
	modifier: Modifier = Modifier,
	selectedCharacteristic: BLECharacteristicsModel? = null,
	navigation: @Composable () -> Unit = {},
) {
	val scrollConnection = TopAppBarDefaults.pinnedScrollBehavior()

	Scaffold(
		topBar = {
			BLEProfileTopBar(
				onSelect = { onProfileEvent(BLEDeviceProfileEvent.OnCharcteristicsConfirmed) },
				scrollConnection = scrollConnection,
				navigation = navigation,
				showAction = selectedCharacteristic != null
			)
		},
		modifier = modifier.nestedScroll(scrollConnection.nestedScrollConnection)
	) { scPadding ->
		Column(
			modifier = Modifier
				.padding(scPadding)
				.padding(horizontal = dimensionResource(id = R.dimen.sc_padding)),
		) {
			BLEConnectionChip(
				state = profile.connectionState,
				modifier = Modifier.align(Alignment.CenterHorizontally)
			)
			BLEDeviceProfile(
				device = profile.device,
				rssi = profile.signalStrength,
			)
			AnimatedVisibility(
				visible = profile.connectionState == BLEConnectionState.CONNECTED,
				enter = slideInVertically(
					animationSpec = tween(easing = FastOutLinearInEasing)
				) { height -> height },
				exit = slideOutVertically(
					animationSpec = tween(easing = FastOutLinearInEasing)
				) { height -> -height },
			) {
				BLEServicesList(
					services = profile.services,
					selectedCharacteristic = selectedCharacteristic,
					onCharacteristicSelect = { characteristics ->
						onProfileEvent(BLEDeviceProfileEvent.OnSelectCharacteristic(characteristics))
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
private fun BTLEDevicesProfileRoute(
	@PreviewParameter(BTLEDevicesProfilePreviewParams::class)
	profile: BLEDeviceProfileState,
) = BlueToothTerminalAppTheme {
	BTLEDeviceProfileRoute(
		profile = profile,
		onProfileEvent = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = "Back",
				modifier = Modifier.padding(ButtonDefaults.ContentPadding)
			)
		},
	)
}