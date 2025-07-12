package com.eva.bluetoothterminalapp.data.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.eva.bluetoothterminalapp.data.mapper.toModel
import com.eva.bluetoothterminalapp.data.mapper.toProto
import com.eva.bluetoothterminalapp.domain.settings.enums.BLEScanPeriodTimmings
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsScanMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsSupportedLayer
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel
import com.eva.bluetoothterminalapp.domain.settings.repository.BLESettingsDataStore
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

class BLESettingsDatastoreImpl(
	private val context: Context
) : BLESettingsDataStore {

	override val settingsFlow: Flow<BLESettingsModel>
		get() = context.bleSettingsDataStore.data.map(BLEAppSettings::toModel)

	override suspend fun getSettings(): BLESettingsModel {
		return settingsFlow.first()
	}

	override suspend fun onUpdateScanPeriod(timming: BLEScanPeriodTimmings) {
		context.bleSettingsDataStore.updateData { pref ->
			pref.copy {
				scanPeriod = timming.toProto
			}
		}
	}

	override suspend fun onIsAdvertiseExtensionChanged(isAdvertiseExtensionsOnly: Boolean) {
		context.bleSettingsDataStore.updateData { pref ->
			pref.copy {
				isAdvertisingExtension = isAdvertiseExtensionsOnly
			}
		}
	}

	override suspend fun onUpdateScanMode(scanMode: BLESettingsScanMode) {
		context.bleSettingsDataStore.updateData { pref ->
			pref.copy {
				this.scanMode = scanMode.toProto
			}
		}
	}

	override suspend fun onUpdateSupportedLayer(layer: BLESettingsSupportedLayer) {
		context.bleSettingsDataStore.updateData { pref ->
			pref.copy {
				supportedLayer = layer.toProto
			}
		}
	}
}

private val Context.bleSettingsDataStore: DataStore<BLEAppSettings> by dataStore(
	fileName = DatastoreConstants.BLE_SETTINGS_FILE_NAME,
	serializer = object : Serializer<BLEAppSettings> {

		override val defaultValue: BLEAppSettings = bLEAppSettings {}

		override suspend fun readFrom(input: InputStream): BLEAppSettings {
			try {
				return BLEAppSettings.parseFrom(input)
			} catch (exception: InvalidProtocolBufferException) {
				throw CorruptionException("Cannot read .proto file", exception)
			}
		}

		override suspend fun writeTo(t: BLEAppSettings, output: OutputStream) =
			t.writeTo(output)
	}
)