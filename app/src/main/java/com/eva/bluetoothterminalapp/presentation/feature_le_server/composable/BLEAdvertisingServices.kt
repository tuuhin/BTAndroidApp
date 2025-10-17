package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.BLEDeviceServiceCard
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BLEAdvertisingServices(
	services: ImmutableList<BLEServiceModel>,
	modifier: Modifier = Modifier
) {
	val isLocalInspectionMode = LocalInspectionMode.current
	val servicesListKeys: ((Int, BLEServiceModel) -> Any)? = remember {
		if (isLocalInspectionMode) null
		else { _, service -> service.serviceId }
	}

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(40.dp)
				.background(MaterialTheme.colorScheme.background),
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(2.dp),
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = "Services",
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = "List of services the server is advertising",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
			Surface(
				color = MaterialTheme.colorScheme.primaryContainer,
				contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
				shape = MaterialTheme.shapes.medium
			) {
				Text(
					text = "${services.size}",
					style = MaterialTheme.typography.titleMedium,
					modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
				)
			}
		}
		LazyColumn(
			modifier = Modifier.clip(MaterialTheme.shapes.medium),
			verticalArrangement = Arrangement.spacedBy(12.dp),
		) {
			if (services.isNotEmpty()) {
				itemsIndexed(
					items = services,
					key = servicesListKeys,
				) { _, service ->
					BLEDeviceServiceCard(
						bleService = service,
						shape = MaterialTheme.shapes.medium,
						containerColor = MaterialTheme.colorScheme.surfaceContainer,
						modifier = Modifier.animateItem()
					)
				}
			} else {
				item {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.heightIn(min = 40.dp)
							.animateItem(),
						contentAlignment = Alignment.Center
					) {
						Text(
							text = stringResource(R.string.ble_server_no_services),
							textAlign = TextAlign.Center,
						)
					}
				}
			}
		}
	}
}