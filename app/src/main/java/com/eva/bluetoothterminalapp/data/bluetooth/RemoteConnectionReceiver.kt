package com.eva.bluetoothterminalapp.data.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.eva.bluetoothterminalapp.domain.models.ClientConnectionState

typealias RemoteConnectionChangedCallback = (connected: ClientConnectionState, device: BluetoothDevice?) -> Unit

@Suppress("DEPRECATION")
class RemoteConnectionReceiver(
	private val onResults: RemoteConnectionChangedCallback
) : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent == null) return

		val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)

		val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
		else intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)


		val connectionState = when (intent.action) {
			BluetoothDevice.ACTION_BOND_STATE_CHANGED -> bondStateToConnectionMode(bondState)
			BluetoothDevice.ACTION_ACL_CONNECTED -> ClientConnectionState.CONNECTION_DEVICE_FOUND
			BluetoothDevice.ACTION_ACL_DISCONNECTED -> ClientConnectionState.CONNECTION_DISCONNECTED
			else -> null
		}

		Log.d("ACTION", "ACTION:${intent.action} BOND_STATE:$bondState")

		connectionState?.let { mode -> onResults(mode, device) }
	}

	private fun bondStateToConnectionMode(bondState: Int): ClientConnectionState? {
		return when (bondState) {
			BluetoothDevice.BOND_BONDED -> ClientConnectionState.CONNECTION_BONDED
			BluetoothDevice.BOND_BONDING -> ClientConnectionState.CONNECTION_BONDING
			else -> null
		}
	}
}