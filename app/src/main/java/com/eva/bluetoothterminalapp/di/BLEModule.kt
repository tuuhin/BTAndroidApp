package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.data.bluetooth_le.AndroidBLEClientConnector
import com.eva.bluetoothterminalapp.data.bluetooth_le.AndroidBluetoothLEScanner
import com.eva.bluetoothterminalapp.data.bluetooth_le.BLEClientGattCallback
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEScanner
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothLEModule = module {

	factoryOf(::AndroidBluetoothLEScanner) bind BluetoothLEScanner::class

	factoryOf(::BLEClientGattCallback)

	factoryOf(::AndroidBLEClientConnector) bind BluetoothLEClientConnector::class
}