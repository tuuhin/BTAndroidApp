package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun BtPermissionNotProvidedBox(
	modifier: Modifier = Modifier,
	onPermissionChanged: (Boolean) -> Unit = {},
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(6.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Surface(
			color = MaterialTheme.colorScheme.secondaryContainer,
			contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
			shape = MaterialShapes.Flower.toShape(),
			modifier = Modifier.sizeIn(maxWidth = 200.dp, maxHeight = 200.dp)
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_permissions),
				contentDescription = stringResource(id = R.string.bluetooth_permission_not_found),
				modifier = Modifier
					.size(120.dp)
					.padding(24.dp)
			)
		}
		Spacer(modifier = Modifier.height(12.dp))
		Text(
			text = stringResource(id = R.string.bluetooth_permission_not_found_title),
			style = MaterialTheme.typography.titleMediumEmphasized,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.primary
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = stringResource(id = R.string.bluetooth_permission_not_found_desc),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center
		)
		BluetoothPermissionButton(onResults = onPermissionChanged)
	}
}

@PreviewLightDark
@Composable
private fun BTPermissionNotProvidedPreview() = BlueToothTerminalAppTheme {
	Surface {
		BtPermissionNotProvidedBox(modifier = Modifier.padding(16.dp))
	}
}