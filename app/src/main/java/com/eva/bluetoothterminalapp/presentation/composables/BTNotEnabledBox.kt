package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTNotEnabledBox(
	modifier: Modifier = Modifier,
	onResults: (Boolean) -> Unit = {},
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(id = R.drawable.no_bluetooth),
			contentDescription = stringResource(id = R.string.bluetooth_not_enable),
			colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
			modifier = Modifier.sizeIn(maxWidth = 120.dp, maxHeight = 120.dp)
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = stringResource(id = R.string.bluetooth_not_enable_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = stringResource(id = R.string.bluetooth_not_enable_desc),
			style = MaterialTheme.typography.bodySmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center
		)
		Spacer(modifier = Modifier.height(8.dp))
		EnableBluetoothButton(
			onResults = onResults,
			modifier = Modifier.fillMaxWidth(.75f)
		)
	}
}

@PreviewLightDark
@Composable
private fun BTNotEnabledBoxPreview() = BlueToothTerminalAppTheme {
	Surface {
		BTNotEnabledBox(modifier = Modifier.padding(16.dp))
	}
}