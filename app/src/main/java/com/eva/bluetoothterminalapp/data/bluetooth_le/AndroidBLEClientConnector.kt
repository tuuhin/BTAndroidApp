package com.eva.bluetoothterminalapp.data.bluetooth_le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothStatusCodes
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.data.mapper.canIndicate
import com.eva.bluetoothterminalapp.data.mapper.canNotify
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.exceptions.BLECharacteristicIndicateOrNotifyPropertyException
import com.eva.bluetoothterminalapp.domain.exceptions.BLEIndicationOrNotifyRunningException
import com.eva.bluetoothterminalapp.domain.exceptions.BLEServiceAndCharacteristicMatchNotFoundException
import com.eva.bluetoothterminalapp.presentation.util.BTConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val TAG = "BLE_CLIENT_LOGGER"

@SuppressLint("MissingPermission")
@Suppress("DEPRECATION")
class AndroidBLEClientConnector(
	private val context: Context,
	reader: SampleUUIDReader,
) : BluetoothLEClientConnector {

	private val gattCallback = BLEClientGattCallback(reader, echoWrite = true)

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
		get() = gattCallback.readCharacteristics

	private var _connectedDevice: BluetoothDeviceModel? = null
	override val connectedDevice: BluetoothDeviceModel?
		get() = _connectedDevice


	private val _isNotifyOrIndicationRunning = MutableStateFlow(false)
	override val isNotifyOrIndicationRunning: StateFlow<Boolean>
		get() = _isNotifyOrIndicationRunning.asStateFlow()


	private var _bLEGatt: BluetoothGatt? = null

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
			Log.d(TAG, "CONNECT GATT")
			// return success if there is no error
			Result.success(true)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun checkRssi(): Result<Boolean> {
		return try {
			Log.i(TAG, "CHECKING RSSI")
			val isSuccess = _bLEGatt?.readRemoteRssi() ?: false
			Result.success(isSuccess)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun discoverServices(): Result<Boolean> {
		return try {
			Log.i(TAG, "DISCOVERING SERVICES")
			val isSuccess = _bLEGatt?.discoverServices() ?: false
			Result.success(isSuccess)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun reconnect(): Result<Boolean> {
		return try {
			Log.i(TAG, "CLIENT RE-CONNECTED")
			val result = _bLEGatt?.connect() ?: false
			Result.success(result)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun read(service: BLEServiceModel, characteristic: BLECharacteristicsModel)
			: Result<Boolean> {
		return try {
			val gattCharacteristic = gattCallback.findCharacteristicFromDomainModel(
				service = service,
				characteristic = characteristic
			) ?: return Result.failure(BLEServiceAndCharacteristicMatchNotFoundException())

			val isSuccess = _bLEGatt?.readCharacteristic(gattCharacteristic) ?: false

			Log.i(TAG, "CHARACTERISTIC ${characteristic.uuid}  SUCCESS:$isSuccess")

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

			val gattCharacteristic = gattCallback.findCharacteristicFromDomainModel(
				service = service,
				characteristic = characteristic
			) ?: return Result.failure(BLEServiceAndCharacteristicMatchNotFoundException())

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

			Log.i(
				TAG,
				"WRITE TO CHARACTERISTIC ${characteristic.uuid}  SUCCESS:$isSuccess"
			)

			Result.success(isSuccess)
		} catch (e: Exception) {
			e.printStackTrace()
			Result.failure(e)
		}
	}

	override fun startIndicationOrNotification(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		enable: Boolean
	): Result<Boolean> {
		return try {
			// if enable true and notify or indication is already running then no need to
			// start indication or notify
			if (!characteristic.isIndicateOrNotify)
				return Result.failure(BLECharacteristicIndicateOrNotifyPropertyException())

			if (enable && _isNotifyOrIndicationRunning.value) {
				Log.i(TAG, "INDICATION OR NOTIFICATION ALREADY RUNNING")
				return Result.failure(BLEIndicationOrNotifyRunningException())
			}

			val gattCharacteristic = gattCallback.findCharacteristicFromDomainModel(
				service = service,
				characteristic = characteristic
			) ?: return Result.failure(BLEServiceAndCharacteristicMatchNotFoundException())

			// set characteristic notifications
			val isSuccess = _bLEGatt?.setCharacteristicNotification(gattCharacteristic, enable)
				?: false

			Log.d(
				TAG,
				"CHARACTERISTIC NOTIFICATION :$enable UUID ${characteristic.uuid} SUCCESS: $isSuccess"
			)

			// if not success return isSuccess as false
			if (!isSuccess) return Result.success(false)

			// as its successfully started mark it as active
			_isNotifyOrIndicationRunning.update { enable }
			Log.i(TAG, "INDICATION OR NOTIFICATION MARKED AS $enable")


			// set the descriptor value for client config
			val descriptorValue = when {
				enable && characteristic.canNotify -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
				enable && characteristic.canIndicate -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
				!enable -> BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
				else -> return Result.failure(BLECharacteristicIndicateOrNotifyPropertyException())
			}

			// needs a client config descriptor if not found then it's a failure
			val isWriteDescriptor = gattCharacteristic
				.getDescriptor(BTConstants.CLIENT_CONFIG_DESCRIPTOR_UUID)
				?.let { desc -> writeToDescriptor(descriptor = desc, bytes = descriptorValue) }

			Log.d(TAG, "IS WRITE DESC SUCCESS ${isWriteDescriptor?.isSuccess}")

			Result.success(true)
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

			val probableDescriptor = gattCallback.findDescriptorFromDomainModel(
				service = service,
				characteristic = characteristic,
				descriptor = descriptor
			)

			val isSuccess = _bLEGatt?.readDescriptor(probableDescriptor) ?: false

			Log.i(TAG, "READ DESCRIPTOR $probableDescriptor  SUCCESS:$isSuccess")

			Result.success(isSuccess)
		} catch (e: Exception) {
			e.printStackTrace()
			Result.failure(e)
		}
	}

	override fun writeToDescriptor(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		descriptor: BLEDescriptorModel,
		value: String
	): Result<Boolean> {
		return try {
			val bytes = value.encodeToByteArray()

			val probableDescriptor = gattCallback.findDescriptorFromDomainModel(
				service = service,
				characteristic = characteristic,
				descriptor = descriptor
			) ?: return Result.failure(BLEServiceAndCharacteristicMatchNotFoundException())

			val isSuccess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				val operation = _bLEGatt?.writeDescriptor(probableDescriptor, bytes)
				operation == BluetoothStatusCodes.SUCCESS
			} else {
				probableDescriptor.value = bytes
				_bLEGatt?.writeDescriptor(probableDescriptor) ?: false
			}

			Log.i(
				TAG,
				"WRITE TO DESCRIPTOR ${characteristic.uuid}  SUCCESS:$isSuccess"
			)

			Result.success(isSuccess)
		} catch (e: Exception) {
			e.printStackTrace()
			Result.failure(e)
		}
	}

	override fun disconnect(): Result<Unit> {
		return try {
			_bLEGatt?.disconnect()
			Log.d(TAG, "CLIENT DISCONNECTED")
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
			Log.d(TAG, "GATT CLIENT CLOSED")
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	@Suppress("DEPRECATION")
	private fun writeToDescriptor(descriptor: BluetoothGattDescriptor, bytes: ByteArray)
			: Result<Boolean> {
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
}