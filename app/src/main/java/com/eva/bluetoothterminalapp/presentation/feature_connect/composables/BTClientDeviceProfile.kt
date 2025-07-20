package com.eva.bluetoothterminalapp.presentation.feature_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.composables.BTDeviceIconLarge
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables.ClientConnectionStateChip
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTClientDeviceProfile(
	device: BluetoothDeviceModel?,
	modifier: Modifier = Modifier,
	connectionState: ClientConnectionState = ClientConnectionState.CONNECTION_CONNECTED,
	shape: Shape = MaterialTheme.shapes.large,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest
) {
	AnimatedVisibility(
		visible = device != null,
		enter = expandIn(
			animationSpec = spring(
				dampingRatio = Spring.DampingRatioMediumBouncy,
				stiffness = Spring.StiffnessMedium
			),
			expandFrom = Alignment.TopCenter,
		) + fadeIn(
			animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
		),
		exit = shrinkOut(
			animationSpec = spring(
				dampingRatio = Spring.DampingRatioMediumBouncy,
				stiffness = Spring.StiffnessMedium
			),
			shrinkTowards = Alignment.TopCenter,
		) + fadeOut(
			animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
		)
	) {
		// the content will not be visible if its null
		if (device == null) return@AnimatedVisibility

		Card(
			colors = CardDefaults.cardColors(
				containerColor = containerColor,
				contentColor = contentColorFor(containerColor)
			),
			elevation = CardDefaults.elevatedCardElevation(),
			shape = shape,
			modifier = modifier,
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
			) {
				Column(
					verticalArrangement = Arrangement.spacedBy(2.dp),
					horizontalAlignment = Alignment.Start,
					modifier = Modifier.weight(.7f)
				) {
					Text(
						text = device.name,
						style = MaterialTheme.typography.titleLarge,
						fontWeight = FontWeight.SemiBold
					)
					Text(
						text = device.address,
						style = MaterialTheme.typography.labelLarge,
						fontFamily = FontFamily.Monospace,
					)
					Spacer(modifier = Modifier.height(4.dp))
					ClientConnectionStateChip(connectionState = connectionState)
				}
				BTDeviceIconLarge(device = device)
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun BTDeviceClientProfilePreview() = BlueToothTerminalAppTheme {
	Surface {
		BTClientDeviceProfile(
			device = PreviewFakes.FAKE_DEVICE_MODEL,
			connectionState = ClientConnectionState.CONNECTION_CONNECTED,
			modifier = Modifier
				.padding(12.dp)
				.fillMaxWidth()
		)
	}
}

