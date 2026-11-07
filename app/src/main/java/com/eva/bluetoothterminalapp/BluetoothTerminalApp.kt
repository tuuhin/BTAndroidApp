package com.eva.bluetoothterminalapp

import android.app.Application
import com.eva.bluetoothterminalapp.di.KoinDependencyContainer
import com.eva.bluetoothterminalapp.domain.utils.NotificationChannelRegistrar
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.plugin.module.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class BluetoothTerminalApp : Application(), KoinStartup {

	private val _notificationRegistrar by inject<NotificationChannelRegistrar>()

	override fun onCreate() {
		super.onCreate()
		_notificationRegistrar.registerChannels()
	}

	override fun onKoinStartup(): KoinConfiguration = koinConfiguration<KoinDependencyContainer> {
		androidLogger()
		androidContext(this@BluetoothTerminalApp)
	}
}