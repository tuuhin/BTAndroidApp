package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTConnectToServerButton(
	onConnectAsServer: () -> Unit,
	modifier: Modifier = Modifier,
) {

	val floatingActionButtonToolTipState = rememberTooltipState()

	TooltipBox(
		positionProvider = TooltipDefaults
			.rememberRichTooltipPositionProvider(spacingBetweenTooltipAndAnchor = 4.dp),
		tooltip = {
			RichTooltip(
				title = { Text(text = stringResource(id = R.string.bt_connection_type_server)) },
				text = {
					Text(text = stringResource(id = R.string.bt_connection_type_server_desc))
				},
				shape = MaterialTheme.shapes.medium,
				tonalElevation = 2.dp,
				colors = TooltipDefaults.richTooltipColors(
					containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
					contentColor = MaterialTheme.colorScheme.onSurface
				)
			)
		},
		state = floatingActionButtonToolTipState
	) {
		ExtendedFloatingActionButton(
			onClick = onConnectAsServer,
			shape = MaterialTheme.shapes.large,
			modifier = modifier
		) {
			Icon(
				imageVector = Icons.Outlined.Dns,
				contentDescription = stringResource(id = R.string.bt_connection_type_server)
			)
			Spacer(modifier = Modifier.width(4.dp))
			Text(text = stringResource(id = R.string.bt_connection_type_server))
		}
	}
}

@PreviewLightDark
@Composable
private fun BTConnectToServerButtonPreview() = BlueToothTerminalAppTheme {
	BTConnectToServerButton(onConnectAsServer = { })
}