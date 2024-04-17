package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
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
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun CloseConnectionDialog(
	showDialog: Boolean,
	modifier: Modifier = Modifier,
	onDismiss: () -> Unit = {},
	onConfirm: () -> Unit = {},
	shape: Shape = AlertDialogDefaults.shape,
	tonalElevation: Dp = AlertDialogDefaults.TonalElevation
) {
	if (!showDialog) return

	AlertDialog(
		onDismissRequest = onDismiss,
		confirmButton = {
			TextButton(onClick = onConfirm) {
				Text(
					text = stringResource(id = R.string.dialog_action_close),
					style = MaterialTheme.typography.titleMedium
				)
			}
		},
		dismissButton = {
			TextButton(
				onClick = onDismiss,
				colors = ButtonDefaults
					.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
			) {
				Text(
					text = stringResource(id = R.string.dialog_action_cancel),
					style = MaterialTheme.typography.titleMedium
				)
			}
		},
		title = { Text(text = stringResource(id = R.string.close_connection_dialog_title)) },
		text = { Text(text = stringResource(id = R.string.close_connection_dialog_text)) },
		icon = {
			Icon(
				imageVector = Icons.Outlined.Warning,
				contentDescription = stringResource(id = R.string.close_connection_dialog_title)
			)
		},
		modifier = modifier,
		shape = shape,
		tonalElevation = tonalElevation
	)
}

@PreviewLightDark
@Composable
private fun CloseConnectionDialogPreview() = BlueToothTerminalAppTheme {
	CloseConnectionDialog(showDialog = true)
}