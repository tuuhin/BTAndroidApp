package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
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
		modifier = modifier.fillMaxSize()
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
							animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
						) { height -> height } + fadeIn(
							animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
						),
						exit = slideOutVertically(
							animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
						) { height -> -height } + fadeOut(
							animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
						),
					)
				)
				AnimatedVisibility(
					visible = isConnectedOrConnecting,
					modifier = Modifier.padding(vertical = 4.dp)
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
							modifier = Modifier
								.padding(horizontal = 4.dp, vertical = 6.dp)
								.weight(1f)
						)
						Surface(
							color = MaterialTheme.colorScheme.primaryContainer,
							contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
							shape = MaterialShapes.Sunny.toShape(),
						) {
							Text(
								text = "$servicesCount",
								style = MaterialTheme.typography.bodyMedium,
								fontWeight = FontWeight.SemiBold,
								modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
							)
						}
					}
				}
				AnimatedVisibility(
					visible = profile.connectionState == BLEConnectionState.CONNECTED,
					enter = slideInVertically(
						animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
					) { height -> height } + fadeIn(MaterialTheme.motionScheme.defaultEffectsSpec()),
					exit = slideOutVertically(
						animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
					) { height -> height } + fadeOut(MaterialTheme.motionScheme.fastEffectsSpec()),
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
			LoadingIndicator(modifier = Modifier.size(36.dp))
			Spacer(modifier = Modifier.height(12.dp))
			Text(
				text = stringResource(R.string.ble_device_connecting),
				style = MaterialTheme.typography.titleLargeEmphasized,
				color = MaterialTheme.colorScheme.onSurface
			)
		}
	}
}