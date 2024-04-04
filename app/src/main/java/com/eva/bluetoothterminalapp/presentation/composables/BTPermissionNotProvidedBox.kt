package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(id = R.drawable.ic_connet),
			contentDescription = stringResource(id = R.string.bluetooth_permission_not_found),
			colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
			modifier = Modifier.sizeIn(maxWidth = 120.dp, maxHeight = 120.dp)
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = stringResource(id = R.string.bluetooth_permission_not_found_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = stringResource(id = R.string.bluetooth_permission_not_found_desc),
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Spacer(modifier = Modifier.height(8.dp))
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