package com.eva.bluetoothterminalapp.data.bluetooth_le.samples

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.eva.bluetoothterminalapp.domain.bluetooth_le.samples.SampleUUIDReader
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

private const val LOGGER = "SAMPLE_SERVICE_READER"

class SampleServiceUUIDReader(
	private val context: Context
) : SampleUUIDReader {
	private val assetsManager: AssetManager
		get() = context.assets

	private val mapper: ObjectMapper
		get() = ObjectMapper(YAMLFactory())
			.registerModules(KotlinModule.Builder().build())
			.findAndRegisterModules()

	private val _cache = ConcurrentHashMap<UUID, String?>()

	private var _isLoaded = false

	private suspend fun loadFileFromAssetsAndFillCache() {
		withContext(Dispatchers.IO) {
			try {
				val samples = assetsManager
					.open("ble_service_uuids.yml")
					.use { stream ->
						mapper.readValue(stream, Array<SampleBLEUUIDModel>::class.java)
					}

				samples.forEach { sampleModel ->
					_cache[sampleModel.uuid128Bits] = sampleModel.name
				}
				_isLoaded = true
			} catch (e: FileNotFoundException) {
				Log.d(LOGGER, "FILE_NOT_FOUND")
			} catch (e: JacksonYAMLParseException) {
				Log.d(LOGGER, "JSON PARSING FAILED")
			} catch (e: JacksonException) {
				Log.d(LOGGER, "JACKSON EXCEPTION")
			}
		}
	}

	override suspend fun findNameForUUID(uuid: UUID): String? {
		// loaded flag ensures we are loading it only once
		if (!_isLoaded || _cache.isEmpty())
			loadFileFromAssetsAndFillCache()
		return _cache[uuid]
	}

}