package com.eva.bluetoothterminalapp.presentation.feature_le_connect.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState

val BLEConnectionState.color: Color
	@Composable
	get() = when (this) {
		BLEConnectionState.CONNECTED -> MaterialTheme.colorScheme.primaryContainer
		BLEConnectionState.DISCONNECTED -> MaterialTheme.colorScheme.primaryContainer
		BLEConnectionState.CONNECTING -> MaterialTheme.colorScheme.tertiaryContainer
		BLEConnectionState.DISCONNECTING -> MaterialTheme.colorScheme.tertiaryContainer
		BLEConnectionState.FAILED -> MaterialTheme.colorScheme.errorContainer
	}

val BLEConnectionState.textResource: String
	@Composable
	get() = when (this) {
		BLEConnectionState.DISCONNECTED -> stringResource(id = R.string.ble_connection_disconnected)
		BLEConnectionState.FAILED -> stringResource(id = R.string.ble_connection_failed)
		BLEConnectionState.DISCONNECTING -> stringResource(id = R.string.ble_connection_disconnecting)
		BLEConnectionState.CONNECTING -> stringResource(R.string.ble_connection_connecting)
		BLEConnectionState.CONNECTED -> stringResource(id = R.string.ble_connection_connected)
	}