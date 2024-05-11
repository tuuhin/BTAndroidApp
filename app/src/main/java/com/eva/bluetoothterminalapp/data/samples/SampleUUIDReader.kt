package com.eva.bluetoothterminalapp.data.samples

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
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

	private val _descriptorUUIDCache = hashMapOf<UUID, SampleBLEUUIDModel>()

	private suspend fun loadServiceUUIDFromFile() = withContext(Dispatchers.IO) {
		try {
			val inputStream = assetsManager.open("ble_service_uuids.json")
			val bufferedStream = BufferedInputStream(inputStream)

			bufferedStream.use { stream ->
				val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
				val data = samples.map { model -> model.uuid128Bits to model }
				_serviceUUIDCache.putAll(data)
			}
			Log.d(READER_LOGGER, "LOADED SERVICES SAMPLES")
		} catch (e: FileNotFoundException) {
			Log.d(READER_LOGGER, "FILE_NOT_FOUND")
		} catch (e: SerializationException) {
			Log.d(READER_LOGGER, "JSON SERIALIZATION EXCEPTION")
		}
	}


	suspend private fun loadCharacteristicsFromFile() = withContext(Dispatchers.IO) {
		try {
			val inputStream = assetsManager.open("ble_characteristics_uuids.json")
			val bufferedInputStream = BufferedInputStream(inputStream)

			bufferedInputStream.use { stream ->
				val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
				val data = samples.map { model -> model.uuid128Bits to model }
				_characteristicsUUIDCache.putAll(data)
			}
			Log.d(READER_LOGGER, "LOADED SAMPLES CHARACERISTICS")
		} catch (e: FileNotFoundException) {
			Log.d(READER_LOGGER, "FILE_NOT_FOUND")
		} catch (e: SerializationException) {
			Log.d(READER_LOGGER, "JSON SERIALIZATION EXCEPTION")
		}
	}

	suspend private fun loadDescriptorsFromFile() = withContext(Dispatchers.IO) {
		try {

			val inputStream = assetsManager.open("ble_descriptor_uuids.json")
			val bufferedInputStream = BufferedInputStream(inputStream)

			bufferedInputStream.use { stream ->
				val samples = Json.decodeFromStream<List<SampleBLEUUIDModel>>(stream)
				val data = samples.map { model -> model.uuid128Bits to model }
				_descriptorUUIDCache.putAll(data)
			}
			Log.d(READER_LOGGER, "LOADED DESCRIPTORS")
		} catch (e: FileNotFoundException) {
			Log.d(READER_LOGGER, "FILE_NOT_FOUND")
		} catch (e: SerializationException) {
			Log.d(READER_LOGGER, "JSON SERIALIZATION EXCEPTION")
		}
	}

	/**
	 * Must call this funtion in order to continue
	 */
	suspend fun loadFromFiles() = withContext(Dispatchers.IO) {
		val loadServices = async {
			if (_serviceUUIDCache.isEmpty()) loadServiceUUIDFromFile()
		}
		val loadCharactetistics = async {
			if (_characteristicsUUIDCache.isEmpty()) loadCharacteristicsFromFile()
		}
		val loadDescriptors = async {
			if (_descriptorUUIDCache.isEmpty()) loadDescriptorsFromFile()
		}
		// all the loading will occur together
		awaitAll(loadDescriptors, loadServices, loadCharactetistics)
	}

	fun findServiceNameForUUID(uuid: UUID): SampleBLEUUIDModel? =
		_serviceUUIDCache.getOrDefault(uuid, null)


	fun findDescriptorNameForUUID(uuid: UUID): SampleBLEUUIDModel? =
		_descriptorUUIDCache.getOrDefault(uuid, null)


	fun findCharacteristicsNameForUUID(uuid: UUID): SampleBLEUUIDModel? =
		_characteristicsUUIDCache.getOrDefault(uuid, null)

}