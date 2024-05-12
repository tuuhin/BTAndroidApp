package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.util.Log
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.koin.core.time.measureDuration
import org.koin.core.time.measureDurationForResult

suspend fun List<BluetoothGattService>.toDomainModelWithNames(
	scope: CoroutineScope,
	reader: SampleUUIDReader
): List<BLEServiceModel> = withContext(scope.coroutineContext) {
	// load the files for the first time it will load all the files in memeory
	val time = measureDuration { reader.loadFromFiles() }
	Log.d("READ DURATION", "LOADING TIME $time millis")

	return@withContext map { gattService ->
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

suspend fun BluetoothGattCharacteristic.toDomainModelWithNames(
	scope: CoroutineScope,
	reader: SampleUUIDReader
): BLECharacteristicsModel = withContext(scope.coroutineContext) {
	// get the characteristic name if available
	val sampleNameChar = reader.findCharacteristicsNameForUUID(uuid)

	val descriptorsDefered = descriptors.map { desc ->
		async(coroutineContext) {
			val name = reader.findDescriptorNameForUUID(desc.uuid)
			desc.toModel(probableName = name?.name)
		}
	}

	val descriptors = descriptorsDefered.awaitAll()

	toDomainModel(
		probableName = sampleNameChar?.name,
		descriptors = descriptors
	)
}

suspend fun BluetoothGattDescriptor.toDomainModelWithName(
	scope: CoroutineScope,
	reader: SampleUUIDReader
): BLEDescriptorModel = withContext(scope.coroutineContext) {
	val (matchingSample, time) = measureDurationForResult {
		reader.findDescriptorNameForUUID(uuid)
	}
	Log.d("READ_DURATION", "CHARACTERISTICS READ $time")
	toModel(probableName = matchingSample?.name)
}
