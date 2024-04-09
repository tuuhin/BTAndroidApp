package com.eva.bluetoothterminalapp.presentation.navigation.args

import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel

fun BluetoothDeviceModel.toArgs() = ConnectionRouteArgs(name = name, address = address)