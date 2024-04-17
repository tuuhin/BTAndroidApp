package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState

val ClientConnectionState.color: Color
	@Composable
	get() = when (this) {
		ClientConnectionState.CONNECTION_INITIALIZING -> MaterialTheme.colorScheme.surfaceContainer
		ClientConnectionState.CONNECTION_ACCEPTED, ClientConnectionState.CONNECTION_DEVICE_FOUND ->
			MaterialTheme.colorScheme.primaryContainer

		ClientConnectionState.CONNECTION_DENIED, ClientConnectionState.CONNECTION_DISCONNECTED ->
			MaterialTheme.colorScheme.tertiaryContainer

		ClientConnectionState.CONNECTION_BONDING, ClientConnectionState.CONNECTION_BONDED ->
			MaterialTheme.colorScheme.secondaryContainer
	}