package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTClientTopBar(
	clientState: ClientConnectionState,
	onReconnect: () -> Unit,
	onDisconnect: () -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	scrollBehavior: TopAppBarScrollBehavior? = null,
	colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
	MediumTopAppBar(
		title = { Text(text = stringResource(id = R.string.bt_client_route)) },
		navigationIcon = navigation,
		actions = {
			AnimatedConnectDisconnectButton(
				clientState = clientState,
				onConnect = onReconnect,
				onDisConnect = onDisconnect,
			)
			Spacer(modifier = Modifier.width(4.dp))
		},
		colors = colors,
		scrollBehavior = scrollBehavior,
		modifier = modifier,
	)
}


private class ClientConnectionStatePreviewParams :
	CollectionPreviewParameterProvider<ClientConnectionState>(
		listOf(
			ClientConnectionState.CONNECTION_CONNECTED,
			ClientConnectionState.CONNECTION_DISCONNECTED,
			ClientConnectionState.CONNECTION_DENIED
		)
	)

@PreviewLightDark
@Composable
private fun AnimatedConnectDisconnectButtonPreview(
	@PreviewParameter(ClientConnectionStatePreviewParams::class)
	state: ClientConnectionState,
) = BlueToothTerminalAppTheme {
	BTClientTopBar(
		clientState = state,
		onDisconnect = {},
		onReconnect = {},
		navigation = {
			Icon(
				imageVector = AppIconVectors.backArrow,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		}
	)
}