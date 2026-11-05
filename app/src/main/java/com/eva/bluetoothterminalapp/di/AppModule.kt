package com.eva.bluetoothterminalapp.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module


@Module(
	includes = [
		DeviceModule::class,
		BLEModule::class,
		SettingsModule::class,
		BTClassicModule::class]
)
@ComponentScan("com.eva.bluetoothterminalapp")
class AppModule


@KoinApplication(modules = [AppModule::class])
class KoinDependencyContainer