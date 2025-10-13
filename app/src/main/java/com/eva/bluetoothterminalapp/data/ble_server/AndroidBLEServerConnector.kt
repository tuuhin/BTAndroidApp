package com.eva.bluetoothterminalapp.data.ble_server

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothStatusCodes
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.data.utils.hasBTConnectPermission
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BLEServerConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.device.BatteryReader
import com.eva.bluetoothterminalapp.domain.exceptions.BLEAdvertiseUnsupportedException
import com.eva.bluetoothterminalapp.domain.exceptions.BluetoothNotEnabled
import com.eva.bluetoothterminalapp.domain.exceptions.BluetoothPermissionNotProvided
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

private const val TAG = "BLE_SERVER"

@SuppressLint("MissingPermission")
class AndroidBLEServerConnector(
	private val context: Context,
	private val batteryReader: BatteryReader,
	private val uuidReader: SampleUUIDReader,
) : BLEServerConnector {

	private val _bluetoothManager by lazy { context.getSystemService<BluetoothManager>() }
	private val _callback by lazy { BLEServerGattCallback(batteryReader, uuidReader) }

	private var _bleServer: BluetoothGattServer? = null

	private val _isServerRunning = MutableStateFlow(false)
	private val _errorsFlow = MutableSharedFlow<Exception>()

	override val connectedDevices: Flow<List<BluetoothDeviceModel>>
		get() = _callback.connectedDevices

	override val services: Flow<List<BLEServiceModel>>
		get() = _callback.services

	override val isServerRunning: StateFlow<Boolean>
		get() = _isServerRunning

	override val errorsFlow: Flow<Exception>
		get() = _errorsFlow

	private val _advertiseCallback = object : AdvertiseCallback() {
		override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
			super.onStartSuccess(settingsInEffect)
			Log.d(TAG, "ADVERTISEMENT STARTED!!")
		}

		override fun onStartFailure(errorCode: Int) {
			super.onStartFailure(errorCode)
			Log.d(TAG, "FAILED TO START ADVERTISEMENT ERROR_CODE:$errorCode")
			val exception = when (errorCode) {
				ADVERTISE_FAILED_ALREADY_STARTED -> Exception("Advertisement is already running")
				ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> Exception("Too many advertiser")
				ADVERTISE_FAILED_INTERNAL_ERROR -> Exception("Android cannot start advertisement")
				ADVERTISE_FAILED_FEATURE_UNSUPPORTED -> Exception("BLE not supported")
				else -> Exception("Cannot start the advertisement")
			}
			_errorsFlow.tryEmit(exception)
		}
	}

	@Suppress("DEPRECATION")
	override fun onStartServer(): Result<Boolean> {
		if (!context.hasBTConnectPermission) return Result.failure(BluetoothPermissionNotProvided())
		if (_bluetoothManager?.adapter?.isEnabled != true) return Result.failure(BluetoothNotEnabled())
		if (_bluetoothManager?.adapter?.isMultipleAdvertisementSupported == false)
			return Result.failure(BLEAdvertiseUnsupportedException())

		val advertiser = _bluetoothManager?.adapter
			?.bluetoothLeAdvertiser ?: return Result.failure(BLEAdvertiseUnsupportedException())

		_bleServer = _bluetoothManager?.openGattServer(context, _callback)
		Log.i(TAG, "GATT SERVER BEGUN!")
		_isServerRunning.update { true }

		val server = _bleServer ?: return Result.success(false)
		val bleServicesQueue = BLEServerServiceQueue(server)
		// on response callback
		_callback.setOnSendResponse(server::sendResponse)
		_callback.setNotifyCharacteristicsChanged { device, characteristics, confirm, byteArray ->
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
				server.notifyCharacteristicChanged(device, characteristics, confirm, byteArray) ==
						BluetoothStatusCodes.SUCCESS
			else server.notifyCharacteristicChanged(device, characteristics, confirm)
		}
		_callback.setOnInformServiceAdded(bleServicesQueue::addNextService)

		val services = listOf(
			buildBleDeviceInfoService(),
			buildEchoService(),
			buildNordicUARTService(),
			buildBatteryService()
		)
		bleServicesQueue.addServices(
			services = services,
			onComplete = { Log.d(TAG, "SERVICES ADDED :COUNT ${server.services.size}") },
		)
		// advertisement settings
		val settingsBuilder = AdvertiseSettings.Builder()
			.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
			.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
			.setConnectable(true)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
			settingsBuilder.setDiscoverable(true)

		val advertiseSettings = settingsBuilder.build()

		// what to advertise
		val advertiseData = AdvertiseData.Builder()
			.setIncludeDeviceName(true)
			.build()

		Log.d(TAG, "STARTING ADVERTISEMENTS")
		// start advertising
		advertiser.startAdvertising(advertiseSettings, advertiseData, _advertiseCallback)
		return Result.success(true)
	}


	override fun onStopServer() {
		Log.d(TAG, "STOPPING ADVERTISING")
		val advertiser = _bluetoothManager?.adapter?.bluetoothLeAdvertiser ?: return
		advertiser.stopAdvertising(_advertiseCallback)

		_bleServer?.clearServices()
		Log.d(TAG, "STOPPING SERVER")
		_isServerRunning.update { false }
		_bleServer?.close()
		_bleServer = null
	}

	override fun cleanUp() {
		_callback.onCleanUp()
		onStopServer()
	}
}