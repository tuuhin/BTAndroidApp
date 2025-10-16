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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BLEConnectedClients(
	connectedClients: ImmutableList<BluetoothDeviceModel>,
	modifier: Modifier = Modifier
) {
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
					text = "Connected Clients",
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = "List of connected clients",
					style = MaterialTheme.typography.labelLarge,
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
		LazyHorizontalGrid(
			rows = GridCells.Fixed(2),
			modifier = Modifier.fillMaxSize(),
			contentPadding = PaddingValues(vertical = 2.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			if (connectedClients.isNotEmpty()) {
				itemsIndexed(
					items = connectedClients,
					key = { _, item -> item.address },
				) { _, device ->
					ConnectedDeviceCard(
						device = device,
						modifier = Modifier
							.animateItem(),
					)
				}
			} else {
				item(span = { GridItemSpan(currentLineSpan = 2) }) {
					Text(
						text = "No Clients connected",
						textAlign = TextAlign.Center,
						modifier = Modifier
							.fillMaxWidth()
							.padding(vertical = 12.dp)
							.animateItem()
					)
				}
			}
		}
	}
}