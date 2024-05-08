package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTClientTopBar(
	clientState: ClientConnectionState,
	onReconnect: () -> Unit,
	onDisconnect: () -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	scrollBehavior: TopAppBarScrollBehavior? = null,
	colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors(),
) {
	TopAppBar(
		title = { Text(text = stringResource(id = R.string.bt_client_route)) },
		navigationIcon = navigation,
		actions = {
			AnimatedConnectDisconnectButton(
				clientState = clientState,
				onConnect = onReconnect,
				onDisConnect = onDisconnect,
			)
		},
		colors = colors,
		scrollBehavior = scrollBehavior,
		modifier = modifier,
	)
}


private class ClientConnectionStatePreviewParmas :
	CollectionPreviewParameterProvider<ClientConnectionState>(
		listOf(
			ClientConnectionState.CONNECTION_ACCEPTED,
			ClientConnectionState.CONNECTION_DISCONNECTED,
			ClientConnectionState.CONNECTION_DENIED
		)
	)

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun AnimatedConnectDisconnectButtonPreview(
	@PreviewParameter(ClientConnectionStatePreviewParmas::class)
	state: ClientConnectionState,
) = BlueToothTerminalAppTheme {
	BTClientTopBar(
		clientState = state,
		onDisconnect = {},
		onReconnect = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		}
	)
}