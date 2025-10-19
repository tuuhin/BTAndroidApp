package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BLEConnectedClients(
	connectedClients: ImmutableList<BluetoothDeviceModel>,
	modifier: Modifier = Modifier
) {
	val isLocalInspectionMode = LocalInspectionMode.current
	val bluetoothDeviceListKeys: ((Int, BluetoothDeviceModel) -> Any)? = remember {
		if (isLocalInspectionMode) null
		else { _, service -> service.address }
	}

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(2.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(40.dp)
				.background(MaterialTheme.colorScheme.background),
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(2.dp),
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = stringResource(R.string.ble_server_connected_clients_title),
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = stringResource(R.string.ble_server_connected_clients_text),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
			Surface(
				color = MaterialTheme.colorScheme.primaryContainer,
				contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
				shape = MaterialTheme.shapes.medium
			) {
				AnimatedContent(
					targetState = connectedClients.size,
					transitionSpec = {
						if (targetState > initialState) {
							slideInVertically { height -> height } + fadeIn() togetherWith
									slideOutVertically { height -> -height } + fadeOut()
						} else {
							slideInVertically { height -> -height } + fadeIn() togetherWith
									slideOutVertically { height -> height } + fadeOut()
						}.using(SizeTransform(clip = false))
					},
					modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
				) { targetCount ->
					Text(
						text = "$targetCount",
						style = MaterialTheme.typography.titleLarge
					)
				}
			}
		}
		LazyRow(
			modifier = Modifier.fillMaxWidth(),
			contentPadding = PaddingValues(vertical = 6.dp),
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			if (connectedClients.isNotEmpty()) {
				itemsIndexed(
					items = connectedClients,
					key = bluetoothDeviceListKeys,
				) { _, device ->
					ConnectedDeviceCard(
						device = device,
						containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
						modifier = Modifier
							.animateItem(),
					)
				}
			} else {
				item {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.heightIn(min = 120.dp)
							.animateItem(),
						contentAlignment = Alignment.Center
					) {
						Text(
							text = stringResource(R.string.ble_server_no_clients),
							textAlign = TextAlign.Center,
						)
					}
				}
			}
		}
	}
}