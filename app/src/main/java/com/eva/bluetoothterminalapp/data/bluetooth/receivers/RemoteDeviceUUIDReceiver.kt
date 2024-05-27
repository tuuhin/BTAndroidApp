package com.eva.bluetoothterminalapp.data.bluetooth.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.ParcelUuid
import java.util.UUID

@Suppress("DEPRECATION")
class RemoteDeviceUUIDReceiver(
	private val onReceivedUUIDs: (List<UUID>) -> Unit = {},
) : BroadcastReceiver() {

	private val baseUUID: UUID
		get() = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB")

	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent == null) return
		if (intent.action != BluetoothDevice.ACTION_UUID) return

		val uuid = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID, ParcelUuid::class.java)
		else intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID)

		val mappedUUID = uuid?.filterIsInstance<ParcelUuid>()
			?.map(ParcelUuid::getUuid)
			// base uuid is not connectable so removing it
			?.filter { it != baseUUID }
			?: emptyList()

		onReceivedUUIDs(mappedUUID)
	}
}