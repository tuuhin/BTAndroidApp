package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.data.bluetooth.AndroidBluetoothScanner
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothScanner
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

	singleOf(::AndroidBluetoothScanner) bind BluetoothScanner::class
}