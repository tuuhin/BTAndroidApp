package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileState
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(
	ExperimentalFoundationApi::class
)
@Composable
fun BLEProfileWithServicesList(
	state: BLEDeviceProfileState,
	modifier: Modifier = Modifier,
) {

	val isLocalInspectionMode = LocalInspectionMode.current

	val lazyColumKeys: ((Int, BLEServiceModel) -> Any)? = remember {
		if (isLocalInspectionMode) null
		else { _, service -> service.serviceId }
	}

	Column(
		modifier = modifier.padding(dimensionResource(id = R.dimen.sc_padding)),
		verticalArrangement = Arrangement.spacedBy(4.dp),
	) {
		AnimatedVisibility(
			visible = state.connectionState != BLEConnectionState.UNKNOWN,
			enter = slideInVertically { height -> -height },
			exit = slideOutVertically { height -> height }
		) {
			BLEClientConnectionBanner(
				connectionState = state.connectionState,
				modifier = Modifier.padding(bottom = 12.dp)
			)
		}
		state.device?.let { device ->
			BLEDeviceProfile(
				device = device,
				rssi = state.signalStrength,
			)
		}
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			stickyHeader {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.padding(dimensionResource(id = R.dimen.lazy_colum_content_padding))
				) {
					Text(
						text = stringResource(id = R.string.le_available_devices),
						style = MaterialTheme.typography.titleMedium
					)
				}
			}
			itemsIndexed(
				items = state.services,
				key = lazyColumKeys,
				contentType = { _, device -> device.javaClass.simpleName },
			) { _, service ->
				BLEDeviceServiceCard(
					service = service,
					modifier = Modifier
						.fillMaxWidth()
						.animateItemPlacement()
				)
			}
		}
	}
}


@PreviewLightDark
@Composable
private fun BLEProfileWithServicesListPreview() = BlueToothTerminalAppTheme {
	Surface {
		BLEProfileWithServicesList(
			state = PreviewFakes.FAKE_BLE_PROFILE_STATE,
		)
	}
}