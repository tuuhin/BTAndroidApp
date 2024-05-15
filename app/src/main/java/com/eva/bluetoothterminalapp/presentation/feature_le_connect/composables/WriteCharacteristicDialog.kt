package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.CharacteristicWriteDialogState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.WriteCharacteristicEvent
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteCharactertisticDialog(
	showDialog: Boolean,
	textFieldValue: String,
	errorString: String?,
	onValueChange: (String) -> Unit,
	onSend: () -> Unit,
	onCancel: () -> Unit,
	modifier: Modifier = Modifier,
	properties: DialogProperties = DialogProperties(),
	shape: Shape = AlertDialogDefaults.shape,
	color: Color = AlertDialogDefaults.containerColor,
	titleContentColor: Color = AlertDialogDefaults.titleContentColor,
	textContentCOlor: Color = AlertDialogDefaults.textContentColor,
	tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
) {

	if (!showDialog) return

	val isSendButtonEnable by remember(textFieldValue) {
		derivedStateOf(textFieldValue::isNotBlank)
	}

	BasicAlertDialog(
		onDismissRequest = onCancel,
		modifier = modifier,
		properties = properties
	) {
		Surface(
			shape = shape,
			color = color,
			tonalElevation = tonalElevation
		) {
			Column(
				modifier = Modifier.padding(24.dp)
			) {
				Text(
					text = stringResource(id = R.string.write_characteristic_dialog_title),
					color = titleContentColor,
					style = MaterialTheme.typography.headlineSmall,
					modifier = Modifier.padding(bottom = 4.dp)
				)
				Text(
					text = stringResource(id = R.string.write_characteristic_dialog_desc),
					color = textContentCOlor,
					style = MaterialTheme.typography.bodyMedium
				)
				Column {
					OutlinedTextField(
						value = textFieldValue,
						onValueChange = onValueChange,
						textStyle = MaterialTheme.typography.bodyMedium,
						shape = MaterialTheme.shapes.medium,
						isError = errorString != null,
						placeholder = {
							Text(text = stringResource(R.string.text_field_placeholder))
						},
						modifier = Modifier.padding(vertical = 12.dp),
					)
					errorString?.let { err ->
						Text(
							text = err,
							style = MaterialTheme.typography.labelSmall,
							color = MaterialTheme.colorScheme.error
						)
					}
				}
				Spacer(modifier = Modifier.height(8.dp))
				ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
					Row(
						horizontalArrangement = Arrangement.spacedBy(12.dp),
						modifier = Modifier.align(Alignment.End)
					) {
						TextButton(
							onClick = onCancel,
							colors = ButtonDefaults
								.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
						) {
							Text(text = stringResource(id = R.string.dialog_action_cancel))
						}
						TextButton(
							onClick = onSend,
							enabled = isSendButtonEnable,
							colors = ButtonDefaults
								.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
						) {
							Text(text = stringResource(id = R.string.dialog_action_send))
						}
					}
				}
			}
		}
	}
}

@Composable
fun WriteCharactertisticDialog(
	state: CharacteristicWriteDialogState,
	onEvent: (WriteCharacteristicEvent) -> Unit,
	modifier: Modifier = Modifier, properties: DialogProperties = DialogProperties(),
	shape: Shape = AlertDialogDefaults.shape,
	color: Color = AlertDialogDefaults.containerColor,
	titleContentColor: Color = AlertDialogDefaults.titleContentColor,
	textContentColor: Color = AlertDialogDefaults.textContentColor,
	tonalElevation: Dp = AlertDialogDefaults.TonalElevation
) {
	WriteCharactertisticDialog(
		showDialog = state.showWriteDialog,
		errorString = state.error,
		textFieldValue = state.writeTextFieldValue,
		onValueChange = { onEvent(WriteCharacteristicEvent.OnTextFieldValueChange(it)) },
		onSend = { onEvent(WriteCharacteristicEvent.WriteCharacteristicValue) },
		onCancel = { onEvent(WriteCharacteristicEvent.CloseDialog) },
		modifier = modifier,
		properties = properties,
		shape = shape,
		color = color,
		titleContentColor = titleContentColor,
		textContentCOlor = textContentColor,
		tonalElevation = tonalElevation
	)
}


@PreviewLightDark
@Composable
private fun WriteCharacteristicDialogPreview() = BlueToothTerminalAppTheme {
	WriteCharactertisticDialog(
		showDialog = true,
		errorString = "Empty string are not allowed",
		textFieldValue = "",
		onValueChange = {},
		onCancel = {},
		onSend = {},
	)
}