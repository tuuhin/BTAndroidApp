package com.eva.bluetoothterminalapp.presentation.feature_devices.util

import android.os.Build


enum class BluetoothScreenType {

	/**
	 * Is bluetooth enabled
	 */
	BLUETOOTH_NOT_ENABLED,

	/**
	 * Is bluetooth permission granted  by the user
	 * required for [Build.VERSION_CODES.S] and above
	 */
	BLUETOOTH_PERMISSION_GRANTED,

	/**
	 * Is bluetooth permission not provided by the user
	 * required for [Build.VERSION_CODES.S] and above
	 */
	BLUETOOTH_PERMISSION_DENIED,
}