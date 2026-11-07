package com.eva.bluetoothterminalapp.data.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SPPConnectionReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		// check the action
		if (intent.action != SPPConnectionConstants.ACTION_SPP_SEND) return

		// retrieve the values from the intent
		val deviceAddress = intent.getStringExtra(SPPConnectionConstants.EXTRA_DEVICE_ADDRESS)
		val sppUuidString = intent.getStringExtra(SPPConnectionConstants.EXTRA_SPP_UUID)
		val payload = intent.getByteArrayExtra(SPPConnectionConstants.EXTRA_PAYLOAD)
		val payloadHex = intent.getStringExtra(SPPConnectionConstants.EXTRA_PAYLOAD_HEX)

		// start the fg service with the received command data
		try {
			val serviceIntent = Intent(context, SPPConnectionService::class.java).apply {
				putExtra(SPPConnectionConstants.EXTRA_DEVICE_ADDRESS, deviceAddress)
				putExtra(SPPConnectionConstants.EXTRA_PAYLOAD, payload)
				putExtra(SPPConnectionConstants.EXTRA_SPP_UUID, sppUuidString)
				putExtra(SPPConnectionConstants.EXTRA_PAYLOAD_HEX, payloadHex)
			}
			context.startForegroundService(serviceIntent)
		} catch (e: Exception) {
			Log.e(TAG, "Failed to start SPP foreground service", e)
		}
	}

	private companion object {
		const val TAG = "SPP_AUTOMATION_RECEIVER"

	}
}