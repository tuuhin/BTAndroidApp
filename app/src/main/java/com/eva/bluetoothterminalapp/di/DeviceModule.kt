package com.eva.bluetoothterminalapp.di

import android.content.Context
import com.eva.bluetoothterminalapp.data.device.BatteryReaderImpl
import com.eva.bluetoothterminalapp.data.device.LightSensorReaderImpl
import com.eva.bluetoothterminalapp.domain.device.BatteryReader
import com.eva.bluetoothterminalapp.domain.device.LightSensorReader
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single


@Module
class DeviceModule {

	@Single
	fun providesBatteryReader(context: Context): BatteryReader = BatteryReaderImpl(context)

	@Single
	fun providesLightSensorReader(context: Context): LightSensorReader =
		LightSensorReaderImpl(context)
}