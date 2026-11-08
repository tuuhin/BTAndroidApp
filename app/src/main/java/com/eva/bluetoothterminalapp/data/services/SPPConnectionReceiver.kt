package com.eva.bluetoothterminalapp.data.services

import android.app.Activity
import android.app.ForegroundServiceStartNotAllowedException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat

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
			ContextCompat.startForegroundService(context, serviceIntent)
		} catch (e: Exception) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && e is ForegroundServiceStartNotAllowedException) {
				Log.e(TAG, "BACKGROUND START IS NOT ALLOWED FROM BACKGROUND ", e)
				// Report failure to the caller
				resultCode = Activity.RESULT_CANCELED
				resultData = "ERROR: Foreground service start not allowed. Open the app and retry."
				return
			}
			Log.e(TAG, "FAILED TO START THE SPP SERVICE", e)
			resultCode = Activity.RESULT_CANCELED
			resultData = "ERROR: ${e.message}"
		}
	}

	private companion object {
		const val TAG = "SPP_AUTOMATION_RECEIVER"

	}
}