package com.eva.bluetoothterminalapp.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScanDiscoveryReceiver(
	private val onchange: (BtScanDiscoveryMode) -> Unit,
) : BroadcastReceiver() {

	override fun onReceive(context: Context?, intent: Intent?) {
		when (intent?.action) {
			BluetoothAdapter.ACTION_DISCOVERY_STARTED -> onchange(BtScanDiscoveryMode.SCAN_STATED)
			BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> onchange(BtScanDiscoveryMode.SCAN_ENDED)
		}
	}

	enum class BtScanDiscoveryMode {
		SCAN_STATED,
		SCAN_ENDED
	}
}