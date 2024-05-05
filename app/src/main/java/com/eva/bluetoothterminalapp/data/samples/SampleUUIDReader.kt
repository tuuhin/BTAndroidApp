package com.eva.bluetoothterminalapp.data.samples

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.koin.core.time.measureDuration
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.util.UUID

private const val READER_LOGGER = "SAMPLE_SERVICE_READER"

@OptIn(ExperimentalSerializationApi::class)
class SampleUUIDReader(private val context: Context) {

	private val assetsManager: AssetManager
		get() = context.assets

	private val _serviceUUIDCache = hashMapOf<UUID, SampleBLEUUIDModel>()

	private val _characteristicsUUIDCache = hashMapOf<UUID, SampleBLEUUIDModel>()

	private suspend fun loadServiceUUIDFromFile() = withContext(Dispatchers.IO) {
		try {

			val time = measureDuration {
				val inputStream = assetsManager.open("ble_service_uuids.json")
				val bufferedStream = BufferedInputStream(inputStream)

				bufferedStream.use { stream ->
					val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
					val data = samples.map { model -> Pair(model.uuid128Bits, model) }
					_serviceUUIDCache.putAll(data)
				}
			}
			Log.d(READER_LOGGER, "READ SERVICES $time millis-seconds")
		} catch (e: FileNotFoundException) {
			Log.d(READER_LOGGER, "FILE_NOT_FOUND")
		} catch (e: SerializationException) {
			Log.d(READER_LOGGER, "JSON SERIALIZATION EXCEPTION")
		}
	}


	suspend private fun loadCharacteristicsFromFile() = withContext(Dispatchers.IO) {
		try {

			val time = measureDuration {
				val inputStream = assetsManager.open("ble_characteristics_uuids.json")
				val bufferedInputStream = BufferedInputStream(inputStream)

				bufferedInputStream.use { stream ->
					val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
					val data = samples.map { model -> Pair(model.uuid128Bits, model) }
					_characteristicsUUIDCache.putAll(data)
				}
			}
			Log.d(READER_LOGGER, "READ CHARACTERISTICS $time millis-seconds")

		} catch (e: FileNotFoundException) {
			Log.d(READER_LOGGER, "FILE_NOT_FOUND")
		} catch (e: SerializationException) {
			Log.d(READER_LOGGER, "JSON SERIALIZATION EXCEPTION")
		}
	}

	suspend fun findServiceNameForUUID(uuid: UUID): SampleBLEUUIDModel? {
		// loaded flag ensures we are loading it only once
		if (_serviceUUIDCache.isEmpty()) loadServiceUUIDFromFile()
		return _serviceUUIDCache.getOrDefault(uuid, null)
	}

	suspend fun findCharacteristicsNameForUUID(uuid: UUID): SampleBLEUUIDModel? {
		// loaded flag ensures we are loading it only once
		if (_characteristicsUUIDCache.isEmpty()) loadCharacteristicsFromFile()
		return _characteristicsUUIDCache.getOrDefault(uuid, null)
	}

}