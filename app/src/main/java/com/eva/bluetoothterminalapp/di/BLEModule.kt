package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.data.bluetooth_le.AndroidBLEClientConnector
import com.eva.bluetoothterminalapp.data.bluetooth_le.AndroidBluetoothLEScanner
import com.eva.bluetoothterminalapp.data.samples.SampleUUIDReader
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEScanner
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothLEModule = module {

	singleOf(::SampleUUIDReader)

	factoryOf(::AndroidBluetoothLEScanner).bind<BluetoothLEScanner>()
	factoryOf(::AndroidBLEClientConnector).bind<BluetoothLEClientConnector>()
}