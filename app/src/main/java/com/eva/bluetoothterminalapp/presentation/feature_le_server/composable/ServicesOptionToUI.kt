package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServerServices

val BLEServerServices.titleText: String
	@Composable
	get() = when (this) {
		BLEServerServices.ECHO_SERVICE -> stringResource(R.string.ble_server_service_option_echo)
		BLEServerServices.UART_SERVICE -> stringResource(R.string.ble_server_service_option_nus)
		BLEServerServices.BATTERY_LEVEL_SERVICE -> stringResource(R.string.ble_server_service_option_battery)
		BLEServerServices.ENVIRONMENT_SENSING_SERVICE -> stringResource(R.string.ble_server_service_option_environment)
		BLEServerServices.DEVICE_SERVICE -> stringResource(R.string.ble_server_service_option_device)
	}

val BLEServerServices.descText: String
	@Composable
	get() = when (this) {
		BLEServerServices.ECHO_SERVICE -> stringResource(R.string.ble_server_service_option_echo_text)
		BLEServerServices.UART_SERVICE -> stringResource(R.string.ble_server_service_option_nus_text)
		BLEServerServices.BATTERY_LEVEL_SERVICE -> stringResource(R.string.ble_server_service_option_battery_text)
		BLEServerServices.ENVIRONMENT_SENSING_SERVICE -> stringResource(R.string.ble_server_service_option_environment_text)
		BLEServerServices.DEVICE_SERVICE -> stringResource(R.string.ble_server_service_option_device_text)
	}
