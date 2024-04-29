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
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

private const val READER_LOGGER = "SAMPLE_SERVICE_READER"


class SampleServiceUUIDReader(private val context: Context) {

	private val assetsManager: AssetManager
		get() = context.assets


	// as there are very less registered service uuid this keeping them in cache
	private val _memoryCache = ConcurrentHashMap<UUID, String?>()

	@OptIn(ExperimentalSerializationApi::class)
	private suspend fun loadFileFromAssetsAndFillCache() = withContext(Dispatchers.IO) {
		try {

			val inputStream = assetsManager.open("ble_service_uuids.json")
			val bufferedStream = BufferedInputStream(inputStream)

			bufferedStream.use { stream ->
				val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
				samples.forEach { sampleModel ->
					_memoryCache[sampleModel.uuid128Bits] = sampleModel.name
				}
			}

		} catch (e: FileNotFoundException) {
			Log.d(READER_LOGGER, "FILE_NOT_FOUND")
		} catch (e: SerializationException) {
			Log.d(READER_LOGGER, "JSON SERIALIZATION EXCEPTION")
		}
	}


	suspend fun findNameForUUID(uuid: UUID): String? {
		// loaded flag ensures we are loading it only once
		if (_memoryCache.isEmpty()) loadFileFromAssetsAndFillCache()
		return _memoryCache.getOrDefault(uuid, null)
	}

}