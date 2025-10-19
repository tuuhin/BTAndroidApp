package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BLEServicesList(
	services: ImmutableList<BLEServiceModel>,
	onCharacteristicSelect: (service: BLEServiceModel, characteristic: BLECharacteristicsModel) -> Unit,
	modifier: Modifier = Modifier,
	selectedCharacteristic: BLECharacteristicsModel? = null,
	contentPadding: PaddingValues = PaddingValues.Zero
) {

	val isLocalInspectionMode = LocalInspectionMode.current

	val lazyColumKeys: ((Int, BLEServiceModel) -> Any)? = remember {
		if (isLocalInspectionMode) null
		else { _, service -> service.serviceId }
	}

	LazyColumn(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		contentPadding = contentPadding,
		modifier = modifier.clip(MaterialTheme.shapes.medium),
	) {
		if (services.isNotEmpty()) {
			itemsIndexed(
				items = services,
				key = lazyColumKeys,
				contentType = { _, _ -> BLEServiceModel::class.simpleName },
			) { _, service ->
				BLEDeviceServiceCard(
					bleService = service,
					selectedCharacteristic = selectedCharacteristic,
					onCharacteristicSelect = { char -> onCharacteristicSelect(service, char) },
					modifier = Modifier
						.fillMaxWidth()
						.animateItem()
						.animateContentSize()
				)
			}
		} else item {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.heightIn(min = 120.dp)
					.animateItem(),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = stringResource(R.string.ble_device_no_service_found),
					textAlign = TextAlign.Center,
				)
			}
		}
	}
}