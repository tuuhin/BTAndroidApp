package com.eva.bluetoothterminalapp.data.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.ParcelUuid
import java.util.UUID

class RemoteDeviceUUIDReceiver(
	private val onReceivedUUIDs: (List<UUID>) -> Unit = {},
) : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent == null) return
		if (intent.action != BluetoothDevice.ACTION_UUID) return

		val uuid = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID, ParcelUuid::class.java)
		else intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID)

		val mappedUUID = uuid?.filterIsInstance<ParcelUuid>()
			?.map(ParcelUuid::getUuid)
			?: emptyList()

		onReceivedUUIDs(mappedUUID)
	}
}