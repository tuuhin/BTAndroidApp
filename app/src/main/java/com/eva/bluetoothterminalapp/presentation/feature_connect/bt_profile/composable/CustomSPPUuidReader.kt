package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R

@Composable
fun CustomSPPUuidReader(
	onAddCustomUuid: (String) -> Unit,
	modifier: Modifier = Modifier
) {
	val textFieldState = rememberTextFieldState()

	val isValid by remember(textFieldState) {
		derivedStateOf { textFieldState.text.isNotBlank() }
	}

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(6.dp)
	) {
		OutlinedTextField(
			state = textFieldState,
			label = {
				Text(stringResource(R.string.bl_connect_profile_custom_uuid))
			},

			shape = MaterialTheme.shapes.large,
			lineLimits = TextFieldLineLimits.SingleLine,
			keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
			onKeyboardAction = {
				if (textFieldState.text.isNotBlank())
					onAddCustomUuid(textFieldState.text.toString())
			},
			modifier = Modifier.fillMaxWidth()
		)
		Button(
			onClick = { onAddCustomUuid(textFieldState.text.toString()) },
			contentPadding = ButtonDefaults.contentPaddingFor(
				ButtonDefaults.ExtraSmallContainerHeight,
				hasStartIcon = true
			),
			enabled = isValid,
			shapes = ButtonDefaults.shapes(
				pressedShape = ButtonDefaults.mediumPressedShape,
				shape = ButtonDefaults.shape
			),
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				text = stringResource(R.string.bl_connect_profile_custom_uuid),
				style = ButtonDefaults.textStyleFor(ButtonDefaults.ExtraSmallContainerHeight)
			)
		}
	}
}