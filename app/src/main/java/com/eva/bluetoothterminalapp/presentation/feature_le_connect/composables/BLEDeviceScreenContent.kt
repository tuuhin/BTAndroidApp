package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLEDeviceProfileState

@Composable
fun BLEDeviceScreenContent(
	profile: BLEDeviceProfileState,
	onCharacteristicSelect: (service: BLEServiceModel, characteristic: BLECharacteristicsModel) -> Unit,
	modifier: Modifier = Modifier,
	selectedCharacteristic: BLECharacteristicsModel? = null,
	contentPadding: PaddingValues = PaddingValues.Zero
) {
	val servicesCount by remember(profile.services) { derivedStateOf { profile.services.size } }
	val isConnectedOrConnecting = remember(profile.connectionState) {
		profile.connectionState in arrayOf(
			BLEConnectionState.CONNECTED,
			BLEConnectionState.CONNECTING
		)
	}

	AnimatedContent(
		targetState = profile.device != null,
		contentAlignment = Alignment.Center,
		modifier = modifier
	) { isReady ->
		if (isReady && profile.device != null) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(contentPadding),
				verticalArrangement = Arrangement.spacedBy(4.dp)
			) {
				BLEDeviceProfile(
					device = profile.device,
					connectionState = profile.connectionState,
					rssi = profile.signalStrength,
					modifier = Modifier.animateEnterExit(
						enter = slideInVertically(
							animationSpec = tween(durationMillis = 200, easing = EaseIn)
						) { height -> height } + scaleIn(
							initialScale = .75f,
							animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
						),
						exit = slideOutVertically(
							animationSpec = tween(durationMillis = 200, easing = EaseOut)
						) { height -> -height } + scaleOut(
							targetScale = .75f,
							animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
						),
					)
				)
				AnimatedVisibility(
					visible = isConnectedOrConnecting
				) {
					Row(
						horizontalArrangement = Arrangement.spacedBy(12.dp),
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.background(MaterialTheme.colorScheme.surface)
							.fillMaxWidth()
							.heightIn(min = 40.dp)
					) {
						Text(
							text = stringResource(id = R.string.le_available_devices),
							style = MaterialTheme.typography.titleLarge,
							modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
						)
						Surface(
							color = MaterialTheme.colorScheme.primaryContainer,
							contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
							shape = MaterialTheme.shapes.small,
						) {
							Text(
								text = "$servicesCount",
								style = MaterialTheme.typography.bodyMedium,
								fontWeight = FontWeight.SemiBold,
								modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
							)
						}
					}
				}
				AnimatedVisibility(
					visible = profile.connectionState == BLEConnectionState.CONNECTED,
					enter = slideInVertically(animationSpec = tween(easing = FastOutLinearInEasing)) { height -> height } + fadeIn(),
					exit = slideOutVertically(animationSpec = tween(easing = FastOutLinearInEasing)) { height -> height } + fadeOut(),
					modifier = Modifier.weight(1f)
				) {
					BLEServicesList(
						services = profile.services,
						selectedCharacteristic = selectedCharacteristic,
						onCharacteristicSelect = onCharacteristicSelect,
						modifier = Modifier.fillMaxSize()
					)
				}
			}
		} else Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(contentPadding),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			CircularProgressIndicator()
			Spacer(modifier = Modifier.height(12.dp))
			Text(
				text = stringResource(R.string.ble_device_connecting),
				style = MaterialTheme.typography.titleLarge,
				color = MaterialTheme.colorScheme.onSurface
			)
		}
	}
}