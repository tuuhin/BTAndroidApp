package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.composable

import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import java.util.UUID

@Composable
fun SelectableUUIDCard(
	uuid: UUID,
	onSelect: () -> Unit,
	modifier: Modifier = Modifier,
	isSelected: Boolean = false,
	uniqueName: String? = null,
	fontFamily: FontFamily = FontFamily.Monospace,
	shape: ListItemShapes = ListItemDefaults.segmentedShapes(0, 1),
	colors: ListItemColors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
) {

	val uuidName by remember(uuid, uniqueName) {
		derivedStateOf { uniqueName ?: "$uuid" }
	}

	SegmentedListItem(
		shapes = shape,
		colors = colors,
		onClick = onSelect,
		leadingContent = {
			RadioButton(
				selected = isSelected,
				onClick = null,
				colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
			)
		},
		content = { Text(text = uuidName, fontFamily = fontFamily) },
		modifier = modifier,
	)
}