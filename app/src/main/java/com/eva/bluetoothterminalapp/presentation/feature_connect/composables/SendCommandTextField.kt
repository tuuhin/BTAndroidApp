package com.eva.bluetoothterminalapp.presentation.feature_connect.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
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
	maxLines: Int = 2,
	cursorColor: Brush = SolidColor(MaterialTheme.colorScheme.primary),
	textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
	color: Color = MaterialTheme.colorScheme.onSurface
) {
	val interaction = remember { MutableInteractionSource() }
	val isFocused by interaction.collectIsFocusedAsState()

	val surfaceColor by animateColorAsState(
		targetValue = if (isFocused) MaterialTheme.colorScheme.surfaceContainerHighest
		else MaterialTheme.colorScheme.surfaceContainerHigh,
		label = "Container color"
	)


	BasicTextField(
		value = value,
		onValueChange = onChange,
		decorationBox = { content ->
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Surface(
					color = surfaceColor,
					shape = MaterialTheme.shapes.extraLarge,
					contentColor = color,
					modifier = Modifier.weight(1f),
				) {
					Box(
						modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp),
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
				FilledIconButton(
					onClick = onImeAction,
					enabled = isEnable,
					modifier = Modifier
						.minimumInteractiveComponentSize()
						.size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
					shapes = IconButtonDefaults.shapes(
						shape = IconButtonDefaults.mediumRoundShape,
						pressedShape = IconButtonDefaults.mediumRoundShape,
					),
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_send),
						contentDescription = stringResource(id = R.string.dialog_action_send),
					)
				}
			}
		},
		enabled = isEnable,
		textStyle = textStyle.copy(color = color),
		cursorBrush = cursorColor,
		keyboardActions = KeyboardActions(onSend = { onImeAction() }),
		keyboardOptions = KeyboardOptions(
			capitalization = KeyboardCapitalization.None,
			autoCorrectEnabled = false,
			keyboardType = KeyboardType.Text,
			imeAction = ImeAction.Send
		),
		maxLines = maxLines,
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