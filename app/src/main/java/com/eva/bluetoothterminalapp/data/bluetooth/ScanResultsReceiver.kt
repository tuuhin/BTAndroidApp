package com.eva.bluetoothterminalapp.data.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel

@Suppress("DEPRECATION")
class ScanResultsReceiver(
	private val onDevice: (BluetoothDeviceModel) -> Unit
) : BroadcastReceiver() {
	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent == null) return

		val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
		else intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

		val hasPermission = context?.let {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
				ContextCompat.checkSelfPermission(
					context,
					Manifest.permission.BLUETOOTH_SCAN
				) == PermissionChecker.PERMISSION_GRANTED
			else true
		} ?: false

		when {
			hasPermission && intent.action == BluetoothDevice.ACTION_FOUND -> device?.toDomainModel()
				?.let(onDevice)

			!hasPermission -> Log.d("RECEIVER_ERROR", "DON'T HAVE PERMISSION")
		}

	}
}