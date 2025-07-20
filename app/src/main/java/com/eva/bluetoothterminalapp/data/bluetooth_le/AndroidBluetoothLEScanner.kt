package com.eva.bluetoothterminalapp.data.bluetooth_le

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEScanner
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.ScanError
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.domain.exceptions.BLENotSupportedException
import com.eva.bluetoothterminalapp.domain.exceptions.BluetoothNotEnabled
import com.eva.bluetoothterminalapp.domain.exceptions.BluetoothPermissionNotProvided
import com.eva.bluetoothterminalapp.domain.exceptions.LocationPermissionNotProvided
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsScanMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsSupportedLayer
import com.eva.bluetoothterminalapp.domain.settings.repository.BLESettingsDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration

private const val LOGGER_TAG = "BLE_SCANNER_TAG"

private typealias BluetoothDevices = List<BluetoothLEDeviceModel>

@SuppressLint("MissingPermission")
class AndroidBluetoothLEScanner(
	private val context: Context,
	private val bleSettings: BLESettingsDataStore,
) : BluetoothLEScanner {

	private val _bluetoothManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _btAdapter: BluetoothAdapter?
		get() = _bluetoothManager?.adapter

	private val _isBTEnabled: Boolean
		get() = _btAdapter?.isEnabled ?: false

	override val hasBTLEFeature: Boolean
		get() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

	private val _hasScanPermission: Boolean
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
			ContextCompat.checkSelfPermission(
				context, Manifest.permission.BLUETOOTH_SCAN
			) == PermissionChecker.PERMISSION_GRANTED
		else true

	private val _hasLocationPermission: Boolean
		get() = ContextCompat.checkSelfPermission(
			context, Manifest.permission.ACCESS_FINE_LOCATION
		) == PermissionChecker.PERMISSION_GRANTED

	private val _devices = MutableStateFlow<BluetoothDevices>(emptyList())
	override val leDevices: StateFlow<BluetoothDevices>
		get() = _devices.asStateFlow()

	private val _isScanning = MutableStateFlow(false)
	override val isScanning: StateFlow<Boolean>
		get() = _isScanning.asStateFlow()

	private val _scanError = Channel<ScanError>()
	override val scanErrorCode: Flow<ScanError>
		get() = _scanError.receiveAsFlow()

	private val _deviceAddresses: List<String>
		get() = _devices.value.map { it.deviceModel.address }


	private val _bLeScanCallback = object : ScanCallback() {

		override fun onScanResult(callbackType: Int, result: ScanResult?) {
			super.onScanResult(callbackType, result)
			// id its not connectable skip
			if (result?.isConnectable == false) return
			// if results has no address skip
			val address = result?.device?.address ?: return
			// if it's a new device
			if (address !in _deviceAddresses) {
				val newDevice = result.toDomainModel()
				// add it to devices
				_devices.update { devices -> devices + newDevice }
				// then work is done
				return
			}
			//if the address already present
			val updatedList = _devices.value.map { device ->
				// if address already present update the rssi of the device
				if (device.deviceModel.address == address) device.copy(rssi = result.rssi)
				// else return the normal device
				else device
			}
			_devices.update { updatedList }
		}

		override fun onScanFailed(errorCode: Int) {
			super.onScanFailed(errorCode)
			val error = when (errorCode) {
				SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> ScanError.SCAN_OUT_OF_RESOURCES
				SCAN_FAILED_ALREADY_STARTED -> ScanError.SCAN_FAILED_ALREADY_STARTED
				SCAN_FAILED_INTERNAL_ERROR -> ScanError.SCAN_INTERNAL_ERROR
				SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> ScanError.SCAN_APPLICATION_REGISTRATION_FAILED
				SCAN_FAILED_SCANNING_TOO_FREQUENTLY -> ScanError.SCAN_TOO_FREQUENT
				else -> ScanError.SCAN_ERROR_UNKNOWN
			}
			val result = _scanError.trySend(error)
			result.onFailure {
				Log.d(LOGGER_TAG, "FAILED TO SEND ERROR CODE : $error")
			}
		}
	}


	override suspend fun startDiscovery(duration: Duration) {
		//checking for failures
		val result = checkIfPermissionAndBTEnabled()
		result.onFailure { err -> Log.d(LOGGER_TAG, err.message ?: "") }
		if (result.isFailure || _isScanning.value) return
		// if normal scan is running then stop it
		if (_btAdapter?.isDiscovering == true) _btAdapter?.cancelDiscovery()

		val settings = bleSettings.getSettings()
		val breakTime = settings.scanPeriod.duration

		coroutineScope {
			try {
				val postJob = launch(Dispatchers.Main) {
					delay(breakTime)
					stopScanCallback()
				}
				startScanCallBack()
				// the coroutine is queued wait for duration to stop the scan
				postJob.join()
			} catch (_: CancellationException) {
				// if there is a cancellation exception then stop the scan
				stopScanCallback()
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}


	override fun stopDiscovery() {
		val result = checkIfPermissionAndBTEnabled()
		if (result.isFailure) return
		stopScanCallback()
	}


	override fun clearResources() = Unit


	private suspend fun startScanCallBack() {
		if (_isScanning.value) return
		// updates is scanning
		_isScanning.update { true }

		// stop classic scan if running
		if (_btAdapter?.isDiscovering == true)
			_btAdapter?.cancelDiscovery()

		val filters = emptyList<ScanFilter>()

		// it's a blocking call
		val settings = bleSettings.getSettings()

		val scanMode = when (settings.scanMode) {
			BLESettingsScanMode.LOW_POWER -> ScanSettings.SCAN_MODE_LOW_POWER
			BLESettingsScanMode.BALANCED -> ScanSettings.SCAN_MODE_BALANCED
			BLESettingsScanMode.LOW_LATENCY -> ScanSettings.SCAN_MODE_LOW_LATENCY
		}

		// check it later
		val layer = when (settings.supportedLayer) {
			BLESettingsSupportedLayer.ALL -> ScanSettings.PHY_LE_ALL_SUPPORTED
			BLESettingsSupportedLayer.LEGACY -> BluetoothDevice.PHY_LE_1M
			BLESettingsSupportedLayer.LONG_RANGE -> BluetoothDevice.PHY_LE_CODED
		}

		val isLegacyOnly = settings.isLegacyOnly

		val scanSettings = ScanSettings.Builder()
			.setScanMode(scanMode)
			.setLegacy(isLegacyOnly)
			.setPhy(layer)
			.build()

		_btAdapter?.bluetoothLeScanner?.startScan(filters, scanSettings, _bLeScanCallback)
		Log.d(LOGGER_TAG, "SCAN STARTED")
	}


	private fun stopScanCallback() {
		// scan is not running so nothing to stop
		if (!_isScanning.value) return
		// stop the scan details
		_isScanning.update { false }
		_btAdapter?.bluetoothLeScanner?.stopScan(_bLeScanCallback)
		Log.d(LOGGER_TAG, "SCAN STOPPED")
	}

	private fun checkIfPermissionAndBTEnabled(): Result<Unit> = when {
		!hasBTLEFeature -> Result.failure(BLENotSupportedException())
		!_isBTEnabled -> Result.failure(BluetoothNotEnabled())
		!_hasScanPermission -> Result.failure(BluetoothPermissionNotProvided())
		!_hasLocationPermission -> Result.failure(LocationPermissionNotProvided())
		else -> Result.success(Unit)
	}
}