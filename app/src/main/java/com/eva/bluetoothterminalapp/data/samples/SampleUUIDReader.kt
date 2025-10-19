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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.FileNotFoundException
import java.util.UUID
import kotlin.time.measureTime

private const val TAG = "SAMPLE_SERVICE_READER"

@OptIn(ExperimentalSerializationApi::class)
class SampleUUIDReader(private val context: Context) {

	private val assetsManager: AssetManager
		get() = context.assets

	private val _serviceUUIDCache = mutableScatterMapOf<UUID, SampleBLEUUIDModel>()
	private val _characteristicsUUIDCache = mutableScatterMapOf<UUID, SampleBLEUUIDModel>()
	private val _descriptorUUIDCache = mutableScatterMapOf<UUID, SampleBLEUUIDModel>()

	private val _serviceMutex = Mutex()
	private val _characteristicsMutex = Mutex()
	private val _descriptorMutex = Mutex()

	private suspend fun loadServiceUUIDFromFile() = _serviceMutex.withLock {
		// if cache is already present no need to load again
		if (_serviceUUIDCache.isNotEmpty()) return@withLock

		withContext(Dispatchers.IO) {
			try {
				assetsManager.open("ble_service_uuids.json").use { stream ->
					val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
					samples
						.distinctBy { item -> item.id }
						.forEach { model -> _serviceUUIDCache[model.uuid128Bits] = model }
				}

				Log.d(TAG, "LOADED SERVICES SAMPLES")
			} catch (e: CancellationException) {
				throw e
			} catch (_: FileNotFoundException) {
				Log.d(TAG, "FILE_NOT_FOUND")
			} catch (_: SerializationException) {
				Log.d(TAG, "JSON SERIALIZATION EXCEPTION")
			}
		}
	}


	private suspend fun loadCharacteristicsFromFile() = _characteristicsMutex.withLock {
		// if characteristics are already in memory then skip
		if (_characteristicsUUIDCache.isNotEmpty()) return@withLock

		withContext(Dispatchers.IO) {
			try {
				assetsManager.open("ble_characteristics_uuids.json")
					.use { stream ->
						val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
						samples
							.distinctBy { item -> item.id }
							.forEach { model ->
								_characteristicsUUIDCache[model.uuid128Bits] = model
							}
					}
				Log.d(TAG, "LOADED SAMPLES CHARACTERISTICS")
			} catch (e: CancellationException) {
				throw e
			} catch (_: FileNotFoundException) {
				Log.d(TAG, "FILE_NOT_FOUND")
			} catch (_: SerializationException) {
				Log.d(TAG, "JSON SERIALIZATION EXCEPTION")
			}
		}
	}


	private suspend fun loadDescriptorsFromFile() = _descriptorMutex.withLock {
		// if descriptor are loaded then cancel loading
		if (_descriptorUUIDCache.isNotEmpty()) return@withLock

		withContext(Dispatchers.IO) {
			try {
				assetsManager.open("ble_descriptor_uuids.json").use { stream ->
					val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
					samples
						.distinctBy { item -> item.id }
						.forEach { model ->
							_descriptorUUIDCache[model.uuid128Bits] = model
						}
				}
				Log.d(TAG, "LOADED DESCRIPTORS")
			} catch (e: CancellationException) {
				throw e
			} catch (_: FileNotFoundException) {
				Log.d(TAG, "FILE_NOT_FOUND")
			} catch (_: SerializationException) {
				Log.d(TAG, "JSON SERIALIZATION EXCEPTION")
			}
		}
	}


	/**
	 * Loads all the files and save them on the hashmap
	 * This should be used when you bulk check the uuids
	 * this loads all the cache
	 */
	suspend fun loadFromFiles() = coroutineScope {
		val loaders = buildList {
			add(async { loadServiceUUIDFromFile() })
			add(async { loadCharacteristicsFromFile() })
			add(async { loadDescriptorsFromFile() })
		}
		val time = measureTime { loaders.awaitAll() }
		Log.d(TAG, "TIME TO LOAD DATA :$time")
		Unit
	}


	suspend fun findServiceNameForUUID(uuid: UUID): SampleBLEUUIDModel? {
		if (_serviceUUIDCache.isEmpty()) loadServiceUUIDFromFile()
		return _serviceUUIDCache[uuid]
	}


	suspend fun findDescriptorNameForUUID(uuid: UUID): SampleBLEUUIDModel? {
		if (_descriptorUUIDCache.isEmpty()) loadDescriptorsFromFile()
		return _descriptorUUIDCache[uuid]
	}


	suspend fun findCharacteristicsNameForUUID(uuid: UUID): SampleBLEUUIDModel? {
		if (_characteristicsUUIDCache.isEmpty()) loadCharacteristicsFromFile()
		return _characteristicsUUIDCache[uuid]
	}

	fun clearCache() {
		Log.d(TAG, "CLEARING CACHE")
		_descriptorUUIDCache.clear()
		_characteristicsUUIDCache.clear()
		_descriptorUUIDCache.clear()
	}
}