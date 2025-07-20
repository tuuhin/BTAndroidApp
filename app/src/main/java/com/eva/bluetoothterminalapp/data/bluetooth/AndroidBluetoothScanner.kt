package com.eva.bluetoothterminalapp.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.bluetooth.receivers.BluetoothStateReceiver
import com.eva.bluetoothterminalapp.data.bluetooth.receivers.BondStateChangedReceiver
import com.eva.bluetoothterminalapp.data.bluetooth.receivers.ScanDiscoveryReceiver
import com.eva.bluetoothterminalapp.data.bluetooth.receivers.ScanResultsReceiver
import com.eva.bluetoothterminalapp.data.bluetooth.util.BTScanDiscoveryStatus
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.data.utils.hasBTScanPermission
import com.eva.bluetoothterminalapp.data.utils.hasLocationPermission
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothScanner
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.exceptions.BluetoothPermissionNotProvided
import com.eva.bluetoothterminalapp.domain.exceptions.LocationPermissionNotProvided
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update

private typealias BluetoothDeviceModels = List<BluetoothDeviceModel>

private const val BLUETOOTH_SCANNER = "ANDROID_BLUETOOTH_SCANNER"

@SuppressLint("MissingPermission")
class AndroidBluetoothScanner(
	private val context: Context
) : BluetoothScanner {

	private val _bluetoothManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _bluetoothAdapter: BluetoothAdapter?
		get() = _bluetoothManager?.adapter

	private val _isBluetoothActive: Boolean
		get() = _bluetoothAdapter?.isEnabled ?: false

	override val isBTDiscovering: Boolean
		get() = if (hasBTPermissions) _bluetoothAdapter?.isDiscovering ?: false
		else false

	private val _hasLocationPermission: Boolean
		get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) context.hasLocationPermission
		else true

	override val hasBTPermissions: Boolean
		get() = context.hasBTScanPermission


	private val _availableDevices = MutableStateFlow<BluetoothDeviceModels>(emptyList())
	override val availableDevices: StateFlow<BluetoothDeviceModels>
		get() = _availableDevices.asStateFlow()

	private val _bondedDevices = MutableStateFlow<BluetoothDeviceModels>(emptyList())
	override val pairedDevices: StateFlow<BluetoothDeviceModels>
		get() = _bondedDevices.asStateFlow()

	private val bondedDeviceAddress: List<String>
		get() = _bondedDevices.value.map(BluetoothDeviceModel::address)

	private val _scannedAddresses: List<String>
		get() = _availableDevices.value.map(BluetoothDeviceModel::address)


	private val _scanReceiver = ScanResultsReceiver { device ->
		// if the device address is already in bonded list skip
		if (device.address in bondedDeviceAddress) return@ScanResultsReceiver
		// if the address already contains in the scan devices then also skip
		if (device.address in _scannedAddresses) return@ScanResultsReceiver
		// otherwise add this device
		_availableDevices.update { devices -> devices + device }
	}

	private val _bondDeviceUpdatedReceiver = BondStateChangedReceiver(
		onNewDeviceBonded = { newDevice ->
			// if no new device so do nothing
			val deviceModel = newDevice?.toDomainModel() ?: return@BondStateChangedReceiver
			// if the device address already in bonded devices list then also do nothing
			if (newDevice.address in bondedDeviceAddress) return@BondStateChangedReceiver
			// if the device address in available device remove it from scan devices
			if (newDevice.address in _scannedAddresses)
				_availableDevices.update { devices -> devices.filter { it.address != newDevice.address } }
			// otherwise add it
			_bondedDevices.update { devices -> devices + deviceModel }
		},
		onOldDeviceUnBonded = { oldDevice ->
			// if no new device so do nothing
			val deviceModel = oldDevice?.toDomainModel() ?: return@BondStateChangedReceiver
			// if the device address matches the old devices address then remove it
			if (oldDevice.address !in bondedDeviceAddress) return@BondStateChangedReceiver
			// remove the item and update the list
			_bondedDevices.update { devices -> devices.filter { it.address != deviceModel.address } }
		},
	)

	override val isScanRunning: Flow<Boolean>
		get() = callbackFlow {
			trySend(isBTDiscovering)
			val intentFilter = IntentFilter().apply {
				addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
				addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
			}

			val scanDiscoveryReceiver = ScanDiscoveryReceiver(
				onchange = { result ->
					Log.d(BLUETOOTH_SCANNER, "SCANNING :$result")
					when (result) {
						BTScanDiscoveryStatus.SCAN_STATED -> trySend(true)
						BTScanDiscoveryStatus.SCAN_ENDED -> trySend(false)
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
				ContextCompat.RECEIVER_EXPORTED
			)

			awaitClose {
				context.unregisterReceiver(btModeReceiver)
			}
		}


	override fun findPairedDevices(): Result<Unit> {
		if (!hasBTPermissions)
			return Result.failure(BluetoothPermissionNotProvided())

		val pairedDevices = _bluetoothAdapter?.bondedDevices
			?.map(BluetoothDevice::toDomainModel) ?: emptyList()

		ContextCompat.registerReceiver(
			context,
			_bondDeviceUpdatedReceiver,
			IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED),
			ContextCompat.RECEIVER_EXPORTED
		)

		_bondedDevices.update { pairedDevices }
		return Result.success(Unit)
	}


	override fun startScan(): Result<Boolean> {
		if (!hasBTPermissions) return Result.failure(BluetoothPermissionNotProvided())
		// checks only for android 11 and lower otherwise its always false
		if (!_hasLocationPermission) return Result.failure(LocationPermissionNotProvided())
		// register BroadCastReceiver to receive bluetooth devices
		ContextCompat.registerReceiver(
			context,
			_scanReceiver,
			IntentFilter(BluetoothDevice.ACTION_FOUND),
			ContextCompat.RECEIVER_EXPORTED
		)
		// start discovery for bluetooth devices
		// it will listen for 12 seconds
		Log.d(BLUETOOTH_SCANNER, "SCAN INITIATED")
		val status = _bluetoothAdapter?.startDiscovery() ?: false
		return Result.success(status)
	}


	override fun stopScan(): Result<Boolean> {
		if (!hasBTPermissions)
			return Result.failure(BluetoothPermissionNotProvided())
		// stop discovery
		Log.d(BLUETOOTH_SCANNER, "SCAN CANCELED")
		val status = _bluetoothAdapter?.cancelDiscovery() ?: false
		return Result.success(status)
	}


	override fun releaseResources() {
		try {
			// remove the broadcast receiver for find device
			context.unregisterReceiver(_scanReceiver)
			// remove broadcast from newly bounded devices
			context.unregisterReceiver(_bondDeviceUpdatedReceiver)
		} catch (e: Exception) {
			Log.e(BLUETOOTH_SCANNER, "RECEIVER ALREADY REMOVED", e)
		}
	}

}