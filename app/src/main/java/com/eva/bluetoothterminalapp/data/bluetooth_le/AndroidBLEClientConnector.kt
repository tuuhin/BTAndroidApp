package com.eva.bluetoothterminalapp.data.bluetooth_le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.mapper.toModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val BLE_CLIENT_LOGGER = "BLE_CLIENT_LOGGER"

@SuppressLint("MissingPermission")
class AndroidBLEClientConnector(
	private val context: Context
) : BluetoothLEClientConnector {

	private val _bluetoothManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _btAdapter: BluetoothAdapter?
		get() = _bluetoothManager?.adapter

	private var _bLEGatt: BluetoothGatt? = null

	private val _connectionState = MutableStateFlow(BLEConnectionState.UNKNOWN)
	override val connectionState: StateFlow<BLEConnectionState>
		get() = _connectionState.asStateFlow()


	private val _deviceRssi = MutableStateFlow(0)
	override val deviceRssi: StateFlow<Int>
		get() = _deviceRssi.asStateFlow()

	private val _bleServices = MutableStateFlow<List<BLEServiceModel>>(emptyList())
	override val bleServices: StateFlow<List<BLEServiceModel>>
		get() = _bleServices.asStateFlow()

	private val _gattCallback = object : BluetoothGattCallback() {

		override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
			super.onConnectionStateChange(gatt, status, newState)

			if (status != BluetoothGatt.GATT_SUCCESS) return

			val connectionState = when (newState) {
				BluetoothProfile.STATE_CONNECTED -> BLEConnectionState.CONNECTED
				BluetoothProfile.STATE_DISCONNECTED -> BLEConnectionState.DISCONNECTED
				BluetoothProfile.STATE_CONNECTING -> BLEConnectionState.CONNECTING
				BluetoothProfile.STATE_DISCONNECTING -> BLEConnectionState.DISCONNECTING
				else -> BLEConnectionState.UNKNOWN
			}

			_connectionState.update { connectionState }

			if (connectionState != BLEConnectionState.CONNECTED) return

			// signal strength this can change
			val isRssiReadSuccess = gatt?.readRemoteRssi()
			if (isRssiReadSuccess != true) Log.d(BLE_CLIENT_LOGGER, "RSSI READ FAILED")
			//discover services
			val isDiscoverStarted = gatt?.discoverServices()
			if (isDiscoverStarted != true) Log.d(BLE_CLIENT_LOGGER, "DISCOVERY ERROR")


		}


		override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
			super.onReadRemoteRssi(gatt, rssi, status)

			if (status != BluetoothGatt.GATT_SUCCESS) return
			_deviceRssi.update { rssi }
		}

		override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
			super.onServicesDiscovered(gatt, status)

			if (status != BluetoothGatt.GATT_SUCCESS) return
			val services = gatt?.services ?: emptyList()

			val bleServices = services.map(BluetoothGattService::toModel)
			// TODO: Error prone code please check later
			_bleServices.update { bleServices }

		}

		override fun onServiceChanged(gatt: BluetoothGatt) {
			super.onServiceChanged(gatt)
			// re-discover services
			gatt.discoverServices()
		}

	}

	override fun connect(address: String): Result<Boolean> {
		return try {

			val device = _btAdapter?.getRemoteDevice(address)
				?: return Result.success(false)
			// connect to the gatt server
			_bLEGatt = device.connectGatt(context, false, _gattCallback)
			// return success if there is no error
			Result.success(true)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun reconnect(): Result<Boolean> {
		return try {
			val result = _bLEGatt?.connect() ?: false
			Result.success(result)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun disconnect(): Result<Unit> {
		return try {
			_bLEGatt?.disconnect()
			// disconnects the gatt client
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun close() {
		_bLEGatt?.close()
		_bLEGatt = null
	}
}