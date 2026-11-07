package com.eva.bluetoothterminalapp.di

import android.content.Context
import com.eva.bluetoothterminalapp.data.ble_server.AndroidBLEServerConnector
import com.eva.bluetoothterminalapp.data.bluetooth_le.AndroidBLEClientConnector
import com.eva.bluetoothterminalapp.data.bluetooth_le.AndroidBluetoothLEScanner
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BLEServerConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEScanner
import com.eva.bluetoothterminalapp.domain.device.BatteryReader
import com.eva.bluetoothterminalapp.domain.device.LightSensorReader
import com.eva.bluetoothterminalapp.domain.settings.repository.BLESettingsDataStore
import com.eva.bluetoothterminalapp.domain.utils.PlatformCoroutineDispatchers
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class BLEModule {

	@Single
	fun sampleUUIDReader(context: Context): SampleUUIDReader = SampleUUIDReader(context)

	@Factory
	fun bluetoothLEScanner(
		context: Context,
		bleSettings: BLESettingsDataStore,
		dispatchers: PlatformCoroutineDispatchers,
	): BluetoothLEScanner = AndroidBluetoothLEScanner(context, bleSettings, dispatchers)

	@Factory
	fun bleClientConnector(
		context: Context,
		reader: SampleUUIDReader,
		dispatchers: PlatformCoroutineDispatchers,
	): BluetoothLEClientConnector = AndroidBLEClientConnector(context, reader, dispatchers)

	@Factory
	fun bleServerConnector(
		context: Context,
		batteryReader: BatteryReader,
		lightSensorReader: LightSensorReader,
		uuidReader: SampleUUIDReader,
		dispatchers: PlatformCoroutineDispatchers,
	): BLEServerConnector = AndroidBLEServerConnector(
		context,
		batteryReader,
		lightSensorReader,
		uuidReader,
		dispatchers
	)
}
