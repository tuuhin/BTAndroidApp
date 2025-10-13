package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.data.device.BatteryReaderImpl
import com.eva.bluetoothterminalapp.domain.device.BatteryReader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val deviceModule = module {
	singleOf(::BatteryReaderImpl).bind<BatteryReader>()
}