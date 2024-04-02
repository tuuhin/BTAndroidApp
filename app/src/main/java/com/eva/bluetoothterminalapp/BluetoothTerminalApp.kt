package com.eva.bluetoothterminalapp

import android.app.Application
import com.eva.bluetoothterminalapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BluetoothTerminalApp : Application() {

	override fun onCreate() {
		super.onCreate()

		val modules = listOf(appModule)

		startKoin {
			androidLogger()
			androidContext(this@BluetoothTerminalApp)
			modules(modules)
		}
	}
}