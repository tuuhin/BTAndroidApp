package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util.ClientConnectType
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectTypeDialogPicker(
	showDialog: Boolean,
	connectType: ClientConnectType,
	onConnectOptionChanged: (ClientConnectType) -> Unit,
	onConnect: () -> Unit,
	modifier: Modifier = Modifier,
	onCancel: () -> Unit = {},
	containerColor: Color = AlertDialogDefaults.containerColor,
	shape: Shape = AlertDialogDefaults.shape,
	tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
) {
	if (!showDialog) return

	BasicAlertDialog(
		onDismissRequest = {},
		properties = DialogProperties(
			dismissOnBackPress = false,
			dismissOnClickOutside = false,
			decorFitsSystemWindows = false
		),
		modifier = modifier,
	) {
		Surface(
			color = containerColor,
			shape = shape,
			tonalElevation = tonalElevation,
		) {
			Column(
				modifier = Modifier.padding(16.dp),
			) {
				Text(
					text = stringResource(id = R.string.connect_mode_title),
					style = MaterialTheme.typography.headlineSmall,
					color = AlertDialogDefaults.titleContentColor,
					modifier = Modifier.padding(14.dp)
				)
				Text(
					text = stringResource(id = R.string.connect_mode_desc),
					style = MaterialTheme.typography.bodyMedium,
					color = AlertDialogDefaults.textContentColor,
					modifier = Modifier.padding(horizontal = 14.dp)
				)
				Column(
					modifier = Modifier.padding(vertical = 8.dp)
				) {
					ClientConnectType.entries.forEach { option ->
						Row(
							horizontalArrangement = Arrangement.spacedBy(12.dp),
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.fillMaxWidth()
								.clip(MaterialTheme.shapes.medium)
								.clickable { onConnectOptionChanged(option) }
						) {
							RadioButton(
								selected = option == connectType,
								onClick = { onConnectOptionChanged(option) },
								colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
							)
							Text(
								text = stringResource(id = option.resource),
								style = MaterialTheme.typography.bodyMedium,
							)
						}
					}
				}
				Row(
					modifier = Modifier.align(Alignment.End)
				) {
					TextButton(
						onClick = onCancel,
						colors = ButtonDefaults
							.textButtonColors(contentColor = contentColorFor(containerColor))
					) {
						Text(text = stringResource(id = R.string.dialog_action_cancel))
					}

					TextButton(
						onClick = onConnect,
						colors = ButtonDefaults
							.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
					) {
						Text(text = stringResource(id = R.string.dialog_action_connect))
					}
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun ConnectTypeDialogPickerPreview() = BlueToothTerminalAppTheme {
	ConnectTypeDialogPicker(
		showDialog = true,
		connectType = ClientConnectType.CONNECT_TO_DEVICE,
		onConnectOptionChanged = {},
		onCancel = {},
		onConnect = {}
	)
}