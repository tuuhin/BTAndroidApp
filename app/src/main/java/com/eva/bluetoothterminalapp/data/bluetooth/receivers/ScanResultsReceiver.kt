package com.eva.bluetoothterminalapp.data.bluetooth.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.data.utils.hasBTScanPermission
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel

@Suppress("DEPRECATION")
class ScanResultsReceiver(
	private val onDevice: (BluetoothDeviceModel) -> Unit
) : BroadcastReceiver() {
	override fun onReceive(context: Context?, intent: Intent?) {
		// check for intent and action
		if (intent == null || context?.hasBTScanPermission != true) return
		// matches the correct action
		if (intent.action != BluetoothDevice.ACTION_FOUND) return

		val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
		else intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

		device?.toDomainModel()?.let(onDevice)
	}
}