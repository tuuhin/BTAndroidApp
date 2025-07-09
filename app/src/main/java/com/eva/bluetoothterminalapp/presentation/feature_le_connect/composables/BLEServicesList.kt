package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BLEServicesList(
	services: ImmutableList<BLEServiceModel>,
	onCharacteristicSelect: (service: BLEServiceModel, characteristic: BLECharacteristicsModel) -> Unit,
	modifier: Modifier = Modifier,
	selectedCharacteristic: BLECharacteristicsModel? = null,
	contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {

	val isLocalInspectionMode = LocalInspectionMode.current

	val lazyColumKeys: ((Int, BLEServiceModel) -> Any)? = remember {
		if (isLocalInspectionMode) null
		else { _, service -> service.serviceId }
	}

	LazyColumn(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		contentPadding = contentPaddingValues,
		modifier = modifier,
	) {
		stickyHeader {
			Box(
				modifier = Modifier
					.background(MaterialTheme.colorScheme.surface)
					.fillMaxWidth()
			) {
				Text(
					text = stringResource(id = R.string.le_available_devices),
					style = MaterialTheme.typography.titleMedium,
					modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
				)
			}
		}
		itemsIndexed(
			items = services,
			key = lazyColumKeys,
			contentType = { _, _ -> BLEServiceModel::class.simpleName },
		) { _, service ->
			BLEDeviceServiceCard(
				bleService = service,
				selectedCharacteristic = selectedCharacteristic,
				onCharacteristicSelect = { onCharacteristicSelect(service, it) },
				modifier = Modifier
					.fillMaxWidth()
					.animateItem()
			)
		}
	}
}