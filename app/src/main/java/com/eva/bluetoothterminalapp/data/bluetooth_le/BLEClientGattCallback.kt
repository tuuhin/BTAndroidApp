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
import com.eva.bluetoothterminalapp.data.mapper.toDomainModel
import com.eva.bluetoothterminalapp.data.mapper.toModel
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.CharacteristicsReadValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.koin.core.time.measureDuration

private const val GATT_LOGGER = "BLE_GATT_CALLBACK"

@SuppressLint("MissingPermission")
class BLEClientGattCallback(
	private val reader: SampleUUIDReader
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
			services.toLocalModelWithProbableName(scope = scope, reader = reader)
		}
		.cancellable()
		.catch { err -> Log.d(GATT_LOGGER, "${err.message}") }
		.flowOn(Dispatchers.IO)


	private val _readValue = MutableStateFlow<CharacteristicsReadValue>(CharacteristicsReadValue())
	val readValue = _readValue.asStateFlow()

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

	@Suppress("DEPRECATION")
	@Deprecated(
		message = "Used natively in Android 12 and lower",
		replaceWith = ReplaceWith("onCharacteristicChanged(gatt, characteristic, characteristic.value)")
	)
	override fun onCharacteristicRead(
		gatt: BluetoothGatt?,
		characteristic: BluetoothGattCharacteristic?,
		status: Int
	) {
		if (gatt == null || characteristic == null) return
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

		try {
			// decode the received value and decide it
			val receivedValue = value.decodeToString()
			val domainModel = characteristic.toDomainModel()
			// update the read value
			val readValue = CharacteristicsReadValue(
				characteristic = domainModel,
				valueAsString = receivedValue
			)

			_readValue.update { readValue }
			Log.d(GATT_LOGGER, "VALUE $receivedValue")
		} catch (e: Exception) {
			Log.e(GATT_LOGGER, "EXCEPTION", e)
			e.printStackTrace()
		}
	}

	override fun onCharacteristicWrite(
		gatt: BluetoothGatt?,
		characteristic: BluetoothGattCharacteristic?,
		status: Int
	) {
		if (status != BluetoothGatt.GATT_SUCCESS) return
		Log.d(GATT_LOGGER, "WRITTEN SUCCESSFULLY")
	}


	fun cancel() = scope.cancel()

	fun checkCharacteristic(
		service: BLEServiceModel,
		characteristic: BLECharacteristicsModel
	): BluetoothGattCharacteristic? {
		val gattService = bleGattServicesValue.find { gattService ->
			gattService.instanceId == service.serviceId && gattService.uuid == service.serviceUUID
		}
		val gattCharacteristic = gattService?.characteristics?.find { char ->
			char.instanceId == characteristic.characteristicsId && char.uuid == characteristic.uuid
		}

		return gattCharacteristic
	}

	override fun onDescriptorRead(
		gatt: BluetoothGatt,
		descriptor: BluetoothGattDescriptor,
		status: Int,
		value: ByteArray
	) {
		super.onDescriptorRead(gatt, descriptor, status, value)
	}

}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun List<BluetoothGattService>.toLocalModelWithProbableName(
	scope: CoroutineScope,
	reader: SampleUUIDReader
): List<BLEServiceModel> = withContext(scope.coroutineContext) {
	// load the files for the first time it will load all the files in memeory
	val time = measureDuration { reader.loadFromFiles() }
	Log.d(GATT_LOGGER, "LOADING TIME $time millis")

	return@withContext map { gattService ->
		// reads the sample service name
		// TODO: Check later which is more efficient in loading the samples
		val sampleServiceNameDefered = async(coroutineContext) {
			reader.findServiceNameForUUID(gattService.uuid)
		}

		val characteristicsDefered = gattService.characteristics.map { characteristic ->
			async(coroutineContext) {
				// get the characteristic name if available
				val sampleNameChar = reader.findCharacteristicsNameForUUID(characteristic.uuid)

				val descriptorsDefered = characteristic.descriptors.map { desc ->
					async(coroutineContext) {
						val name = reader.findDescriptorNameForUUID(desc.uuid)
						desc.toModel(probableName = name?.name)
					}
				}

				val descriptors = descriptorsDefered.awaitAll()

				characteristic.toDomainModel(
					probableName = sampleNameChar?.name,
					descriptors = descriptors
				)
			}
		}

		val serviceName = sampleServiceNameDefered.await()
		val characteristics = characteristicsDefered.awaitAll()
		// return completed results
		gattService.toDomainModel(
			probableName = serviceName?.name,
			characteristic = characteristics
		)
	}
}
