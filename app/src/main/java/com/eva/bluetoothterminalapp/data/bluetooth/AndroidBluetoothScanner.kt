package com.eva.bluetoothterminalapp.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothScanner
import com.eva.bluetoothterminalapp.domain.exceptions.BluetoothPermissionNotProvided
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update

private typealias BluetoothDeviceModels = List<BluetoothDeviceModel>

private const val BLUETOOTH_SCANNER = "ANDROID_BLUETOOTH_SCANNER"

class AndroidBluetoothScanner(
	private val context: Context
) : BluetoothScanner {

	private val _bluetoothManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _bluetoothAdapter: BluetoothAdapter?
		get() = _bluetoothManager?.adapter

	private val _isBluetoothActive: Boolean
		get() = _bluetoothAdapter?.isEnabled ?: false

	private val _hasConnectPermission: Boolean
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
			ContextCompat.checkSelfPermission(
				context, Manifest.permission.BLUETOOTH_CONNECT
			) == PackageManager.PERMISSION_GRANTED
		else true


	private val _availableDevices = MutableStateFlow<BluetoothDeviceModels>(emptyList())
	override val availableDevices: StateFlow<List<BluetoothDeviceModel>>
		get() = _availableDevices.asStateFlow()


	private val _bondedDevices = MutableStateFlow<BluetoothDeviceModels>(emptyList())
	override val pairedDevices: StateFlow<List<BluetoothDeviceModel>>
		get() = _bondedDevices.asStateFlow()


	private val receiver = ScanResultsReceiver { device ->
		_availableDevices.update { devices ->
			if (devices.contains(device)) devices
			else devices + device
		}
	}

	override val isScanRunning: Flow<Boolean>
		get() = callbackFlow {
			trySend(false)
			val intentFilter = IntentFilter().apply {
				addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
				addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
			}

			val scanDiscoveryReceiver = ScanDiscoveryReceiver(
				onchange = { result ->
					when (result) {
						ScanDiscoveryReceiver.BtScanDiscoveryMode.SCAN_STATED -> trySend(true)
						ScanDiscoveryReceiver.BtScanDiscoveryMode.SCAN_ENDED -> trySend(false)
					}
				}
			)

			ContextCompat.registerReceiver(
				context,
				scanDiscoveryReceiver,
				intentFilter,
				ContextCompat.RECEIVER_EXPORTED
			)

			awaitClose {
				context.unregisterReceiver(scanDiscoveryReceiver)
			}
		}

	override val isBluetoothActive: Flow<Boolean>
		get() = callbackFlow {
			trySend(_isBluetoothActive)
			val btModeReceiver = BluetoothStateReceiver { isActive ->
				trySend(isActive)
			}

			ContextCompat.registerReceiver(
				context,
				btModeReceiver,
				IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED),
				ContextCompat.RECEIVER_NOT_EXPORTED
			)

			awaitClose {
				context.unregisterReceiver(btModeReceiver)
			}
		}


	@SuppressLint("MissingPermission")
	override fun findPairedDevices(): Result<Unit> {
		if (!_hasConnectPermission)
			return Result.failure(BluetoothPermissionNotProvided())

		val pairedDevices =
			_bluetoothAdapter?.bondedDevices?.map(BluetoothDevice::toDomainModel) ?: emptyList()

		_bondedDevices.update { pairedDevices }
		return Result.success(Unit)
	}

	@SuppressLint("MissingPermission")
	override fun startScan(): Result<Boolean> {
		if (!_hasConnectPermission)
			return Result.failure(BluetoothPermissionNotProvided())
		// register BroadCastReceiver to receive bluetooth devices
		ContextCompat.registerReceiver(
			context,
			receiver,
			IntentFilter(BluetoothDevice.ACTION_FOUND),
			ContextCompat.RECEIVER_EXPORTED
		)
		// start discovery for bluetooth devices
		// it will listen for 12 seconds
		val status = _bluetoothAdapter?.startDiscovery() ?: false
		return Result.success(status)
	}

	@SuppressLint("MissingPermission")
	override fun stopScan(): Result<Boolean> {
		if (!_hasConnectPermission)
			return Result.failure(BluetoothPermissionNotProvided())
		// stop discovery
		val status = _bluetoothAdapter?.cancelDiscovery() ?: false
		// unregister the receiver
		Log.d(BLUETOOTH_SCANNER, "STOPPED")
		context.unregisterReceiver(receiver)
		return Result.success(status)
	}

}