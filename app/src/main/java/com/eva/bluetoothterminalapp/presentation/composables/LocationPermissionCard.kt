package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
	colors: CardColors = CardDefaults.outlinedCardColors(
		containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
	),
	borderStroke: BorderStroke = CardDefaults.outlinedCardBorder(),
	shape: Shape = MaterialTheme.shapes.small,
) {
	OutlinedCard(
		colors = colors,
		shape = shape,
		border = borderStroke,
		modifier = modifier.padding(vertical = 8.dp),
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(4.dp),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(6.dp)
		) {
			Column(
				modifier = Modifier.weight(.7f),
				verticalArrangement = Arrangement.spacedBy(4.dp)
			) {
				Text(
					text = stringResource(id = R.string.location_perms_title),
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = stringResource(id = R.string.location_perms_desc),
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
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