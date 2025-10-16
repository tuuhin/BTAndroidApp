package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun AdvertisePermissionDialog(
	showDialog: Boolean,
	onConfirm: () -> Unit,
	modifier: Modifier = Modifier,
	onDismiss: () -> Unit = {},
	shape: Shape = AlertDialogDefaults.shape,
	tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
	properties: DialogProperties = DialogProperties()
) {
	if (!showDialog) return

	AlertDialog(
		confirmButton = {
			FilledTonalButton(
				onClick = onConfirm,
				colors = ButtonDefaults.filledTonalButtonColors(contentColor = MaterialTheme.colorScheme.primary)
			) {
				Text(text = stringResource(id = R.string.dialog_action_allow))
			}
		},
		dismissButton = {
			TextButton(
				onClick = onDismiss,
				colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
			) {
				Text(text = stringResource(id = R.string.dialog_action_cancel))
			}
		},
		title = { Text(text = stringResource(id = R.string.ble_advertise_permission_missing)) },
		text = { Text(text = stringResource(id = R.string.ble_advertise_permission_missing_text)) },
		shape = shape,
		properties = properties,
		onDismissRequest = onDismiss,
		tonalElevation = tonalElevation,
		modifier = modifier,
	)
}

@Preview
@Composable
private fun AdvertisePermissionDialogPreview() = BlueToothTerminalAppTheme {
	AdvertisePermissionDialog(
		showDialog = true,
		onConfirm = {},
		onDismiss = {}
	)
}