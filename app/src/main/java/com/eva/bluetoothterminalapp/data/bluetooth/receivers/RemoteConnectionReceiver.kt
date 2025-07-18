package com.eva.bluetoothterminalapp.data.bluetooth.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ClientConnectionState

private typealias RemoteConnectionChangedCallback = (connected: ClientConnectionState, device: BluetoothDevice?) -> Unit

@Suppress("DEPRECATION")
class RemoteConnectionReceiver(
	private val onResults: RemoteConnectionChangedCallback
) : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		val actions = arrayOf(
			BluetoothDevice.ACTION_BOND_STATE_CHANGED,
			BluetoothDevice.ACTION_ACL_CONNECTED,
			BluetoothDevice.ACTION_ACL_DISCONNECTED
		)

		if (intent == null) return
		if (intent.action !in actions) return

		val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)

		val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
		else intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)


		val connectionState = when (intent.action) {
			BluetoothDevice.ACTION_BOND_STATE_CHANGED -> bondStateToConnectionMode(bondState)
			BluetoothDevice.ACTION_ACL_CONNECTED -> ClientConnectionState.CONNECTION_PEER_FOUND
			BluetoothDevice.ACTION_ACL_DISCONNECTED -> ClientConnectionState.CONNECTION_DISCONNECTED
			else -> null
		}

		Log.d("ACTION", "ACTION:${intent.action} CONNECTION STATE:$connectionState")

		connectionState?.let { mode -> onResults(mode, device) }
	}

	private fun bondStateToConnectionMode(bondState: Int): ClientConnectionState? {
		return when (bondState) {
			BluetoothDevice.BOND_BONDED -> ClientConnectionState.CONNECTION_PAIRED
			BluetoothDevice.BOND_BONDING -> ClientConnectionState.CONNECTION_PAIRING
			else -> null
		}
	}
}