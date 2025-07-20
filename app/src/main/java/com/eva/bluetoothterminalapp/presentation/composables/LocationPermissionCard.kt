package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun LocationPermissionCard(
	modifier: Modifier = Modifier,
	onLocationAccess: (Boolean) -> Unit = {},
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
	shape: Shape = MaterialTheme.shapes.small,
) {
	Card(
		colors = CardDefaults.cardColors(
			containerColor = containerColor,
			contentColor = contentColorFor(containerColor)
		),
		shape = shape,
		modifier = modifier.padding(vertical = 8.dp),
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(4.dp),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(12.dp)
		) {
			Column(
				modifier = Modifier.weight(.7f),
				verticalArrangement = Arrangement.spacedBy(4.dp)
			) {
				Text(
					text = stringResource(id = R.string.location_perms_title),
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.secondary
				)
				Text(
					text = stringResource(id = R.string.location_perms_desc),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurface
				)
			}
			EnableLocationButton(
				onFineLocationAccess = onLocationAccess
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun LocationPermissionCardPreview() = BlueToothTerminalAppTheme {
	LocationPermissionCard()
}