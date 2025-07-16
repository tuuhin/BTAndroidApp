package com.eva.bluetoothterminalapp.data.bluetooth.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

@Suppress("DEPRECATION")
class BondStateChangedReceiver(
	private val onNewDeviceBonded: (BluetoothDevice?) -> Unit,
	private val onOldDeviceUnBonded: (BluetoothDevice?) -> Unit,
) : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent == null || intent.action != BluetoothDevice.ACTION_BOND_STATE_CHANGED) return

		val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
		else intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

		val currentBondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
		val previousBondState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1)

		// Bonding --> Bonded , New device added
		val isNewDevice = previousBondState == BluetoothDevice.BOND_BONDING
				&& currentBondState == BluetoothDevice.BOND_BONDED

		// Bonded -> Bond None , device removed
		val isOldRemove = previousBondState == BluetoothDevice.BOND_BONDED
				&& currentBondState == BluetoothDevice.BOND_NONE

		when {
			isNewDevice -> onNewDeviceBonded(device)
			isOldRemove -> onOldDeviceUnBonded(device)
		}
	}
}