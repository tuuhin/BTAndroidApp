package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.PeerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTServerTopAppBar(
	serverState: ServerConnectionState,
	peerState: PeerConnectionState,
	onStop: () -> Unit,
	onRestart: () -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
	scrollBehavior: TopAppBarScrollBehavior? = null,
) {
	MediumTopAppBar(
		title = { Text(text = stringResource(id = R.string.bt_server_route)) },
		navigationIcon = navigation,
		actions = {
			AnimatedStopAndRestartButton(
				serverState = serverState,
				peerState = peerState,
				onRestart = onRestart,
				onStop = onStop,
			)
		},
		colors = TopAppBarDefaults.topAppBarColors(actionIconContentColor = MaterialTheme.colorScheme.primary),
		scrollBehavior = scrollBehavior,
		modifier = modifier
	)
}