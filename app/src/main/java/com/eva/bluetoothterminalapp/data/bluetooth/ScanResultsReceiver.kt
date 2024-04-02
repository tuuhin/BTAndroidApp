package com.eva.bluetoothterminalapp.data.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel

class ScanResultsReceiver(
	private val onDevice: (BluetoothDeviceModel) -> Unit
) : BroadcastReceiver() {
	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent == null || intent.action != BluetoothDevice.ACTION_FOUND) return

		val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
		else intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

		device?.toDomainModel()?.let(onDevice)
	}
}