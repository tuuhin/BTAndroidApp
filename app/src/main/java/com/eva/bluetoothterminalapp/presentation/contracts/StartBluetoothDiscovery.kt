package com.eva.bluetoothterminalapp.presentation.contracts

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class StartBluetoothDiscovery(
	private val duration: Duration = 1.minutes
) : ActivityResultContract<Unit, Int>() {

	override fun createIntent(context: Context, input: Unit): Intent {
		val seconds = duration.toInt(DurationUnit.SECONDS)
		return Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
			putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, seconds)
		}
	}

	override fun parseResult(resultCode: Int, intent: Intent?): Int {
		return resultCode
	}
}