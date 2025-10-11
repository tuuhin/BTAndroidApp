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
import com.eva.bluetoothterminalapp.data.utils.hasBTConnectPermission
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BLEServerConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.coroutines.flow.Flow

private const val TAG = "BLE_SERVER"

@SuppressLint("MissingPermission")
class AndroidBLEServerConnector(
	private val context: Context,
) : BLEServerConnector {

	private val _bluetoothManager by lazy { context.getSystemService<BluetoothManager>() }
	private val _callback by lazy { BLEServerGattCallback() }

	private var _bleServer: BluetoothGattServer? = null

	override val connectedDevices: Flow<List<BluetoothDeviceModel>>
		get() = _callback.connectedDevices

	override val services: Flow<List<BLEServiceModel>>
		get() = _callback.services

	private val _advertiseCallback = object : AdvertiseCallback() {
		override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
			super.onStartSuccess(settingsInEffect)
			Log.d(TAG, "ADVERTISEMENT STARTED!!")
		}

		override fun onStartFailure(errorCode: Int) {
			super.onStartFailure(errorCode)
			Log.d(TAG, "FAILED TO START ADVERTISEMENT ERROR_CODE:$errorCode")
		}
	}

	@Suppress("DEPRECATION")
	override suspend fun onStartServer() {
		if (!context.hasBTConnectPermission) return
		if (_bluetoothManager?.adapter?.isEnabled != true) return
		if (_bluetoothManager?.adapter?.isMultipleAdvertisementSupported == false) return

		val advertiser = _bluetoothManager?.adapter?.bluetoothLeAdvertiser ?: return

		_bleServer = _bluetoothManager?.openGattServer(context, _callback)

		val server = _bleServer ?: return
		// on response callback
		_callback.setOnSendResponse(server::sendResponse)
		_callback.setNotifyCharacteristicsChanged { device, characteristics, confirm, byteArray ->
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
				server.notifyCharacteristicChanged(device, characteristics, confirm, byteArray) ==
						BluetoothStatusCodes.SUCCESS
			else server.notifyCharacteristicChanged(device, characteristics, confirm)
		}

		// required services
		server.addService(bleDeviceInfoService)
		server.addService(echoService)
		server.addService(nordicUARTService)

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

		// start advertising
		advertiser.startAdvertising(advertiseSettings, advertiseData, _advertiseCallback)
	}


	override suspend fun onStopServer() {
		_callback.onCleanUp()
		_bleServer?.close()
		_bleServer = null
	}
}