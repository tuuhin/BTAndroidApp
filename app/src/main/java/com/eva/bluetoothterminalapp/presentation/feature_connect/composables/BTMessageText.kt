package com.eva.bluetoothterminalapp.presentation.feature_client.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.presentation.feature_connect.util.toReadableTimeText
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTMessageText(
	message: BluetoothMessage,
	modifier: Modifier = Modifier
) {

	Row(
		modifier = modifier.padding(4.dp),
		horizontalArrangement = Arrangement.spacedBy(12.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = message.logTime.toReadableTimeText(),
			style = MaterialTheme.typography.titleSmall,
			color = MaterialTheme.colorScheme.onSurface,
			fontWeight = FontWeight.Medium
		)
		Text(
			text = message.message,
			style = MaterialTheme.typography.bodyMedium,
			color = when (message.type) {
				BluetoothMessageType.MESSAGE_FROM_SERVER -> MaterialTheme.colorScheme.primary
				BluetoothMessageType.MESSAGE_FROM_CLIENT -> MaterialTheme.colorScheme.secondary
				BluetoothMessageType.MESSAGE_BY_DEVICE -> MaterialTheme.colorScheme.tertiary
			},
			fontWeight = FontWeight.SemiBold
		)
	}
}

@PreviewLightDark
@Composable
private fun BTMessageTextPreview() = BlueToothTerminalAppTheme {
	Surface {
		BTMessageText(
			message = BluetoothMessage(
				message = "HELLO",
				type = BluetoothMessageType.MESSAGE_BY_DEVICE,
			),
			modifier = Modifier.padding(12.dp)
		)
	}
}