package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.data.datastore.BLESettingsDatastoreImpl
import com.eva.bluetoothterminalapp.data.datastore.BTSettingsDatastoreImpl
import com.eva.bluetoothterminalapp.domain.settings.repository.BLESettingsDataStore
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsModule = module {
	// low energy datastore
	factoryOf(::BLESettingsDatastoreImpl).bind<BLESettingsDataStore>()
	// classic settings datastore
	factoryOf(::BTSettingsDatastoreImpl).bind<BTSettingsDataSore>()

}