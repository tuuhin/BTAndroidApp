package com.eva.bluetoothterminalapp.presentation.feature_connect.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
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


	val surfaceColor by animateColorAsState(
		targetValue = if (isFocused) MaterialTheme.colorScheme.surfaceContainerHighest
		else MaterialTheme.colorScheme.surfaceContainerHigh,
		label = "Container color"
	)

	val iconButtonColor by animateColorAsState(
		targetValue = if (isEnable) MaterialTheme.colorScheme.primaryContainer
		else MaterialTheme.colorScheme.primary.copy(alpha = .4f),
		label = "Icon button colors"
	)

	BasicTextField(
		value = value,
		onValueChange = onChange,
		decorationBox = { content ->
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				Surface(
					color = surfaceColor,
					shape = MaterialTheme.shapes.extraLarge,
					contentColor = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier.weight(1f),
				) {
					Box(
						modifier = Modifier
							.padding(horizontal = 16.dp, vertical = 12.dp),
					) {
						when {
							value.isBlank() ->
								Text(
									text = stringResource(id = R.string.text_field_placeholder),
									style = MaterialTheme.typography.labelLarge,
									color = MaterialTheme.colorScheme.onSurfaceVariant
										.copy(alpha = .5f)
								)

							else -> content()
						}
					}
				}
				IconButton(
					onClick = onImeAction,
					enabled = isEnable,
					colors = IconButtonDefaults
						.filledIconButtonColors(containerColor = iconButtonColor)
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_send),
						contentDescription = "Send",
					)
				}
			}
		},
		enabled = isEnable,
		textStyle = MaterialTheme.typography.bodyMedium
			.copy(color = MaterialTheme.colorScheme.onSurface),
		cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
		keyboardActions = KeyboardActions(
			onSend = { onImeAction() },
		),
		keyboardOptions = KeyboardOptions(
			autoCorrect = false,
			imeAction = ImeAction.Send
		),
		maxLines = 4,
		interactionSource = interaction,
		modifier = modifier.focusable(enabled = true)
	)
}

private class TextValuePreviewParams :
	CollectionPreviewParameterProvider<String>(listOf("", "Some value"))

@PreviewLightDark
@Composable
private fun SendCommandTextFieldPreview(
	@PreviewParameter(TextValuePreviewParams::class)
	value: String
) = BlueToothTerminalAppTheme {
	Surface {
		SendCommandTextField(
			value = value,
			isEnable = false,
			onChange = {},
			modifier = Modifier.padding(10.dp)
		)
	}
}