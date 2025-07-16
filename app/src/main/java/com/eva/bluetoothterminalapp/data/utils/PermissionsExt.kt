package com.eva.bluetoothterminalapp.data.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

val Context.hasBTScanPermission: Boolean
	get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		ContextCompat.checkSelfPermission(
			this, Manifest.permission.BLUETOOTH_SCAN
		) == PermissionChecker.PERMISSION_GRANTED
	} else true

val Context.hasLocationPermission: Boolean
	get() = ContextCompat.checkSelfPermission(
		this, Manifest.permission.ACCESS_FINE_LOCATION
	) == PermissionChecker.PERMISSION_GRANTED

val Context.hasBTConnectPermission: Boolean
	get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		ContextCompat.checkSelfPermission(
			this,
			Manifest.permission.BLUETOOTH_CONNECT
		) == PermissionChecker.PERMISSION_GRANTED
	} else true

