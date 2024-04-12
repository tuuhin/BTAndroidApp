package com.eva.bluetoothterminalapp.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScanDiscoveryReceiver(
	private val onchange: (BTScanDiscoveryStatus) -> Unit,
) : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		if (intent?.action == null) return
		// if any other device is scanning then this Discover will also respond
		when (intent.action) {
			BluetoothAdapter.ACTION_DISCOVERY_STARTED -> onchange(BTScanDiscoveryStatus.SCAN_STATED)
			BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> onchange(BTScanDiscoveryStatus.SCAN_ENDED)
		}
	}
}