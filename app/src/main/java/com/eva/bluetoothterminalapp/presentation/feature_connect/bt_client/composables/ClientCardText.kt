package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState

val ClientConnectionState.textResource: String
	@Composable
	get() = when (this) {
		ClientConnectionState.CONNECTION_DEVICE_FOUND -> stringResource(id = R.string.connection_device_found)
		ClientConnectionState.CONNECTION_DENIED -> stringResource(id = R.string.connection_denied)
		ClientConnectionState.CONNECTION_DISCONNECTED -> stringResource(id = R.string.connection_disconnected)
		ClientConnectionState.CONNECTION_BONDING -> stringResource(id = R.string.connection_device_bonding)
		ClientConnectionState.CONNECTION_BONDED -> stringResource(id = R.string.connection_bonded)
		ClientConnectionState.CONNECTION_INITIALIZING -> stringResource(R.string.connection_init)
		ClientConnectionState.CONNECTION_ACCEPTED -> stringResource(id = R.string.connection_accepted)
	}