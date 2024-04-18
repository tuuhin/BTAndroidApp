package com.eva.bluetoothterminalapp.domain.bluetooth_le


enum class ScanError {
	SCAN_FAILED_ALREADY_STARTED,
	SCAN_APPLICATION_REGISTRATION_FAILED,
	SCAN_INTERNAL_ERROR,
	SCAN_OUT_OF_RESOURCES,
	SCAN_TOO_FREQUENT,
	SCAN_ERROR_UNKNOWN
}