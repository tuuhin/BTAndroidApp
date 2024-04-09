package com.eva.bluetoothterminalapp.presentation.feature_client.composables

import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun SendCommandTextField(
	value: String,
	onChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	isEnable: Boolean = true,
	onImeAction: () -> Unit = {},
) {
	val interaction = remember { MutableInteractionSource() }
	val isFocused by interaction.collectIsFocusedAsState()

	BasicTextField(
		value = value,
		onValueChange = onChange,
		decorationBox = { content ->

			val surfaceColor = if (isFocused) MaterialTheme.colorScheme.surfaceContainerHighest
			else MaterialTheme.colorScheme.surfaceContainerHigh

			Surface(
				color = surfaceColor,
				shape = MaterialTheme.shapes.large,
				contentColor = MaterialTheme.colorScheme.onSurface
			) {
				Row(
					modifier = Modifier.padding(6.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Box(
						modifier = Modifier
							.weight(1f)
							.padding(8.dp)
							.horizontalScroll(state = rememberScrollState()),
					) {
						when {
							value.isBlank() ->
								Text(
									text = "Placeholder",
									style = MaterialTheme.typography.bodyMedium,
									color = MaterialTheme.colorScheme.onSurfaceVariant
										.copy(alpha = .5f)
								)

							else -> content()
						}
					}
					IconButton(
						onClick = onImeAction,
						colors = IconButtonDefaults.filledIconButtonColors(),
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_send),
							contentDescription = null,
						)
					}
				}
			}
		},
		enabled = isEnable,
		textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
		cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
		keyboardActions = KeyboardActions(
			onSend = { onImeAction() },
		),
		keyboardOptions = KeyboardOptions(
			autoCorrect = false, imeAction = ImeAction.Send
		),
		maxLines = 4,
		modifier = modifier.focusable(enabled = true, interactionSource = interaction)
	)

}

@PreviewLightDark
@Composable
private fun SendCommandTextFieldPreview() = BlueToothTerminalAppTheme {
	Surface {
		SendCommandTextField(
			value = "",
			onChange = {},
			modifier = Modifier.padding(10.dp)
		)
	}
}