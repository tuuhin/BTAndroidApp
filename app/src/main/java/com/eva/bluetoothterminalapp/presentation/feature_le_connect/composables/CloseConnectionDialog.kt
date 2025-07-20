package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.CloseConnectionEvents
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun CloseConnectionDialog(
	showDialog: Boolean,
	onDismiss: () -> Unit,
	onConfirm: () -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = AlertDialogDefaults.shape,
	tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
	properties: DialogProperties = DialogProperties()
) {
	if (!showDialog) return

	AlertDialog(
		confirmButton = {
			TextButton(
				onClick = onConfirm,
				colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
			) {
				Text(text = stringResource(id = R.string.dialog_action_close))
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
		title = { Text(text = stringResource(id = R.string.close_ble_connection_dialog)) },
		text = { Text(text = stringResource(id = R.string.close_ble_connection_desc)) },
		icon = {
			Icon(
				imageVector = Icons.Default.Close,
				contentDescription = stringResource(id = R.string.close_ble_connection_dialog)
			)
		},
		shape = shape,
		properties = properties,
		onDismissRequest = onDismiss,
		tonalElevation = tonalElevation,
		modifier = modifier,
	)
}

@Composable
fun CloseConnectionDialog(
	showDialog: Boolean,
	onEvent: (CloseConnectionEvents) -> Unit,
	modifier: Modifier = Modifier
) {
	CloseConnectionDialog(
		showDialog = showDialog,
		onDismiss = { onEvent(CloseConnectionEvents.CancelCloseConnection) },
		onConfirm = { onEvent(CloseConnectionEvents.ConfirmCloseDialog) },
		modifier = modifier
	)
}

@PreviewLightDark
@Composable
private fun CloseConnectionDialogPreview() = BlueToothTerminalAppTheme {
	CloseConnectionDialog(showDialog = true, onEvent = {})
}