package com.eva.bluetoothterminalapp.presentation.navigation.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BluetoothDeviceArgs(
	val address: String,
	val name: String? = null,
) : Parcelable
