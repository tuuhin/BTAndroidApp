package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.presentation.util.imageVector
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BluetoothDeviceCard(
	device: BluetoothDeviceModel,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.large,
	colors: CardColors = CardDefaults.cardColors(),
	elevation: CardElevation = CardDefaults.cardElevation(),
) {
	Card(
		modifier = modifier,
		shape = shape,
		colors = colors,
		elevation = elevation,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier.padding(8.dp),
		) {
			Box(
				modifier = Modifier
					.defaultMinSize(minWidth = 32.dp, minHeight = 32.dp)
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.onPrimaryContainer),
				contentAlignment = Alignment.Center
			) {
				Icon(
					imageVector = device.imageVector,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.primaryContainer,
					modifier = Modifier.padding(8.dp)
				)
			}
			Column {
				Text(
					text = device.name,
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = device.address,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}


@PreviewLightDark
@Composable
private fun BluetoothDeviceCardPreview() = BlueToothTerminalAppTheme {
	Surface {
		BluetoothDeviceCard(
			device = PreviewFakes.FAKE_DEVICE_MODEL,
			onClick = {}
		)
	}
}