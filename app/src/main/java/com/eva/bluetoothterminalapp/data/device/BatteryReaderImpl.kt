package com.eva.bluetoothterminalapp.data.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.domain.device.BatteryReader
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BatteryReaderImpl(private val context: Context) : BatteryReader {

	private val _batteryManager by lazy { context.getSystemService<BatteryManager>() }

	override val isBatteryCharging: Boolean
		get() = _batteryManager?.isCharging ?: false

	override val currentBatteryLevel: Int
		get() = _batteryManager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 0

	override fun isBatteryChargingFlow(): Flow<Boolean> {
		return callbackFlow {

			val receiver = object : BroadcastReceiver() {
				override fun onReceive(context: Context?, intent: Intent?) {
					when (intent?.action) {
						Intent.ACTION_POWER_CONNECTED -> trySend(true)
						Intent.ACTION_POWER_DISCONNECTED -> trySend(false)
					}
				}
			}

			val intentFilter = IntentFilter().apply {
				addAction(Intent.ACTION_POWER_CONNECTED)
				addAction(Intent.ACTION_POWER_DISCONNECTED)
			}

			ContextCompat.registerReceiver(
				context,
				receiver,
				intentFilter,
				ContextCompat.RECEIVER_EXPORTED
			)

			awaitClose { context.unregisterReceiver(receiver) }
		}
	}

	override fun batteryLevelFlow(): Flow<Int> {
		return callbackFlow {

			val receiver = object : BroadcastReceiver() {
				override fun onReceive(context: Context?, intent: Intent?) {
					if (intent?.action != Intent.ACTION_BATTERY_CHANGED) return

					val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
					val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
					val batteryLevel = (level * 100f / scale).coerceIn(0f..100f).toInt()
					trySend(batteryLevel)
				}
			}

			ContextCompat.registerReceiver(
				context,
				receiver,
				IntentFilter(Intent.ACTION_BATTERY_CHANGED),
				ContextCompat.RECEIVER_EXPORTED
			)

			awaitClose { context.unregisterReceiver(receiver) }
		}
	}
}