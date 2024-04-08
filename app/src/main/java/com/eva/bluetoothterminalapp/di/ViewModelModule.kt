package com.eva.bluetoothterminalapp.di

import com.eva.bluetoothterminalapp.presentation.feature_client.BTClientViewModel
import com.eva.bluetoothterminalapp.presentation.feature_devices.BTDeviceViewmodel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

	// devices viewmodel
	viewModelOf(::BTDeviceViewmodel)

	// client viewmodel
	viewModelOf(::BTClientViewModel)


}