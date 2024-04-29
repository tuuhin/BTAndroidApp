package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.data.samples.SampleServiceUUIDReader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val readerModule = module {
	singleOf(::SampleServiceUUIDReader)
}