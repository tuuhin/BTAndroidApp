package com.eva.bluetoothterminalapp.data.bluetooth_le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothStatusCodes
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.CharacteristicsReadValue
import com.eva.bluetoothterminalapp.domain.exceptions.BLEServiceCharacteristicMissingException
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

private const val BLE_CLIENT_LOGGER = "BLE_CLIENT_LOGGER"

@SuppressLint("MissingPermission")
class AndroidBLEClientConnector(
	private val context: Context,
	private val gattCallback: BLEClientGattCallback,
) : BluetoothLEClientConnector {

	private val _bluetoothManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _btAdapter: BluetoothAdapter?
		get() = _bluetoothManager?.adapter

	override val connectionState: StateFlow<BLEConnectionState>
		get() = gattCallback.connectionState

	override val deviceRssi: StateFlow<Int>
		get() = gattCallback.deviceRssi

	override val bleServices: Flow<List<BLEServiceModel>>
		get() = gattCallback.bleServicesFlowAsDomainModel

	override val readValue: StateFlow<CharacteristicsReadValue>
		get() = gattCallback.readValue

	private var _connectedDevice: BluetoothDeviceModel? = null
	override val connectedDevice: BluetoothDeviceModel?
		get() = _connectedDevice

	private var _bLEGatt: BluetoothGatt? = null

	override fun connect(address: String): Result<Boolean> {
		return try {

			val device = _btAdapter?.getRemoteDevice(address)
				?: return Result.success(false)
			// update the device
			_connectedDevice = device.toDomainModel()
			// connect to the gatt server
			_bLEGatt = device.connectGatt(
				context, false, gattCallback, BluetoothDevice.TRANSPORT_LE
			)

			Log.d(BLE_CLIENT_LOGGER, "CONNECT GATT")
			// return success if there is no error
			Result.success(true)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun checkRssi(): Result<Boolean> {
		return try {
			val isSuccess = _bLEGatt?.readRemoteRssi() ?: false
			Result.success(isSuccess)
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

	override fun read(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel
	): Result<Boolean> {
		return try {
			val gattCharacteristic = gattCallback.checkCharacteristic(service, characteristic)
				?: return Result.failure(BLEServiceCharacteristicMissingException())

			val isSuccess = _bLEGatt?.readCharacteristic(gattCharacteristic) ?: false
			Result.success(isSuccess)
		} catch (e: Exception) {
			e.printStackTrace()
			Result.failure(e)
		}
	}

	@Suppress("DEPRECATION")
	override fun write(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		value: String
	): Result<Boolean> {
		return try {
			val bytes = value.encodeToByteArray()
			val gattCharacteristic = gattCallback.checkCharacteristic(service, characteristic)
				?: return Result.failure(BLEServiceCharacteristicMissingException())

			val isSuccess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				val operation = _bLEGatt?.writeCharacteristic(
					gattCharacteristic,
					bytes,
					gattCharacteristic.writeType
				)
				operation == BluetoothStatusCodes.SUCCESS
			} else {
				gattCharacteristic.value = bytes
				_bLEGatt?.writeCharacteristic(gattCharacteristic) ?: false
			}

			Result.success(isSuccess)
		} catch (e: Exception) {
			e.printStackTrace()
			Result.failure(e)
		}
	}

	override fun setNotification(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		enable: Boolean
	): Result<Boolean> {
		return try {
			val gattCharacteristic = gattCallback.checkCharacteristic(service, characteristic)
				?: return Result.failure(BLEServiceCharacteristicMissingException())

			val isSuccess = _bLEGatt?.setCharacteristicNotification(gattCharacteristic, enable)
				?: false

			Result.success(isSuccess)
		} catch (e: Exception) {
			e.printStackTrace()
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
			gattCallback.cancel()
			Log.d(BLE_CLIENT_LOGGER, "GATT CLIENT CLOSED")
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}