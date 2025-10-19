package com.eva.bluetoothterminalapp.data.ble_server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.getSystemService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

@OptIn(ExperimentalCoroutinesApi::class)
class BLEServerNotificationManager(private val context: Context) {

	private val _btManager by lazy { context.getSystemService<BluetoothManager>() }

	// notifications : latest one and the notification map
	private val _latestNotification = MutableSharedFlow<BLENotificationTypes>(
		extraBufferCapacity = 1,
		onBufferOverflow = BufferOverflow.DROP_OLDEST
	)

	private val _notificationsMap = ConcurrentHashMap<String, Map<BLENotificationTypes, Boolean>>()

	val batteryNotificationDevices: Flow<List<BluetoothDevice>>
		get() = _latestNotification.filter { it == BLENotificationTypes.BATTERY }
			.mapLatest { filterClients(BLENotificationTypes.BATTERY) }

	val illuminationNotificationDevices: Flow<List<BluetoothDevice>>
		get() = _latestNotification.filter { it == BLENotificationTypes.ILLUMINANCE }
			.mapLatest { filterClients(BLENotificationTypes.ILLUMINANCE) }

	private suspend fun filterClients(type: BLENotificationTypes): List<BluetoothDevice> {
		return withContext(Dispatchers.IO) {
			_notificationsMap.asSequence()
				.filter { (_, notificationMap) -> notificationMap[type] == true }
				.mapNotNull { (address, _) ->
					if (BluetoothAdapter.checkBluetoothAddress(address)) {
						_btManager?.adapter?.getRemoteDevice(address)
					} else null
				}
				.toList()
		}
	}

	fun isNotificationEnabled(address: String, type: BLENotificationTypes) =
		_notificationsMap[address]?.get(type) ?: false

	fun onUpdateNotification(address: String, type: BLENotificationTypes, isEnabled: Boolean) {
		val resultMap = (_notificationsMap[address] ?: emptyMap()).toMutableMap()
		resultMap.put(type, isEnabled)
		_notificationsMap.put(address, resultMap)
		_latestNotification.tryEmit(type)
	}

	fun onClear() = _notificationsMap.clear()
}
