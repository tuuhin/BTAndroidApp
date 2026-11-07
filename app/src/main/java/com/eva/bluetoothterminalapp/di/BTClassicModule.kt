package com.eva.bluetoothterminalapp.di

import android.content.Context
import com.eva.bluetoothterminalapp.data.bluetooth.AndroidBTClientConnector
import com.eva.bluetoothterminalapp.data.bluetooth.AndroidBTServerConnector
import com.eva.bluetoothterminalapp.data.bluetooth.AndroidBluetoothScanner
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothScanner
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothServerConnector
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class BTClassicModule {

	@Factory
	fun btScanner(context: Context): BluetoothScanner = AndroidBluetoothScanner(context)

	@Factory
	fun btClientConnector(
		context: Context,
		btSettings: BTSettingsDataSore
	): BluetoothClientConnector = AndroidBTClientConnector(context, btSettings)

	@Factory
	fun btServerConnector(
		context: Context,
		btSettings: BTSettingsDataSore
	): BluetoothServerConnector = AndroidBTServerConnector(context, btSettings)
}
