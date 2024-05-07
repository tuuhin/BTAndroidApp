package com.eva.bluetoothterminalapp.presentation.navigation.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class BluetoothClientConnectArgs(
	val address: String,
	val uuid: UUID,
) : Parcelable
