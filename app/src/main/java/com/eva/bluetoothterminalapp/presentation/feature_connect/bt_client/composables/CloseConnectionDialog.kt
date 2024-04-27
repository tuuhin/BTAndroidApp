package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WavingHand
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.EndConnectionEvents
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun CloseConnectionDialog(
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
			TextButton(onClick = onConfirm) {
				Text(text = stringResource(id = R.string.dialog_action_close))
			}
		},
		dismissButton = {
			TextButton(onClick = onDismiss) {
				Text(text = stringResource(id = R.string.dialog_action_cancel))
			}
		},
		title = { Text(text = stringResource(id = R.string.close_connection_dialog_title)) },
		text = {
			Text(
				text = stringResource(id = R.string.close_connection_dialog_text),
				textAlign = TextAlign.Center
			)
		},
		icon = {
			Icon(
				imageVector = Icons.Outlined.WavingHand,
				contentDescription = stringResource(id = R.string.close_connection_dialog_title),
				modifier = Modifier.defaultMinSize(32.dp, 32.dp)
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
	onEvent: (EndConnectionEvents) -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = AlertDialogDefaults.shape,
	tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
	properties: DialogProperties = DialogProperties()
) {
	CloseConnectionDialog(
		showDialog = showDialog,
		onConfirm = { onEvent(EndConnectionEvents.OnDisconnectAndNavigateBack) },
		onDismiss = { onEvent(EndConnectionEvents.OnCancelAndCloseDialog) },
		modifier = modifier,
		tonalElevation = tonalElevation,
		shape = shape,
		properties = properties
	)
}

@PreviewLightDark
@Composable
private fun CloseConnectionDialogPreview() = BlueToothTerminalAppTheme {
	CloseConnectionDialog(showDialog = true)
}