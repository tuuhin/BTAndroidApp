package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTClientTopBar(
	clientState: ClientConnectionState,
	onReconnect: () -> Unit,
	onDisconnect: () -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	scrollBehavior: TopAppBarScrollBehavior? = null,
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
		colors = TopAppBarDefaults
			.topAppBarColors(actionIconContentColor = MaterialTheme.colorScheme.primary),
		scrollBehavior = scrollBehavior,
		modifier = modifier,
	)
}
