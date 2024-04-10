package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.data.bluetooth.AndroidBTClientConnector
import com.eva.bluetoothterminalapp.data.bluetooth.AndroidBTServerConnector
import com.eva.bluetoothterminalapp.data.bluetooth.AndroidBluetoothScanner
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothScanner
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothServerConnector
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

	singleOf(::AndroidBluetoothScanner) bind BluetoothScanner::class

	factoryOf(::AndroidBTClientConnector) bind BluetoothClientConnector::class

	factoryOf(::AndroidBTServerConnector) bind BluetoothServerConnector::class

}