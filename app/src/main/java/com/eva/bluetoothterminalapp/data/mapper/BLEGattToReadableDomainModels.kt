package com.eva.bluetoothterminalapp.data.mapper

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun List<BluetoothGattService>.toDomainModelWithNames(
	reader: SampleUUIDReader
): List<BLEServiceModel> = coroutineScope {
	map { gattService ->
		val serviceDeferred = async { reader.findServiceNameForUUID(gattService.uuid) }

		val characteristicDeferred = gattService.characteristics.map { characteristic ->
			async { characteristic.toDomainModelWithNames(reader) }
		}

		val serviceName = serviceDeferred.await()
		// return completed results
		gattService.toDomainModel(
			probableName = serviceName?.name,
			characteristic = characteristicDeferred.awaitAll()
		)
	}
}

suspend fun BluetoothGattCharacteristic.toDomainModelWithNames(reader: SampleUUIDReader)
		: BLECharacteristicsModel = coroutineScope {
	// get the characteristic name if available
	val sampleNameChar = reader.findCharacteristicsNameForUUID(uuid)

	val descriptorsDeferred = descriptors.map { desc ->
		async { desc.toDomainModelWithName(reader) }
	}
	toDomainModel(
		probableName = sampleNameChar?.name,
		descriptors = descriptorsDeferred.awaitAll()
	)
}

suspend fun BluetoothGattDescriptor.toDomainModelWithName(reader: SampleUUIDReader): BLEDescriptorModel {
	val sample = reader.findDescriptorNameForUUID(uuid)
	return toModel(probableName = sample?.name)
}
