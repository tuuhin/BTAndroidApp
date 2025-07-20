package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.composables.BTDeviceIconLarge
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEDeviceProfile(
	device: BluetoothDeviceModel?,
	modifier: Modifier = Modifier,
	connectionState: BLEConnectionState = BLEConnectionState.CONNECTED,
	rssi: Int = 0,
	shape: Shape = MaterialTheme.shapes.large,
	containerColor: Color = MaterialTheme.colorScheme.secondaryContainer
) {

	AnimatedVisibility(
		visible = device != null,
		enter = slideInVertically { height -> height },
		exit = slideOutVertically { height -> -height }
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
					Text(
						text = "RSSI : $rssi ${BluetoothDeviceModel.RSSI_UNIT}",
						style = MaterialTheme.typography.labelLarge,
						modifier = Modifier.width(IntrinsicSize.Max)
					)
					BLEConnectionStatusChip(
						state = connectionState,
						shape = MaterialTheme.shapes.small
					)
				}
				BTDeviceIconLarge(device = device)
			}
		}
	}
}


@PreviewLightDark
@Composable
private fun BLEDeviceProfilePreview() = BlueToothTerminalAppTheme {
	Surface {
		BLEDeviceProfile(
			device = PreviewFakes.FAKE_DEVICE_MODEL,
			rssi = -50,
			modifier = Modifier
				.padding(12.dp)
				.fillMaxWidth()
		)
	}
}