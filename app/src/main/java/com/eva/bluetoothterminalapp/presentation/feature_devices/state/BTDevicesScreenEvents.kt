package com.eva.bluetoothterminalapp.presentation.feature_devices.state

sealed interface BTDevicesScreenEvents {

	/**
	 * Starts bluetooth classic scan
	 */
	data object StartScan : BTDevicesScreenEvents

	/**
	 * Stop bluetooth classic scan
	 */
	data object StopScan : BTDevicesScreenEvents

	/**
	 * Check if bluetooth permission has changed or not
	 */
	data class OnBTPermissionChanged(val isGranted: Boolean) : BTDevicesScreenEvents

	/**
	 * Start Bluetooth low energy scan
	 */
	data object StartLEDeviceScan : BTDevicesScreenEvents

	/**
	 * Stop Bluetooth low energy scan
	 */
	data object StopLEDevicesScan : BTDevicesScreenEvents

	/**
	 * Stops any running scan,used when switching tabs
	 */
	data object OnStopAnyRunningScan : BTDevicesScreenEvents

}