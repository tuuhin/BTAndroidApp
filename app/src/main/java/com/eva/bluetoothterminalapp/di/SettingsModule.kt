package com.eva.bluetoothterminalapp.di

import android.content.Context
import com.eva.bluetoothterminalapp.data.datastore.BLESettingsDatastoreImpl
import com.eva.bluetoothterminalapp.data.datastore.BTSettingsDatastoreImpl
import com.eva.bluetoothterminalapp.domain.settings.repository.BLESettingsDataStore
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class SettingsModule {

	@Factory
	fun providesBLESettingsDataStore(context: Context): BLESettingsDataStore =
		BLESettingsDatastoreImpl(context)

	@Factory
	fun providesBTSettingsDatastore(context: Context): BTSettingsDataSore =
		BTSettingsDatastoreImpl(context)
}