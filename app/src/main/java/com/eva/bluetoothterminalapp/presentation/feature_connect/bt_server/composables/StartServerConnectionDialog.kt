package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerEvents
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun StartServerConnectionDialog(
	showDialog: Boolean,
	onComfirm: () -> Unit,
	onCancel: () -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = AlertDialogDefaults.shape,
	tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
) {
	if (!showDialog) return

	AlertDialog(
		onDismissRequest = {},
		confirmButton = {
			TextButton(onClick = onComfirm) {
				Text(text = stringResource(id = R.string.dialog_action_start))
			}
		},
		dismissButton = {
			TextButton(onClick = onCancel) {
				Text(text = stringResource(id = R.string.dialog_action_cancel))
			}
		},
		title = { Text(text = stringResource(id = R.string.bt_server_start_connection_dialog_title)) },
		text = {
			Text(
				text = stringResource(id = R.string.bt_server_start_connection_dialog_text),
				textAlign = TextAlign.Center
			)
		},
		icon = {
			Icon(
				imageVector = Icons.Default.Bluetooth,
				contentDescription = stringResource(id = R.string.bt_server_start_connection_dialog_title)
			)
		},
		tonalElevation = tonalElevation,
		shape = shape,
		modifier = modifier,
		properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
	)
}

@Composable
fun StartServerConnectionDailog(
	showDialog: Boolean,
	onEvent: (BTServerEvents) -> Unit,
	modifier: Modifier = Modifier
) {
	StartServerConnectionDialog(
		showDialog = showDialog,
		onComfirm = { onEvent(BTServerEvents.OnStartServer) },
		onCancel = { onEvent(BTServerEvents.OnStopServerAndNavigateBack) },
		modifier = modifier
	)
}

@PreviewLightDark
@Composable
private fun StartServerConnectionDialogPreview() = BlueToothTerminalAppTheme {
	StartServerConnectionDialog(
		showDialog = true,
		onComfirm = { },
		onCancel = { },
	)
}