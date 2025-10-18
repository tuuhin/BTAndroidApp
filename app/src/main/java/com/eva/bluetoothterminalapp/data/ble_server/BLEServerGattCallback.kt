package com.eva.bluetoothterminalapp.data.ble_server

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import android.util.Log
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.data.mapper.toDomainModelWithNames
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.device.BatteryReader
import com.eva.bluetoothterminalapp.domain.device.LightSensorReader
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

private typealias SendResponse = (device: BluetoothDevice, requestId: Int, status: Int, offset: Int, value: ByteArray?) -> Unit
private typealias NotifyCharacteristicsChanged = (device: BluetoothDevice, characteristics: BluetoothGattCharacteristic, confirm: Boolean, value: ByteArray) -> Boolean

private const val TAG = "BLE_SERVER_CALLBACK"

class BLEServerGattCallback(
	private val context: Context,
	private val batteryReader: BatteryReader,
	private val lightSensorReader: LightSensorReader,
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

	// notifications : latest one and the notification map
	private val _notificationHandler by lazy { BLEServerNotificationManager(context) }

	private var _sendResponse: SendResponse? = null
	private var _notifyCharacteristicsChanged: NotifyCharacteristicsChanged? = null
	private var _onServiceAdded: (() -> Unit)? = null

	private var _batteryLevelJob: Job? = null
	private var _illuminationSensorJob: Job? = null

	fun setOnSendResponse(callback: SendResponse) {
		_sendResponse = callback
	}

	fun setNotifyCharacteristicsChanged(callback: NotifyCharacteristicsChanged) {
		_notifyCharacteristicsChanged = callback
	}

	fun setOnServiceAdded(onServiceAdded: () -> Unit = {}) {
		_onServiceAdded = onServiceAdded
	}

	fun broadcastBatteryInfo(service: BluetoothGattService) {
		val characteristic = service.getCharacteristic(BLEServerUUID.BATTERY_LEVEL_CHARACTERISTIC)
			?: return

		Log.d(TAG, "BROADCAST BATTERY INFO READY")

		_batteryLevelJob?.cancel()
		_batteryLevelJob = onBroadcastToSubscribers(
			devicesFlow = _notificationHandler.batteryNotificationDevices,
			notificationsFlow = batteryReader.batteryLevelFlow(),
			onNotify = { client, level ->
				val bytes = "$level".toByteArray(Charsets.UTF_8)
				_notifyCharacteristicsChanged?.invoke(client, characteristic, false, bytes)
			}
		)
	}

	fun broadcastIlluminanceInfo(service: BluetoothGattService) {
		val characteristic = service.getCharacteristic(BLEServerUUID.ILLUMINANCE_CHARACTERISTIC)
			?: return

		Log.d(TAG, "BROADCAST ILLUMINATION INFO READY")

		_illuminationSensorJob?.cancel()
		_illuminationSensorJob = onBroadcastToSubscribers(
			devicesFlow = _notificationHandler.illuminationNotificationDevices,
			notificationsFlow = lightSensorReader.readValuesFlow(),
			onNotify = { client, illuminance ->
				val illuminanceValue = (illuminance * 100).toInt() / 100f
				val bytes = "$illuminanceValue".toByteArray(Charsets.UTF_8)
				_notifyCharacteristicsChanged?.invoke(client, characteristic, false, bytes)
			}
		)
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
				_notificationHandler.onClear()
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
			val serviceName = async { uuidReader.findServiceNameForUUID(service.uuid) }
			val characteristicDeferred = service.characteristics.map { characteristic ->
				async { characteristic.toDomainModelWithNames(uuidReader) }
			}
			domainService.probableName = serviceName.await()?.name
			domainService.characteristic = characteristicDeferred.awaitAll().toPersistentList()

			_services.update { previous -> (previous + domainService).distinctBy { it.serviceId } }

		}.invokeOnCompletion {
			// inform new service is added
			_onServiceAdded?.invoke()
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

			BLEServerUUID.ENVIRONMENTAL_SENSING_SERVICE -> {
				if (characteristic.uuid == BLEServerUUID.ILLUMINANCE_CHARACTERISTIC) {
					scope.launch(Dispatchers.IO) {
						var status = BluetoothGatt.GATT_FAILURE
						val value = lightSensorReader.readCurrentValue()?.let { value ->
							status = BluetoothGatt.GATT_SUCCESS
							"$value".toByteArray(Charsets.UTF_8)
						}
						_sendResponse?.invoke(device, requestId, status, offset, value)
					}
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
						val transmitMessage = _nusValuesMap[device.address]?.txMessage ?: "nothing"
						val tValue = transmitMessage.toByteArray(charset)
						_notifyCharacteristicsChanged?.invoke(device, characteristic, false, tValue)
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

			BLEServerUUID.BATTERY_LEVEL_CHARACTERISTIC, BLEServerUUID.ILLUMINANCE_CHARACTERISTIC -> {
				val readValue = if (descriptor.uuid == BLEServerUUID.CCC_DESCRIPTOR) {
					val type = when (descriptor.characteristic.uuid) {
						BLEServerUUID.BATTERY_LEVEL_CHARACTERISTIC -> BLENotificationTypes.BATTERY
						BLEServerUUID.ILLUMINANCE_CHARACTERISTIC -> BLENotificationTypes.ILLUMINANCE
						// this will never be called so not handing it here
						else -> return
					}
					val isEnabled = _notificationHandler.isNotificationEnabled(device.address, type)
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
				// FOR CCC value cannot be null
				if (value == null) {
					Log.e(TAG, "INVALID VALUE , VALUE CANNOT BE NULL FOR DESCRIPTOR")
					if (!responseNeeded) return
					_sendResponse
						?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
					return
				}

				val isNotifyEnabled = when {
					value.contentEquals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) -> true
					value.contentEquals(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE) -> true
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
						val currentMessage = _echoValuesMap.getOrDefault(device.address, null)
							?.message ?: "null"
						_echoValuesMap[device.address] = BLEMessageModel.EchoMessage(
							message = currentMessage,
							isNotifyEnabled = isNotifyEnabled
						)
					}

					BLEServerUUID.NUS_TX_CHARACTERISTIC -> {
						val currentMessage = _nusValuesMap.getOrDefault(device.address, null)
							?.message ?: ""
						_nusValuesMap[device.address] = BLEMessageModel.NUSMessage(
							rxMessage = currentMessage,
							isNotifyEnabled = isNotifyEnabled
						)
					}

					BLEServerUUID.BATTERY_LEVEL_CHARACTERISTIC -> {
						_notificationHandler.onUpdateNotification(
							device.address,
							BLENotificationTypes.BATTERY,
							isNotifyEnabled
						)
					}

					BLEServerUUID.ILLUMINANCE_CHARACTERISTIC -> {
						_notificationHandler.onUpdateNotification(
							device.address,
							BLENotificationTypes.ILLUMINANCE,
							isNotifyEnabled
						)
					}

					else -> {
						Log.w(TAG, "INVALID CHARACTERISTICS FOR CCC DESCRIPTOR")
						// not matching characteristic id
						if (!responseNeeded) return
						_sendResponse
							?.invoke(device, requestId, BluetoothGatt.GATT_FAILURE, offset, null)
						return
					}
				}
				if (!responseNeeded) return
				_sendResponse?.invoke(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value)
			}

			else -> {
				// not matching descriptor id
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

	@OptIn(ExperimentalCoroutinesApi::class)
	private fun <T> onBroadcastToSubscribers(
		devicesFlow: Flow<List<BluetoothDevice>>,
		notificationsFlow: Flow<T>,
		onNotify: suspend (device: BluetoothDevice, bytes: T) -> Unit,
		onCancel: () -> Unit = {},
	): Job {
		return devicesFlow.onEach { clients ->
			if (clients.isEmpty()) onCancel()
		}.flatMapLatest { clients ->
			if (clients.isEmpty()) emptyFlow()
			else notificationsFlow.map { notificationValue -> clients to notificationValue }
		}.onEach { (clients, bytes) ->
			for (client in clients) onNotify(client, bytes)
		}.launchIn(scope)
	}
}