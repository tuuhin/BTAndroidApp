package com.eva.bluetoothterminalapp

import android.app.Application
import com.eva.bluetoothterminalapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class BluetoothTerminalApp : Application(), KoinStartup {

	override fun onKoinStartup(): KoinConfiguration = koinConfiguration {
		androidLogger()
		androidContext(this@BluetoothTerminalApp)
		modules(appModule)
	}
}