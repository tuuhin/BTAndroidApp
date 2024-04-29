package com.eva.bluetoothterminalapp.di

import org.koin.dsl.module

val appModule = module {
	// contains all the modules
	includes(
		bluetoothLEModule,
		bluetoothClassicModule,
		readerModule,
		viewModelModule
	)
}