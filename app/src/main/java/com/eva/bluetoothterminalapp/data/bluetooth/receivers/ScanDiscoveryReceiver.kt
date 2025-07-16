package com.eva.bluetoothterminalapp.data.bluetooth.receivers

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.eva.bluetoothterminalapp.data.bluetooth.util.BTScanDiscoveryStatus

class ScanDiscoveryReceiver(
	private val onchange: (BTScanDiscoveryStatus) -> Unit,
) : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {

		val probableAction = arrayOf(
			BluetoothAdapter.ACTION_DISCOVERY_STARTED,
			BluetoothAdapter.ACTION_DISCOVERY_FINISHED
		)
		if (intent?.action == null) return
		// check for the correct actions
		if (intent.action !in probableAction) return
		// if any other device is scanning then this Discover will also respond
		when (intent.action) {
			BluetoothAdapter.ACTION_DISCOVERY_STARTED -> onchange(BTScanDiscoveryStatus.SCAN_STATED)
			BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> onchange(BTScanDiscoveryStatus.SCAN_ENDED)
		}
	}
}