package com.eva.bluetoothterminalapp.data.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.eva.bluetoothterminalapp.domain.models.BTClientStatus

class RemoteConnectionReceiver(
	private val onResults: (connected: BTClientStatus, device: BluetoothDevice?) -> Unit
) : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent == null) return

		val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)

		val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
		else intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)


		val action = when (intent.action) {
			BluetoothDevice.ACTION_BOND_STATE_CHANGED -> bondStateToConnectionMode(bondState)
			BluetoothDevice.ACTION_ACL_CONNECTED -> BTClientStatus.CONNECTION_CONNECTED
			BluetoothDevice.ACTION_ACL_DISCONNECTED -> BTClientStatus.CONNECTION_DISCONNECTED
			else -> null
		}

		Log.d("ACTION", "ACTION:${intent.action} BOND_STATE:$bondState")

		action?.let { mode -> onResults(mode, device) }
	}

	private fun bondStateToConnectionMode(bondState: Int): BTClientStatus? {
		return when (bondState) {
			BluetoothDevice.BOND_BONDED -> BTClientStatus.CONNECTION_BONDED
			BluetoothDevice.BOND_BONDING -> BTClientStatus.CONNECTION_BONDING
			else -> null
		}
	}
}