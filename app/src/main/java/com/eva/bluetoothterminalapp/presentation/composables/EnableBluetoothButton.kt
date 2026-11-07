package com.eva.bluetoothterminalapp.presentation.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
		contentPadding = ButtonDefaults.contentPaddingFor(
			ButtonDefaults.MediumContainerHeight,
			hasStartIcon = true
		),
		shapes = ButtonDefaults.shapes(
			pressedShape = ButtonDefaults.mediumPressedShape,
			shape = ButtonDefaults.shape
		),
	) {
		Icon(
			imageVector = AppIconVectors.bluetooth,
			contentDescription = null,
			modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MediumContainerHeight)),
		)
		Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MediumContainerHeight)))
		Text(
			text = stringResource(id = R.string.enable_bluetooth_button_text),
			style = ButtonDefaults.textStyleFor(ButtonDefaults.MediumContainerHeight),
		)
	}
}

@PreviewLightDark
@Composable
private fun EnableBluetoothButtonPreview() = BlueToothTerminalAppTheme {
	EnableBluetoothButton(onResults = {})
}