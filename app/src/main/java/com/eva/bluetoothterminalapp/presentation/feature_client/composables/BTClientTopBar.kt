package com.eva.bluetoothterminalapp.presentation.feature_client.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.BTClientStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTClientTopBar(
	connectionState: BTClientStatus,
	navigation: @Composable () -> Unit,
	onConnect: () -> Unit,
	onDisconnect: () -> Unit,
	onClear: () -> Unit,
	modifier: Modifier = Modifier
) {

	TopAppBar(
		title = { Text(text = stringResource(id = R.string.bt_client_route)) },
		navigationIcon = navigation,
		actions = {
			AnimatedConnectDisconnectButton(
				connectionState = connectionState,
				onConnect = onConnect,
				onDisConnect = onDisconnect,
			)
			TooltipBox(
				positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
				tooltip = {
					PlainTooltip {
						Text(text = stringResource(id = R.string.clear_terminal_tooltip_text))
					}
				},
				state = rememberTooltipState()
			) {
				IconButton(onClick = onClear) {
					Icon(
						imageVector = Icons.Outlined.DeleteOutline,
						contentDescription = stringResource(id = R.string.clear_terminal_tooltip_text)
					)
				}
			}
		},
		modifier = modifier,
	)
}
