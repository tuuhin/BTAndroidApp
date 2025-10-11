package com.eva.bluetoothterminalapp.data.ble_server

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.os.Build
import android.util.Log
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap

private typealias SendResponse = (device: BluetoothDevice, requestId: Int, status: Int, offset: Int, value: ByteArray?) -> Unit
private typealias NotifyCharacteristicsChanged = (device: BluetoothDevice, characteristics: BluetoothGattCharacteristic, confirm: Boolean, value: ByteArray) -> Boolean

private const val TAG = "BLE_SERVER_CALLBACK"

class BLEServerGattCallback : BluetoothGattServerCallback() {

	private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

	private val _connectedDevices = MutableStateFlow<List<BluetoothDeviceModel>>(emptyList())
	val connectedDevices = _connectedDevices.asStateFlow()

	private val _services = MutableStateFlow<List<BLEServiceModel>>(emptyList())
	val services = _services.asStateFlow()

	// key : device address and value : BLEMessageModel
	private val _echoValuesMap = ConcurrentHashMap<String, BLEMessageModel>()
	private val _nusValuesMap = ConcurrentHashMap<String, BLEMessageModel>()

	private var _sendResponse: SendResponse? = null
	private var _notifyCharacteristicsChanged: NotifyCharacteristicsChanged? = null

	fun setOnSendResponse(callback: SendResponse) {
		_sendResponse = callback
	}

	fun setNotifyCharacteristicsChanged(callback: NotifyCharacteristicsChanged) {
		_notifyCharacteristicsChanged = callback
	}

	override fun onConnectionStateChange(device: BluetoothDevice?, status: Int, newState: Int) {
		if (status != BluetoothGatt.GATT_SUCCESS) {
			Log.e(TAG, "CONNECTION WITH SOME ERROR: $status")
			return
		}
		Log.d(TAG, "CLIENT ADDRESS:${device?.address} CONNECTION: $newState")
		val domainDevice = device?.toDomainModel() ?: return

		when (newState) {
			BluetoothProfile.STATE_CONNECTED -> _connectedDevices.update { devices ->
				val newList = devices + domainDevice
				newList.distinctBy { it.address }
			}

			BluetoothProfile.STATE_DISCONNECTED -> {
				_connectedDevices.update { devices ->
					val filteredList = devices.filterNot { it.address == domainDevice.address }
					filteredList.distinctBy { it.address }
				}
				// remove the device
				_echoValuesMap.remove(device.address)
			}

			else -> {}
		}

	}

	override fun onServiceAdded(status: Int, service: BluetoothGattService?) {
		if (status != BluetoothGatt.GATT_SUCCESS) {
			Log.e(TAG, "SOME ERROR IN ADDING THE SERVICE: $status")
			return
		}
		if (service == null) return
		val domainService = service.toDomainModel()
		_services.update { previous -> (previous + domainService).distinctBy { it.serviceId } }
	}

	override fun onCharacteristicReadRequest(
		device: BluetoothDevice?,
		requestId: Int,
		offset: Int,
		characteristic: BluetoothGattCharacteristic?
	) {
		if (device == null || characteristic == null) return
		Log.e(TAG, "READ REQUESTED FOR CHARACTERISTICS :${characteristic.uuid}")
		val charset = Charsets.UTF_8

		when (characteristic.service.uuid) {
			BLEServerUUID.DEVICE_INFO_SERVICE -> {
				val value = when (characteristic.uuid) {
					BLEServerUUID.MANUFACTURER_NAME -> Build.MANUFACTURER.toByteArray(charset)
					BLEServerUUID.MODEL_NUMBER -> Build.MODEL.toByteArray(charset)
					BLEServerUUID.SOFTWARE_REVISION -> byteArrayOf(Build.VERSION.SDK_INT.toByte())
					BLEServerUUID.HARDWARE_REVISION -> Build.HARDWARE.toByteArray(charset)
					else -> null
				}
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value)
			}

			BLEServerUUID.ECHO_SERVICE -> {
				val savedValue = _echoValuesMap[device.address]?.message ?: "null"
				val value = savedValue.toByteArray(charset)
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value)
			}

			else -> {
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
			}
		}
	}

	override fun onCharacteristicWriteRequest(
		device: BluetoothDevice?,
		requestId: Int,
		characteristic: BluetoothGattCharacteristic?,
		preparedWrite: Boolean,
		responseNeeded: Boolean,
		offset: Int,
		value: ByteArray?
	) {
		if (device == null || characteristic == null) return
		Log.e(TAG, "WRITE REQUESTED FOR CHARACTERISTICS :${characteristic.uuid}")

		if (value == null) {
			Log.d(TAG, "WRITE REQUEST WITH EMPTY VALUE")
			if (responseNeeded) {
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
			}
			return
		}

		val charset = Charsets.UTF_8

		when (characteristic.service.uuid) {
			BLEServerUUID.ECHO_SERVICE -> {
				if (characteristic.uuid == BLEServerUUID.ECHO_CHARACTERISTIC) {
					val current = _echoValuesMap[device.address]
					_echoValuesMap[device.address] = BLEMessageModel(
						message = value.toString(charset),
						isNotifyEnabled = current?.isNotifyEnabled ?: false
					)

					// check if notify enabled
					if (_echoValuesMap[device.address]?.isNotifyEnabled == true) {
						// we are always working with notification not indication thus no client validation
						_notifyCharacteristicsChanged?.invoke(device, characteristic, false, value)
					}
					if (!responseNeeded) return
					_sendResponse
						?.invoke(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value)
				} else {
					if (!responseNeeded) return
					// empty value with failure
					_sendResponse
						?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
				}
			}

			BLEServerUUID.NORDIC_UART_SERVICE -> {
				when (characteristic.uuid) {
					BLEServerUUID.NUS_RX_CHARACTERISTIC -> {}
					BLEServerUUID.NUS_TX_CHARACTERISTIC -> {}
				}
			}

			else -> {
				if (!responseNeeded) return
				// empty value with failure
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
			}
		}
	}

	override fun onDescriptorReadRequest(
		device: BluetoothDevice?,
		requestId: Int,
		offset: Int,
		descriptor: BluetoothGattDescriptor?
	) {
		if (device == null || descriptor == null) return
		Log.d(
			TAG,
			"DESCRIPTOR READ REQUEST ${descriptor.uuid} CHARACTERISTIC : ${descriptor.characteristic.uuid}"
		)

		when (descriptor.characteristic.uuid) {
			BLEServerUUID.ECHO_CHARACTERISTIC -> {
				if (descriptor.uuid == BLEServerUUID.ECHO_DESCRIPTOR) {

					val isNotifyEnabled = _echoValuesMap[device.address]?.isNotifyEnabled ?: false
					val value = if (isNotifyEnabled)
						BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
					else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE

					_sendResponse
						?.invoke(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value)
				} else {
					_sendResponse
						?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
				}
			}

			else -> {
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
			}
		}

	}

	override fun onDescriptorWriteRequest(
		device: BluetoothDevice?,
		requestId: Int,
		descriptor: BluetoothGattDescriptor?,
		preparedWrite: Boolean,
		responseNeeded: Boolean,
		offset: Int,
		value: ByteArray?
	) {
		if (device == null || descriptor == null) return

		Log.d(
			TAG,
			"DESCRIPTOR WRITE REQUEST ${descriptor.uuid} CHARACTERS : ${descriptor.characteristic.uuid}"
		)

		if (value == null) {
			Log.d(TAG, "WRITE REQUEST WITH EMPTY VALUE")
			if (responseNeeded) {
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
			}
			return
		}

		when (descriptor.characteristic.uuid) {
			BLEServerUUID.ECHO_CHARACTERISTIC -> {
				if (descriptor.uuid == BLEServerUUID.ECHO_DESCRIPTOR) {
					val isNotifyEnabled = when {
						value.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) -> true
						value.contentEquals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE) -> false
						else -> {
							Log.w(TAG, "INVALID DESCRIPTOR VALUE ")
							if (!responseNeeded) return
							_sendResponse?.invoke(
								device, requestId, BluetoothGatt.GATT_FAILURE, offset, null
							)
							return
						}
					}
					// find the device and update it
					val currentMessage = _echoValuesMap.get(device.address)?.message ?: "null"
					_echoValuesMap[device.address] = BLEMessageModel(
						message = currentMessage,
						isNotifyEnabled = isNotifyEnabled
					)

					if (!responseNeeded) return
					_sendResponse
						?.invoke(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value)
				} else {
					// invalid descriptor uuid
					if (!responseNeeded) return
					_sendResponse
						?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
				}
			}

			else -> {
				// no matching characteristics found
				if (!responseNeeded) return
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
			}
		}
	}

	fun onCleanUp() {
		_echoValuesMap.clear()
		scope.cancel()
	}
}