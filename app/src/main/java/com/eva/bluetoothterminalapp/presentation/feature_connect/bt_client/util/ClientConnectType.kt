package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.util

import androidx.annotation.StringRes
import com.eva.bluetoothterminalapp.R

enum class ClientConnectType {
	CONNECT_TO_DEVICE,
	CONNECT_TO_SERVER;

	@get:StringRes
	val resource: Int
		get() = when (this) {
			CONNECT_TO_DEVICE -> R.string.client_connect_to_device
			CONNECT_TO_SERVER -> R.string.client_connect_to_server
		}

}