package com.eva.bluetoothterminalapp.data.bluetooth.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.PeerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel

private typealias PeerConnectionChangedCallback = (connected: PeerConnectionState, device: BluetoothDeviceModel?) -> Unit

class PeerConnectionReceiver(
	private val onResults: PeerConnectionChangedCallback
) : BroadcastReceiver() {

	@Suppress("DEPRECATION")
	override fun onReceive(context: Context?, intent: Intent?) {
		val actions = arrayOf(
			BluetoothDevice.ACTION_ACL_CONNECTED,
			BluetoothDevice.ACTION_ACL_DISCONNECTED
		)

		if (intent == null || intent.action !in actions) return
		if (intent.action !in actions) return

		val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
		else intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

		val deviceModel = device?.toDomainModel()


		val connectionState = when (intent.action) {
			BluetoothDevice.ACTION_ACL_CONNECTED -> PeerConnectionState.PEER_CONNECTED
			BluetoothDevice.ACTION_ACL_DISCONNECTED -> PeerConnectionState.PEER_DISCONNECTED
			else -> null
		}
		connectionState?.let { mode -> onResults(mode, deviceModel) }
	}
}