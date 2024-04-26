package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.data.bluetooth_le.AndroidBLEClientConnector
import com.eva.bluetoothterminalapp.data.bluetooth_le.AndroidBluetoothLEScanner
import com.eva.bluetoothterminalapp.data.bluetooth_le.samples.SampleServiceUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEScanner
import com.eva.bluetoothterminalapp.domain.bluetooth_le.samples.SampleUUIDReader
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothLEModule = module {

	singleOf(::SampleServiceUUIDReader) bind SampleUUIDReader::class

	factoryOf(::AndroidBluetoothLEScanner) bind BluetoothLEScanner::class

	factoryOf(::AndroidBLEClientConnector) bind BluetoothLEClientConnector::class
}