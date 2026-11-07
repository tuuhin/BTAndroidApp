package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun LocationPermissionBox(
	onLocationPermsGranted: (Boolean) -> Unit,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(2.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Surface(
			color = MaterialTheme.colorScheme.secondaryContainer,
			contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
			shape = MaterialShapes.Gem.toShape(),
			modifier = Modifier.sizeIn(maxWidth = 140.dp, maxHeight = 140.dp)
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_map_location),
				contentDescription = stringResource(id = R.string.location_perms_title),
				modifier = Modifier.padding(24.dp)
			)
		}
		Spacer(modifier = Modifier.height(12.dp))
		Text(
			text = stringResource(id = R.string.location_perms_title),
			style = MaterialTheme.typography.titleMediumEmphasized,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.primary
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = stringResource(id = R.string.location_perms_desc),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center
		)
		Spacer(modifier = Modifier.height(12.dp))
		EnableLocationButton(
			onFineLocationAccess = onLocationPermsGranted,
			modifier = Modifier.fillMaxWidth(.75f)
		)
	}

}

@PreviewLightDark
@Composable
private fun LocationPermissionBoxPreview() = BlueToothTerminalAppTheme {
	Surface {
		LocationPermissionBox(
			onLocationPermsGranted = {},
			modifier = Modifier.padding(16.dp)
		)
	}
}