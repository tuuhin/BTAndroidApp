package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import java.util.UUID

@Composable
fun SelectableUUIDCard(
	uuid: UUID,
	onSelect: () -> Unit,
	modifier: Modifier = Modifier,
	isSelected: Boolean = false,
	uniqueName: String? = null,
	shape: Shape = MaterialTheme.shapes.medium,
	fontFamily: FontFamily = FontFamily.Monospace,
) {

	val uuidName by remember(uuid, uniqueName) {
		derivedStateOf { uniqueName ?: "$uuid" }
	}

	Row(
		modifier = modifier
			.padding(horizontal = dimensionResource(id = R.dimen.lazy_colum_content_padding))
			.clip(shape = shape)
			.clickable(onClick = onSelect, role = Role.Checkbox),
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		RadioButton(
			selected = isSelected,
			onClick = onSelect,
			colors = RadioButtonDefaults
				.colors(selectedColor = MaterialTheme.colorScheme.secondary)
		)
		Text(
			text = uuidName,
			style = MaterialTheme.typography.labelLarge,
			fontFamily = fontFamily,
			color = MaterialTheme.colorScheme.onSurface,
		)
	}
}