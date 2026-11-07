package com.eva.bluetoothterminalapp.di

import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module


@Module(
	includes = [
		DeviceModule::class,
		BLEModule::class,
		SettingsModule::class,
		PresentationModule::class,
		BTClassicModule::class
	]
)
class AppModule


@KoinApplication(modules = [AppModule::class])
class KoinDependencyContainer