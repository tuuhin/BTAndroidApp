package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BLEDeviceRequestMTUDialog(
	showDialog: Boolean,
	onRequestValue: (Int) -> Unit,
	onDismissRequest: () -> Unit,
	modifier: Modifier = Modifier
) {
	val focusRequester = remember { FocusRequester() }
	val textState = rememberTextFieldState()

	if (!showDialog) return

	AlertDialog(
		onDismissRequest = onDismissRequest,
		modifier = modifier,
		title = { Text(text = stringResource(R.string.ble_device_profile_action_request_mtu)) },
		confirmButton = {
			Button(
				onClick = {
					val paredValue = textState.text.toString().toIntOrNull()
					if (paredValue != null && paredValue in 23..517)
						onRequestValue(paredValue)
				},
			) {
				Text(text = stringResource(R.string.dialog_action_request))
			}
		},
		dismissButton = {
			TextButton(
				onClick = onDismissRequest,
				colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
			) {
				Text(text = stringResource(R.string.dialog_action_cancel))
			}
		},
		text = {
			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier.wrapContentHeight()
			) {
				OutlinedTextField(
					state = textState,
					trailingIcon = {
						IconButton(onClick = { focusRequester.freeFocus() }) {
							Icon(
								imageVector = AppIconVectors.close,
								contentDescription = "Cancel focus"
							)
						}
					},
					placeholder = { Text(text = stringResource(R.string.ble_device_profile_mtu_placeholder)) },
					shape = MaterialTheme.shapes.large,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					lineLimits = TextFieldLineLimits.SingleLine,
					contentPadding = PaddingValues(12.dp),
					modifier = Modifier.focusRequester(focusRequester)
				)
				Text(
					text = stringResource(R.string.ble_device_profile_action_request_mtu_info),
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.tertiary,
					textAlign = TextAlign.Center
				)
			}
		}
	)
}

@PreviewLightDark
@Composable
private fun BLEDeviceRequestMTUDialogPreview() = BlueToothTerminalAppTheme {
	BLEDeviceRequestMTUDialog(
		showDialog = true,
		onDismissRequest = {},
		onRequestValue = {}
	)
}