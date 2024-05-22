package com.eva.bluetoothterminalapp.presentation.util

import androidx.annotation.StringRes
import com.eva.bluetoothterminalapp.R

enum class BluetoothTypes(@StringRes val text: Int, val tabIdx: Int) {
	CLASSIC(R.string.bluetooth_classic, 0),
	LOW_ENERGY(R.string.bluetooth_le, 1)
}