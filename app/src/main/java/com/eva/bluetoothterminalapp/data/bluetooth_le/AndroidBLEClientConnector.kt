package com.eva.bluetoothterminalapp.data.bluetooth_le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.data.mapper.toModel
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val BLE_CLIENT_LOGGER = "BLE_CLIENT_LOGGER"

@SuppressLint("MissingPermission")
class AndroidBLEClientConnector(
	private val context: Context,
	private val reader: SampleUUIDReader,
) : BluetoothLEClientConnector {

	private val _bluetoothManager by lazy { context.getSystemService<BluetoothManager>() }

	private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

	private val _btAdapter: BluetoothAdapter?
		get() = _bluetoothManager?.adapter

	private var _bLEGatt: BluetoothGatt? = null

	private val _connectionState = MutableStateFlow(BLEConnectionState.CONNECTING)
	override val connectionState: StateFlow<BLEConnectionState>
		get() = _connectionState.asStateFlow()


	private val _deviceRssi = MutableStateFlow(0)
	override val deviceRssi: StateFlow<Int>
		get() = _deviceRssi.asStateFlow()

	private var _connectedDevice: BluetoothDeviceModel? = null
	override val connectedDevice: BluetoothDeviceModel?
		get() = _connectedDevice


	private val _bleServices = MutableStateFlow<List<BLEServiceModel>>(emptyList())
	override val bleServices: StateFlow<List<BLEServiceModel>>
		get() = _bleServices.asStateFlow()


	private val _gattCallback = object : BluetoothGattCallback() {


		override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
			super.onConnectionStateChange(gatt, status, newState)

			Log.d(BLE_CLIENT_LOGGER, "STATUS :$status")

			if (status != BluetoothGatt.GATT_SUCCESS) {
				_connectionState.update { BLEConnectionState.FAILED }
				return
			}


			val connectionState = when (newState) {
				BluetoothProfile.STATE_CONNECTED -> BLEConnectionState.CONNECTED
				BluetoothProfile.STATE_DISCONNECTED -> BLEConnectionState.DISCONNECTED
				BluetoothProfile.STATE_CONNECTING -> BLEConnectionState.CONNECTING
				BluetoothProfile.STATE_DISCONNECTING -> BLEConnectionState.DISCONNECTING
				else -> BLEConnectionState.FAILED
			}

			_connectionState.update { connectionState }

			if (connectionState != BLEConnectionState.CONNECTED) return

			// signal strength this can change
			gatt?.readRemoteRssi()

			//discover services
			gatt?.discoverServices()

		}


		override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
			super.onReadRemoteRssi(gatt, rssi, status)

			if (status != BluetoothGatt.GATT_SUCCESS) return
			Log.d(BLE_CLIENT_LOGGER, "RSSI UPDATED $rssi")
			_deviceRssi.update { rssi }
		}

		override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
			super.onServicesDiscovered(gatt, status)

			if (status != BluetoothGatt.GATT_SUCCESS) return
			val services = gatt?.services ?: emptyList()

			scope.launch {
				val bleServices = services.map { gattService ->
					// get the service name if available
					val sampleServiceName = reader.findServiceNameForUUID(gattService.uuid)

					val characteristics = async {
						gattService.characteristics.map { characteristic ->
							val sample = reader.findCharacteristicsNameForUUID(characteristic.uuid)
							characteristic.toModel(probableName = sample?.name)
						}
					}.await()

					gattService.toModel(
						sampleName = sampleServiceName?.name,
						characteristic = characteristics
					)
				}
				_bleServices.update { bleServices }
			}

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
			// update the device
			_connectedDevice = device.toDomainModel()
			// connect to the gatt server
			_bLEGatt = device
				.connectGatt(context, false, _gattCallback, BluetoothDevice.TRANSPORT_LE)
			Log.d(BLE_CLIENT_LOGGER, "CONNECT GATT")
			// return success if there is no error
			Result.success(true)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun reconnect(): Result<Boolean> {
		return try {
			val result = _bLEGatt?.connect() ?: false
			Log.d(BLE_CLIENT_LOGGER, "CLIENT RE-CONNECTED")
			Result.success(result)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun disconnect(): Result<Unit> {
		return try {
			_bLEGatt?.disconnect()
			Log.d(BLE_CLIENT_LOGGER, "CLIENT DISCONNECTED")
			// disconnects the gatt client
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun close() {
		try {
			_connectedDevice = null
			// close the gatt server
			_bLEGatt?.close()
			_bLEGatt = null
			// cancels the scope
			scope.cancel()
			Log.d(BLE_CLIENT_LOGGER, "GATT CLIENT CLOSED")
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}