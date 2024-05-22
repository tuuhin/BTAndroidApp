package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.data.datastore.BLESettingsDatastoreImpl
import com.eva.bluetoothterminalapp.domain.settings.repository.BLESettingsDataStore
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsModule = module {

	factoryOf(::BLESettingsDatastoreImpl) bind BLESettingsDataStore::class
}