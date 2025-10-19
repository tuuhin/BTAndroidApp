package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.presentation.composables.BTDeviceIconLarge
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEDeviceProfile(
	device: BluetoothDeviceModel,
	modifier: Modifier = Modifier,
	connectionState: BLEConnectionState = BLEConnectionState.CONNECTED,
	rssi: Int = 0,
	shape: Shape = MaterialTheme.shapes.extraLarge,
	containerColor: Color = MaterialTheme.colorScheme.secondaryContainer
) {
	Surface(
		color = containerColor,
		contentColor = contentColorFor(containerColor),
		shape = shape,
		modifier = modifier,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 20.dp, vertical = 16.dp),
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(2.dp),
				horizontalAlignment = Alignment.Start,
				modifier = Modifier.weight(.7f)
			) {
				Text(
					text = device.name,
					style = MaterialTheme.typography.titleLarge,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					fontWeight = FontWeight.SemiBold
				)
				Text(
					text = device.address,
					style = MaterialTheme.typography.labelLarge,
					fontFamily = FontFamily.Monospace,
				)
				Text(
					text = buildString {
						append("RSSI : ")
						append(rssi)
						append(BluetoothDeviceModel.RSSI_UNIT)
					},
					style = MaterialTheme.typography.labelLarge,
					modifier = Modifier.padding(top = 6.dp)
				)
				BLEConnectionStatusChip(
					state = connectionState,
					shape = MaterialTheme.shapes.medium,
				)
			}
			BTDeviceIconLarge(
				device = device,
				containerColor = MaterialTheme.colorScheme.tertiary,
				contentColor = MaterialTheme.colorScheme.onTertiary
			)
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