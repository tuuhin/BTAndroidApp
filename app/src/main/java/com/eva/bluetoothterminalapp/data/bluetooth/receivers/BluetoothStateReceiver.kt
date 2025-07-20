package com.eva.bluetoothterminalapp.data.bluetooth.receivers

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothStateReceiver(
	private val isBtOn: (Boolean) -> Unit
) : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent == null || intent.action != BluetoothAdapter.ACTION_STATE_CHANGED) return

		val btState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
		isBtOn.invoke(btState == BluetoothAdapter.STATE_ON)

	}
}