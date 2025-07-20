package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
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
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerEvents
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun CloseServerConnectionDialog(
	showDialog: Boolean,
	modifier: Modifier = Modifier,
	onDismiss: () -> Unit = {},
	onConfirm: () -> Unit = {},
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
		title = { Text(text = stringResource(id = R.string.bt_server_close_connection_dialog_title)) },
		text = { Text(text = stringResource(id = R.string.bt_server_close_connection_dialog_text)) },
		shape = shape,
		properties = properties,
		onDismissRequest = onDismiss,
		tonalElevation = tonalElevation,
		modifier = modifier,
	)
}

@Composable
fun CloseServerConnectionDialog(
	showDialog: Boolean,
	onEvent: (BTServerEvents) -> Unit,
	modifier: Modifier = Modifier
) {
	CloseServerConnectionDialog(
		showDialog = showDialog,
		onConfirm = { onEvent(BTServerEvents.OnStopServerAndNavigateBack) },
		onDismiss = { onEvent(BTServerEvents.OnCloseDisconnectDialog) },
		modifier = modifier
	)
}

@PreviewLightDark
@Composable
private fun CloseConnectionDialogPreview() = BlueToothTerminalAppTheme {
	CloseServerConnectionDialog(showDialog = true, onEvent = {})
}