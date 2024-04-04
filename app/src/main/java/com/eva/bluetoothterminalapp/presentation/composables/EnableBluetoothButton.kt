package com.eva.bluetoothterminalapp.presentation.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.contracts.EnableBluetoothContract
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun EnableBluetoothButton(
	onResults: (Boolean) -> Unit,
	modifier: Modifier = Modifier
) {

	val launcher = rememberLauncherForActivityResult(
		contract = EnableBluetoothContract(),
		onResult = onResults
	)

	Button(
		onClick = launcher::launch,
		modifier = modifier,
		shape = MaterialTheme.shapes.medium
	) {
		Icon(
			imageVector = Icons.Default.Bluetooth,
			contentDescription = null
		)
		Spacer(modifier = Modifier.width(2.dp))
		Text(
			text = stringResource(id = R.string.enable_bluetooth_button_text),
			style = MaterialTheme.typography.bodyMedium,
		)
	}
}

@PreviewLightDark
@Composable
private fun EnableBluetoothButtonPreview() = BlueToothTerminalAppTheme {
	EnableBluetoothButton(onResults = {})
}