package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel

@Composable
fun BLEServiceContent(
	service: BLEServiceModel,
	modifier: Modifier = Modifier,
) {

	val context = LocalContext.current

	val characteristicsSize by remember(service.characteristic) {
		derivedStateOf { service.characteristic.size }
	}

	val serviceTypeText by remember(service.serviceType) {
		derivedStateOf {
			when (service.serviceType) {
				BLEServicesTypes.PRIMARY -> context.getString(R.string.ble_service_primary)
				BLEServicesTypes.SECONDARY -> context.getString(R.string.ble_service_secondary)
				BLEServicesTypes.UNKNOWN -> context.getString(R.string.ble_service_unknown)
			}
		}
	}

	val serviceNameReadable by remember(service.bleServiceName) {
		derivedStateOf { service.bleServiceName ?: "Unknown Service" }
	}

	Column(modifier = modifier) {
		Text(
			text = serviceNameReadable,
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.titleMedium
		)
		Text(
			text = "UUID: ${service.serviceUUID}",
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)

		Text(
			text = buildString {
				append("Service Type :")
				append(serviceTypeText)
			},
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Text(
			text = buildString {
				append("Characteristics :")
				append(characteristicsSize)
			},
			style = MaterialTheme.typography.labelMedium
		)
	}
}