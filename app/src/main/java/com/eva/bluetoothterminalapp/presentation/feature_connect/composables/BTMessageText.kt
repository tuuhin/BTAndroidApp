package com.eva.bluetoothterminalapp.presentation.feature_connect.composables

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessageType
import com.eva.bluetoothterminalapp.presentation.feature_connect.util.toReadableTimeText
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTMessageText(
	message: BluetoothMessage,
	modifier: Modifier = Modifier,
	showTime: Boolean = true,
) {
	Row(
		modifier = modifier.padding(4.dp),
		horizontalArrangement = Arrangement.spacedBy(12.dp),
		verticalAlignment = Alignment.Top
	) {
		if (showTime)
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
				BluetoothMessageType.MESSAGE_FROM_OTHER -> MaterialTheme.colorScheme.primary
				BluetoothMessageType.MESSAGE_FROM_SELF -> MaterialTheme.colorScheme.secondary
			},
		)
	}
}

private class BTMessageTextPreviewParams
	: CollectionPreviewParameterProvider<BluetoothMessageType>(BluetoothMessageType.entries)


@PreviewLightDark
@Composable
private fun BTMessageTextPreview(
	@PreviewParameter(BTMessageTextPreviewParams::class)
	type: BluetoothMessageType
) = BlueToothTerminalAppTheme {
	Surface {
		BTMessageText(
			message = BluetoothMessage(
				message = "Some message",
				type = type,
			),
			modifier = Modifier.padding(12.dp)
		)
	}
}

@PreviewLightDark
@Composable
private fun BTMessageTextPreview2(
	@PreviewParameter(BTMessageTextPreviewParams::class)
	type: BluetoothMessageType
) = BlueToothTerminalAppTheme {
	Surface {
		BTMessageText(
			message = BluetoothMessage(
				message = "This is a very very long message that should be taken into account",
				type = type,
			),
			modifier = Modifier.padding(12.dp)
		)
	}
}