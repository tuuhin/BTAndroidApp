package com.eva.bluetoothterminalapp.data.bluetooth_le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothStatusCodes
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.bluetooth.BTConstants
import com.eva.bluetoothterminalapp.data.mapper.canIndicate
import com.eva.bluetoothterminalapp.data.mapper.canNotify
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.exceptions.BLECharacteristicInvalidForIndicateOrNotify
import com.eva.bluetoothterminalapp.domain.exceptions.BLEClientConfigDescriptorNotFound
import com.eva.bluetoothterminalapp.domain.exceptions.BLEServiceCharacteristicMissingException
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

private const val BLE_CLIENT_LOGGER = "BLE_CLIENT_LOGGER"

@SuppressLint("MissingPermission")
class AndroidBLEClientConnector(
	private val context: Context,
	private val reader: SampleUUIDReader,
) : BluetoothLEClientConnector {

	private val gattCallback = BLEClientGattCallback(reader)

	private val _bluetoothManager by lazy { context.getSystemService<BluetoothManager>() }

	private val _btAdapter: BluetoothAdapter?
		get() = _bluetoothManager?.adapter

	override val connectionState: StateFlow<BLEConnectionState>
		get() = gattCallback.connectionState

	override val deviceRssi: StateFlow<Int>
		get() = gattCallback.deviceRssi

	override val bleServices: Flow<List<BLEServiceModel>>
		get() = gattCallback.bleServicesFlowAsDomainModel

	override val readForCharacteristic: Flow<BLECharacteristicsModel?>
		get() = gattCallback.readCharacteristicWithDescriptors

	private var _connectedDevice: BluetoothDeviceModel? = null
	override val connectedDevice: BluetoothDeviceModel?
		get() = _connectedDevice


	private var _bLEGatt: BluetoothGatt? = null
	private var _charWithNotfication: BluetoothGattCharacteristic? = null

	override fun connect(address: String, autoConnect: Boolean): Result<Boolean> {
		return try {

			val device = _btAdapter?.getRemoteDevice(address)
				?: return Result.success(false)
			// update the device
			_connectedDevice = device.toDomainModel()
			// connect to the gatt server
			_bLEGatt = device.connectGatt(
				context,
				autoConnect,
				gattCallback,
				BluetoothDevice.TRANSPORT_LE
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

	override fun discoverServices(): Result<Boolean> {
		return try {
			val isSuccess = _bLEGatt?.discoverServices() ?: false
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

			val _characteristic = gattCallback.findCharacteristicFromDomainModel(
				service = service,
				characteristic = characteristic
			) ?: return Result.failure(BLEServiceCharacteristicMissingException())

			val isSuccess = _bLEGatt?.readCharacteristic(_characteristic) ?: false

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

			val _characteristic = gattCallback.findCharacteristicFromDomainModel(
				service = service,
				characteristic = characteristic
			) ?: return Result.failure(BLEServiceCharacteristicMissingException())

			val isSuccess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				val operation = _bLEGatt?.writeCharacteristic(
					_characteristic,
					bytes,
					_characteristic.writeType
				)

				operation == BluetoothStatusCodes.SUCCESS
			} else {
				_characteristic.value = bytes
				_bLEGatt?.writeCharacteristic(_characteristic) ?: false
			}

			Result.success(isSuccess)
		} catch (e: Exception) {
			e.printStackTrace()
			Result.failure(e)
		}
	}

	override fun setIndicationOrNotification(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		enable: Boolean
	): Result<Boolean> {
		return try {
			val _characteristic = gattCallback.findCharacteristicFromDomainModel(
				service = service,
				characteristic = characteristic
			) ?: return Result.failure(BLEServiceCharacteristicMissingException())

			// set characteristic notifications
			val isSuccess = _bLEGatt?.setCharacteristicNotification(_characteristic, enable)
				?: false

			// if characteristic notification enable success
			if (isSuccess) {
				// if any other is running stop it
				// TODO: Check if its stoped in the correct position or not
				if (_charWithNotfication != null) stopRunningNotificationOrIndication()

				val descriptorValue = when {
					enable && characteristic.canNotify -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
					enable && characteristic.canIndicate -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
					!enable -> BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
					else -> return Result.failure(BLECharacteristicInvalidForIndicateOrNotify())
				}

				// needs a client config descriptor if not found then its a failure
				val descriptor = _characteristic
					.getDescriptor(BTConstants.CLIENT_CONFIG_DESCRIPTOR_UUID)
					?: return Result.failure(BLEClientConfigDescriptorNotFound())

				val isWritten = writeToDescriptor(
					descriptor = descriptor,
					bytes = descriptorValue
				)

				val canStart = enable && isWritten.isSuccess
				_charWithNotfication = if (canStart) _characteristic else null
			}


			Result.success(isSuccess)
		} catch (e: Exception) {
			e.printStackTrace()
			Result.failure(e)
		}
	}

	override fun readDescriptor(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		descriptor: BLEDescriptorModel
	): Result<Boolean> {
		return try {

			val _descriptor = gattCallback.findDescriptorFromDomainModel(
				service = service,
				characteristic = characteristic,
				descriptor = descriptor
			)

			val isSuccess = _bLEGatt?.readDescriptor(_descriptor) ?: false

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
			// cancels the scope
			gattCallback.cancelAwaitingTasks()
			// close the gatt server
			_bLEGatt?.close()
			_bLEGatt = null
			Log.d(BLE_CLIENT_LOGGER, "GATT CLIENT CLOSED")
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	@Suppress("DEPRECATION")
	private fun writeToDescriptor(
		descriptor: BluetoothGattDescriptor,
		bytes: ByteArray,
	): Result<Boolean> {
		return try {
			val isSuccess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				val operation = _bLEGatt?.writeDescriptor(descriptor, bytes)
				operation == BluetoothStatusCodes.SUCCESS
			} else {
				descriptor.value = bytes
				_bLEGatt?.writeDescriptor(descriptor) ?: false
			}
			Result.success(isSuccess)
		} catch (e: Exception) {
			e.printStackTrace()
			Result.failure(e)
		}
	}

	private fun stopRunningNotificationOrIndication() {
		val _characteristic = _charWithNotfication ?: return

		val descriptor = _characteristic.getDescriptor(BTConstants.CLIENT_CONFIG_DESCRIPTOR_UUID)
			?: return

		writeToDescriptor(
			descriptor = descriptor,
			bytes = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
		)
		_bLEGatt?.setCharacteristicNotification(_characteristic, false)
	}

}