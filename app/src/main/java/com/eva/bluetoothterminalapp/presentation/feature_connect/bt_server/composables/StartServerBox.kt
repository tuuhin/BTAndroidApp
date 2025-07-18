package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun StartServerBox(
	onStartServer: () -> Unit,
	modifier: Modifier = Modifier,
) {

	Column(
		modifier = modifier.widthIn(max = dimensionResource(R.dimen.permission_box_min_size)),
		verticalArrangement = Arrangement.spacedBy(6.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Icon(
			painter = painterResource(R.drawable.ic_connect_variant),
			contentDescription = stringResource(id = R.string.bt_server_start_connection_text),
			tint = MaterialTheme.colorScheme.secondary,
			modifier = Modifier.size(128.dp),
		)
		Text(
			text = stringResource(id = R.string.bt_server_start_connection_text),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.primary
		)
		Text(
			text = stringResource(id = R.string.bt_server_start_connection_desc),
			style = MaterialTheme.typography.labelLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center
		)
		Text(
			text = stringResource(R.string.bt_server_start_connection_extra_info),
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.tertiary,
			textAlign = TextAlign.Center
		)
		Button(
			onClick = onStartServer,
			shape = MaterialTheme.shapes.medium,
			modifier = Modifier.defaultMinSize(minWidth = 220.dp)
		) {
			Text(
				text = stringResource(id = R.string.bt_server_start_connection_text),
				fontWeight = FontWeight.SemiBold
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun StartServerBoxPreview() = BlueToothTerminalAppTheme {
	Surface {
		StartServerBox(
			onStartServer = { },
		)
	}
}