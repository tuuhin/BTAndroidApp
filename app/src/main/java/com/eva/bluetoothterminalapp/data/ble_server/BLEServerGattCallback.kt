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
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.device.BatteryReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

private typealias SendResponse = (device: BluetoothDevice, requestId: Int, status: Int, offset: Int, value: ByteArray?) -> Unit
private typealias NotifyCharacteristicsChanged = (device: BluetoothDevice, characteristics: BluetoothGattCharacteristic, confirm: Boolean, value: ByteArray) -> Boolean

private const val TAG = "BLE_SERVER_CALLBACK"

class BLEServerGattCallback(
	private val batteryReader: BatteryReader,
	private val uuidReader: SampleUUIDReader,
) : BluetoothGattServerCallback() {

	private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

	private val _connectedDevices = MutableStateFlow<List<BluetoothDeviceModel>>(emptyList())
	val connectedDevices = _connectedDevices.asStateFlow()

	private val _services = MutableStateFlow<List<BLEServiceModel>>(emptyList())
	val services = _services.asStateFlow()

	// key : device address and value : BLEMessageModel
	private val _echoValuesMap = ConcurrentHashMap<String, BLEMessageModel.EchoMessage>()
	private val _nusValuesMap = ConcurrentHashMap<String, BLEMessageModel.NUSMessage>()
	private val _batteryNotificationMap = ConcurrentHashMap<String, Boolean>()

	private var _sendResponse: SendResponse? = null
	private var _notifyCharacteristicsChanged: NotifyCharacteristicsChanged? = null
	private var _onInformServiceAddedCallback: (() -> Unit)? = null

	fun setOnSendResponse(callback: SendResponse) {
		Log.d(TAG, "ON SEND RESPONSE CALLBACK SET!!")
		_sendResponse = callback
	}

	fun setNotifyCharacteristicsChanged(callback: NotifyCharacteristicsChanged) {
		Log.d(TAG, "ON NOTIFY CALLBACK SET!!")
		_notifyCharacteristicsChanged = callback
	}

	fun setOnInformServiceAdded(onServiceAdded: () -> Unit = {}) {
		Log.d(TAG, "ON SERVICE ADDED CALLBACK SET")
		_onInformServiceAddedCallback = onServiceAdded
	}

	override fun onConnectionStateChange(device: BluetoothDevice?, status: Int, newState: Int) {
		if (status != BluetoothGatt.GATT_SUCCESS) {
			Log.e(TAG, "CONNECTION WITH SOME ERROR: $status")
			return
		}
		val message = if (newState == BluetoothProfile.STATE_CONNECTED) "CONNECTED"
		else "DISCONNECTED"

		Log.i(TAG, "CLIENT ADDRESS:${device?.address} $message")
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
				_nusValuesMap.remove(device.address)
				_batteryNotificationMap.remove(device.address)
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

		Log.i(TAG, "SERVICE :${service.uuid} ADDED")

		scope.launch {
			val domainService = service.toDomainModel()
			val sampleUUID = uuidReader.findServiceNameForUUID(service.uuid)
			domainService.probableName = sampleUUID?.name

			_services.update { previous -> (previous + domainService).distinctBy { it.serviceId } }

		}.invokeOnCompletion {
			// inform new service is added
			_onInformServiceAddedCallback?.invoke()
		}
	}

	override fun onCharacteristicReadRequest(
		device: BluetoothDevice?,
		requestId: Int,
		offset: Int,
		characteristic: BluetoothGattCharacteristic?
	) {
		super.onCharacteristicReadRequest(device, requestId, offset, characteristic)
		if (device == null || characteristic == null) return
		Log.i(TAG, "READ REQUESTED FOR CHARACTERISTICS :${characteristic.uuid}")

		val charset = Charsets.UTF_8
		var isFailedResponse = false

		when (characteristic.service.uuid) {
			BLEServerUUID.DEVICE_INFO_SERVICE -> {
				val value = when (characteristic.uuid) {
					BLEServerUUID.MANUFACTURER_NAME -> Build.MANUFACTURER.toByteArray(charset)
					BLEServerUUID.MODEL_NUMBER -> Build.MODEL.toByteArray(charset)
					BLEServerUUID.SOFTWARE_REVISION -> Build.VERSION.SDK_INT.toString()
						.toByteArray(charset)

					BLEServerUUID.HARDWARE_REVISION -> Build.HARDWARE.toByteArray(charset)
					else -> null
				}
				val isSuccess = if (value == null) BluetoothGatt.GATT_FAILURE
				else BluetoothGatt.GATT_SUCCESS
				_sendResponse?.invoke(device, requestId, isSuccess, offset, value)
			}

			BLEServerUUID.ECHO_SERVICE -> {
				if (characteristic.uuid == BLEServerUUID.ECHO_CHARACTERISTIC) {
					val savedValue = _echoValuesMap[device.address]?.message ?: "null"
					val value = savedValue.toByteArray(charset)
					_sendResponse
						?.invoke(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value)
				}
			}

			BLEServerUUID.BATTERY_SERVICE -> {
				if (characteristic.uuid == BLEServerUUID.BATTERY_LEVEL_CHARACTERISTIC) {
					val value = batteryReader.currentBatteryLevel.toString().toByteArray(charset)
					_sendResponse
						?.invoke(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value)
				} else isFailedResponse = true
			}

			else -> isFailedResponse = true
		}

		if (!isFailedResponse) return
		_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)

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
			Log.e(TAG, "WRITE REQUEST WITH EMPTY VALUE ,RESPONSE: $responseNeeded")
			if (!responseNeeded) return
			_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
			return
		}

		val charset = Charsets.UTF_8
		var isFailedResponse = false

		when (characteristic.service.uuid) {
			BLEServerUUID.ECHO_SERVICE -> {
				if (characteristic.uuid == BLEServerUUID.ECHO_CHARACTERISTIC) {
					val current = _echoValuesMap[device.address]
					_echoValuesMap[device.address] = BLEMessageModel.EchoMessage(
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
				}
				// invalid matching characteristics id
				else isFailedResponse = true
			}

			BLEServerUUID.NORDIC_UART_SERVICE -> {
				if (characteristic.uuid == BLEServerUUID.NUS_RX_CHARACTERISTIC) {
					val receivedMessage = value.toString(charset)
					val current = _nusValuesMap[device.address]
					_nusValuesMap[device.address] = BLEMessageModel.NUSMessage(
						rxMessage = receivedMessage,
						isNotifyEnabled = current?.isNotifyEnabled ?: false,
					)
					// notify value changed if enabled
					if (_nusValuesMap[device.address]?.isNotifyEnabled == true) {
						val transmitMessage =
							_nusValuesMap[device.address]?.txMessage ?: "nothing"
						val transmitValue = transmitMessage.toByteArray(charset)
						_notifyCharacteristicsChanged
							?.invoke(device, characteristic, false, transmitValue)
					}
				}
				// rx should be only used to receive value, tx is for transmitting
				else isFailedResponse = true
			}

			else -> isFailedResponse = true
		}
		// if this is a failed response send response as failure
		if (!isFailedResponse) return
		if (!responseNeeded) return
		// empty value with failure
		_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
	}

	override fun onDescriptorReadRequest(
		device: BluetoothDevice?,
		requestId: Int,
		offset: Int,
		descriptor: BluetoothGattDescriptor?
	) {
		if (device == null || descriptor == null) return
		Log.i(
			TAG,
			"DESCRIPTOR READ REQUEST ${descriptor.uuid} CHARACTERISTIC : ${descriptor.characteristic.uuid}"
		)

		when (descriptor.characteristic.uuid) {
			BLEServerUUID.ECHO_CHARACTERISTIC -> {
				val readValue = if (descriptor.uuid == BLEServerUUID.CCC_DESCRIPTOR) {
					val isEnabled = _echoValuesMap[device.address]?.isNotifyEnabled ?: false
					if (isEnabled) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
					else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
				} else null
				val status = if (readValue != null) BluetoothGatt.GATT_SUCCESS
				else BluetoothGatt.GATT_FAILURE
				_sendResponse?.invoke(device, requestId, status, offset, readValue)
			}

			BLEServerUUID.NUS_TX_CHARACTERISTIC -> {
				val readValue = if (descriptor.uuid == BLEServerUUID.CCC_DESCRIPTOR) {
					val isEnabled = _nusValuesMap[device.address]?.isNotifyEnabled ?: false
					if (isEnabled) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
					else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
				} else null
				val status = if (readValue != null) BluetoothGatt.GATT_SUCCESS
				else BluetoothGatt.GATT_FAILURE
				_sendResponse?.invoke(device, requestId, status, offset, readValue)
			}

			BLEServerUUID.BATTERY_LEVEL_CHARACTERISTIC -> {
				val readValue = if (descriptor.uuid == BLEServerUUID.CCC_DESCRIPTOR) {
					val isEnabled = _batteryNotificationMap[device.address] ?: false
					if (isEnabled) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
					else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
				} else null
				val status = if (readValue != null) BluetoothGatt.GATT_SUCCESS
				else BluetoothGatt.GATT_FAILURE
				_sendResponse?.invoke(device, requestId, status, offset, readValue)
			}

			else -> _sendResponse
				?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
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

		Log.i(
			TAG,
			"WRITE REQUEST DESCRIPTOR ID ${descriptor.uuid} CHARACTERISTIC ID : ${descriptor.characteristic.uuid}"
		)

		when (descriptor.uuid) {
			BLEServerUUID.CCC_DESCRIPTOR -> {
				val isNotifyEnabled = when {
					value.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) -> true
					value.contentEquals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE) -> false
					else -> {
						if (!responseNeeded) return
						_sendResponse
							?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
						return
					}
				}

				when (descriptor.characteristic.uuid) {
					BLEServerUUID.ECHO_CHARACTERISTIC -> {
						val currentMessage = _echoValuesMap.get(device.address)?.message ?: "null"
						_echoValuesMap[device.address] = BLEMessageModel.EchoMessage(
							message = currentMessage,
							isNotifyEnabled = isNotifyEnabled
						)
					}

					BLEServerUUID.NUS_TX_CHARACTERISTIC -> {
						val currentMessage = _nusValuesMap.get(device.address)?.message ?: ""
						_nusValuesMap[device.address] = BLEMessageModel.NUSMessage(
							rxMessage = currentMessage,
							isNotifyEnabled = isNotifyEnabled
						)
					}

					BLEServerUUID.BATTERY_LEVEL_CHARACTERISTIC ->
						_batteryNotificationMap[device.address] = isNotifyEnabled
				}
				if (!responseNeeded) return
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null)
			}

			else -> {
				if (!responseNeeded) return
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
			}
		}
	}

	fun onCleanUp() {
		Log.i(TAG, "CLEARING OFF DEVICES MAP")
		_echoValuesMap.clear()
		_nusValuesMap.clear()
		scope.cancel()
	}
}