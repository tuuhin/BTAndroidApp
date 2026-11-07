package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SheetTitleBar(
	title: String,
	onDismissSheet: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	titleStyle: TextStyle = MaterialTheme.typography.headlineSmallEmphasized,
	titleColor: Color = MaterialTheme.colorScheme.onSurface,
	iconColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(12.dp),
		modifier = modifier,
	) {
		Text(
			text = title,
			style = titleStyle,
			fontWeight = FontWeight.SemiBold,
			color = titleColor,
			modifier = Modifier.weight(1f),
		)
		FilledIconButton(
			onClick = onDismissSheet,
			enabled = enabled,
			colors = IconButtonDefaults.iconButtonColors(
				containerColor = iconColor,
				contentColor = contentColorFor(iconColor),
			),
			modifier = Modifier
				.minimumInteractiveComponentSize()
				.size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
			shapes = IconButtonDefaults.shapes(
				shape = IconButtonDefaults.smallRoundShape,
				pressedShape = IconButtonDefaults.smallPressedShape,
			),
		) {
			Icon(
				imageVector = AppIconVectors.close,
				contentDescription = "Cancel Action",
			)
		}
	}
}