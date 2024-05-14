package com.eva.bluetoothterminalapp.data.bluetooth_le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.os.Build
import android.util.Log
import com.eva.bluetoothterminalapp.data.mapper.toDomainModelWithName
import com.eva.bluetoothterminalapp.data.mapper.toDomainModelWithNames
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val GATT_LOGGER = "BLE_GATT_CALLBACK"

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
class BLEClientGattCallback(
	private val reader: SampleUUIDReader,
	private val echoWrite: Boolean = true,
) : BluetoothGattCallback() {

	private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	private val _deviceRssi = MutableStateFlow(0)
	val deviceRssi = _deviceRssi.asStateFlow()

	private val _connectionState = MutableStateFlow(BLEConnectionState.CONNECTING)
	val connectionState = _connectionState.asStateFlow()


	private val _bleGattServices = MutableStateFlow<List<BluetoothGattService>>(emptyList())
	private val bleGattServicesValue: List<BluetoothGattService>
		get() = _bleGattServices.value

	val bleServicesFlowAsDomainModel = _bleGattServices
		.map { services ->
			services.toDomainModelWithNames(scope = scope, reader = reader)
		}
		.cancellable()
		.catch { err -> Log.d(GATT_LOGGER, "LOCAL MODEL CONVERTION :${err.message}") }
		.flowOn(Dispatchers.IO)

	private val _readCharacteristic = MutableStateFlow<BLECharacteristicsModel?>(null)
	private val _readDescriptor = MutableStateFlow<BLEDescriptorModel?>(null)

	val readCharacteristicWithDescriptors = _readCharacteristic.asStateFlow()

	override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {

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

		if (status != BluetoothGatt.GATT_SUCCESS) return
		// update rssi value
		_deviceRssi.update { rssi }
	}

	override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {

		if (status != BluetoothGatt.GATT_SUCCESS) return

		val services = gatt?.services ?: emptyList()

		_bleGattServices.update { prev ->
			(prev + services).distinctBy(BluetoothGattService::getUuid)
		}
	}

	override fun onServiceChanged(gatt: BluetoothGatt) {
		// re-discover services
		gatt.discoverServices()
	}

	@Deprecated(
		message = "Used natively in Android 12 and lower",
		replaceWith = ReplaceWith("onCharacteristicChanged(gatt, characteristic, characteristic.value)")
	)
	override fun onCharacteristicRead(
		gatt: BluetoothGatt?,
		characteristic: BluetoothGattCharacteristic?,
		status: Int
	) {
		if (gatt == null || characteristic?.value == null) return
		// only use this under API 32
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) return
		onCharacteristicRead(gatt, characteristic, characteristic.value, status)
	}

	override fun onCharacteristicRead(
		gatt: BluetoothGatt,
		characteristic: BluetoothGattCharacteristic,
		value: ByteArray,
		status: Int
	) {
		if (status != BluetoothGatt.GATT_SUCCESS) return
		scope.launch {
			try {
				// decode the received value and decide it\
				val domainModel = characteristic.toDomainModelWithNames(scope, reader)
					.copy(byteArray = value)
				// update the read value
				_readCharacteristic.update { domainModel }

				Log.d(GATT_LOGGER, "VALUE ${domainModel.valueHexString}")
			} catch (e: Exception) {
				Log.e(GATT_LOGGER, "EXCEPTION", e)
				e.printStackTrace()
			}
		}
	}

	override fun onCharacteristicWrite(
		gatt: BluetoothGatt?,
		characteristic: BluetoothGattCharacteristic?,
		status: Int
	) {
		if (status != BluetoothGatt.GATT_SUCCESS) return
		Log.d(GATT_LOGGER, "WRITTEN SUCCESSFULLY")
		if (characteristic == null || !echoWrite) return
		val isSuccess = gatt?.readCharacteristic(characteristic) ?: false
		Log.d(GATT_LOGGER, "UPDATING THE CHARACTERISITC VALUE $isSuccess")
	}

	@Deprecated(
		message = "Used natively in Android 12 and lower",
		replaceWith = ReplaceWith("onDescriptorRead(gatt, descriptor, descriptor.value)")
	)
	override fun onDescriptorRead(
		gatt: BluetoothGatt?,
		descriptor: BluetoothGattDescriptor?,
		status: Int
	) {
		if (gatt == null || descriptor?.value == null) return
		// only use this under API 32
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) return
		onDescriptorRead(gatt, descriptor, status, descriptor.value)
	}

	override fun onDescriptorRead(
		gatt: BluetoothGatt,
		descriptor: BluetoothGattDescriptor,
		status: Int,
		value: ByteArray
	) {
		if (status != BluetoothGatt.GATT_SUCCESS) return

		scope.launch {
			try {
				// decode the received value and decide it
				val domainModel = descriptor.toDomainModelWithName(scope, reader)
					.copy(byteArray = value)

				_readDescriptor.update { domainModel }

				// TODO: Check it later if its required
				if (_readCharacteristic.value?.uuid != descriptor.characteristic.uuid) {
					gatt.readCharacteristic(descriptor.characteristic)
				}

				// make sure characteristic is already read
				_readCharacteristic.update { char ->
					char?.copy(
						descriptors = char.descriptors.map { desc ->
							if (desc.uuid == domainModel.uuid) domainModel
							else desc
						},
					)
				}
				// update the read value
				Log.d(GATT_LOGGER, "VALUE DESC ${domainModel.valueHexString}")
			} catch (e: Exception) {
				Log.e(GATT_LOGGER, "EXCEPTION", e)
				e.printStackTrace()
			}
		}
	}

	override fun onDescriptorWrite(
		gatt: BluetoothGatt?,
		descriptor: BluetoothGattDescriptor?,
		status: Int
	) {
		if (status != BluetoothGatt.GATT_SUCCESS) return
		Log.d(GATT_LOGGER, "WRITTEN VALUE ")
		// re-validating the current value
		if (descriptor == null || !echoWrite) return
		val isSuccess = gatt?.readDescriptor(descriptor) ?: false
		Log.d(GATT_LOGGER, "UPDATED DESCRIPTOR VALUE $isSuccess")
	}

	@Deprecated(
		message = "Used natively in Android 12 and lower",
		replaceWith = ReplaceWith("onCharacteristicChanged(gatt, characteristic, value)")
	)
	override fun onCharacteristicChanged(
		gatt: BluetoothGatt?,
		characteristic: BluetoothGattCharacteristic?
	) {
		if (gatt == null || characteristic == null) return
		// only use this under API 32
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) return
		onCharacteristicChanged(gatt, characteristic, characteristic.value)
	}

	override fun onCharacteristicChanged(
		gatt: BluetoothGatt,
		characteristic: BluetoothGattCharacteristic,
		value: ByteArray
	) {
		scope.launch {
			try {
				// decode the received value and decide it\
				val domainModel = characteristic.toDomainModelWithNames(scope, reader)
					.copy(byteArray = value)
				// update the read value
				_readCharacteristic.update { domainModel }

				Log.d(GATT_LOGGER, "VALUE ${domainModel.valueHexString}")
			} catch (e: Exception) {
				Log.e(GATT_LOGGER, "EXCEPTION", e)
				e.printStackTrace()
			}
		}
	}

	fun cancelAwaitingTasks() = scope.cancel()

	fun findCharacteristicFromDomainModel(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel
	): BluetoothGattCharacteristic? {
		return bleGattServicesValue
			.find { it.uuid == service.serviceUUID }
			?.getCharacteristic(characteristic.uuid)
	}

	fun findDescriptorFromDomainModel(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel,
		descriptor: BLEDescriptorModel,
	): BluetoothGattDescriptor? {

		return bleGattServicesValue
			.find { it.uuid == service.serviceUUID }
			?.getCharacteristic(characteristic.uuid)
			?.getDescriptor(descriptor.uuid)
	}

}