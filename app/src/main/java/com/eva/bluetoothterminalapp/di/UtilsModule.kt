package com.eva.bluetoothterminalapp.di

import android.content.Context
import com.eva.bluetoothterminalapp.data.utils.NotificationChannelRegistrarImpl
import com.eva.bluetoothterminalapp.data.utils.PlatformCoroutineDispatcherImpl
import com.eva.bluetoothterminalapp.domain.utils.NotificationChannelRegistrar
import com.eva.bluetoothterminalapp.domain.utils.PlatformCoroutineDispatchers
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class UtilsModule {

	@Single
	fun providesNotificationRegistrar(context: Context): NotificationChannelRegistrar =
		NotificationChannelRegistrarImpl(context)

	@Factory
	fun providesDispatchers(): PlatformCoroutineDispatchers = PlatformCoroutineDispatcherImpl()
}