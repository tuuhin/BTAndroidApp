package com.eva.bluetoothterminalapp.data.samples

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.collection.mutableScatterMapOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.FileNotFoundException
import java.util.UUID
import kotlin.time.measureTime

private const val READER_LOGGER = "SAMPLE_SERVICE_READER"

@OptIn(ExperimentalSerializationApi::class)
class SampleUUIDReader(private val context: Context) {

	private val assetsManager: AssetManager
		get() = context.assets

	private val _serviceUUIDCache = mutableScatterMapOf<UUID, SampleBLEUUIDModel>()

	private val _characteristicsUUIDCache = mutableScatterMapOf<UUID, SampleBLEUUIDModel>()

	private val _descriptorUUIDCache = mutableScatterMapOf<UUID, SampleBLEUUIDModel>()

	private suspend fun loadServiceUUIDFromFile() = withContext(Dispatchers.IO) {
		try {
			assetsManager.open("ble_service_uuids.json").use { stream ->
				val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
				val data = samples.map { model -> model.uuid128Bits to model }
					.distinctBy { it.first }
				_serviceUUIDCache.putAll(data)
			}

			Log.d(READER_LOGGER, "LOADED SERVICES SAMPLES")
		} catch (e: CancellationException) {
			throw e
		} catch (_: FileNotFoundException) {
			Log.d(READER_LOGGER, "FILE_NOT_FOUND")
		} catch (_: SerializationException) {
			Log.d(READER_LOGGER, "JSON SERIALIZATION EXCEPTION")
		}
	}

	private suspend fun loadCharacteristicsFromFile() = withContext(Dispatchers.IO) {
		try {
			assetsManager.open("ble_characteristics_uuids.json")
				.use { stream ->
					val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
					val data = samples.map { model -> model.uuid128Bits to model }
						.distinctBy { it.first }
					_characteristicsUUIDCache.putAll(data)
				}
			Log.d(READER_LOGGER, "LOADED SAMPLES CHARACTERISTICS")
		} catch (e: CancellationException) {
			throw e
		} catch (_: FileNotFoundException) {
			Log.d(READER_LOGGER, "FILE_NOT_FOUND")
		} catch (_: SerializationException) {
			Log.d(READER_LOGGER, "JSON SERIALIZATION EXCEPTION")
		}
	}

	private suspend fun loadDescriptorsFromFile() = withContext(Dispatchers.IO) {
		try {
			assetsManager.open("ble_descriptor_uuids.json")
				.use { inputStream ->
					val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(inputStream)
					val data = samples.map { model -> model.uuid128Bits to model }
						.distinctBy { it.first }
					_descriptorUUIDCache.putAll(data)
				}
			Log.d(READER_LOGGER, "LOADED DESCRIPTORS")
		} catch (e: CancellationException) {
			throw e
		} catch (_: FileNotFoundException) {
			Log.d(READER_LOGGER, "FILE_NOT_FOUND")
		} catch (_: SerializationException) {
			Log.d(READER_LOGGER, "JSON SERIALIZATION EXCEPTION")
		}
	}

	/**
	 * Loads all the files and save them on the hashmap
	 * This should be used when you bulk check the uuids
	 * this loads all the cache
	 */
	suspend fun loadFromFiles() {
		coroutineScope {
			val loaders = buildList {
				if (_serviceUUIDCache.isEmpty())
					add(async(Dispatchers.IO) { loadServiceUUIDFromFile() })
				if (_characteristicsUUIDCache.isEmpty())
					add(async(Dispatchers.IO) { loadCharacteristicsFromFile() })
				if (_descriptorUUIDCache.isEmpty())
					add(async(Dispatchers.IO) { loadDescriptorsFromFile() })
			}
			if (loaders.isNotEmpty()) {
				val time = measureTime { loaders.awaitAll() }
				Log.d(READER_LOGGER, "TIME TO LOAD DATA :$time")
			}
		}
	}

	suspend fun findServiceNameForUUID(uuid: UUID): SampleBLEUUIDModel? {
		// if the files are not loaded then services will be loaded in the memory
		if (_serviceUUIDCache.isEmpty()) loadServiceUUIDFromFile()
		return if (!_serviceUUIDCache.isEmpty()) null
		else _serviceUUIDCache[uuid]
	}


	suspend fun findDescriptorNameForUUID(uuid: UUID): SampleBLEUUIDModel? {
		// if the files are not loaded then services will be loaded in the memory
		if (_descriptorUUIDCache.isEmpty()) loadDescriptorsFromFile()
		return if (!_descriptorUUIDCache.contains(uuid)) null
		else _descriptorUUIDCache[uuid]
	}


	suspend fun findCharacteristicsNameForUUID(uuid: UUID): SampleBLEUUIDModel? {
		// if the file is not loaded
		if (_characteristicsUUIDCache.isEmpty()) loadCharacteristicsFromFile()
		return if (!_characteristicsUUIDCache.contains(uuid)) null
		else _characteristicsUUIDCache[uuid]
	}
}