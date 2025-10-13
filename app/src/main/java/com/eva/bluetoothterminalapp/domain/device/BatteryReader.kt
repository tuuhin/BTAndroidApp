package com.eva.bluetoothterminalapp.domain.device

import kotlinx.coroutines.flow.Flow

interface BatteryReader {

	val isBatteryCharging: Boolean

	val currentBatteryLevel: Int

	fun isBatteryChargingFlow(): Flow<Boolean>

	fun batteryLevelFlow(): Flow<Int>
}