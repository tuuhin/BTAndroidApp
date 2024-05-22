package com.eva.bluetoothterminalapp.domain.settings.enums

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

enum class BLEScanPeriodTimmings(val duration: Duration) {
	TWELVE_SECONDS(12.seconds),
	FOURTY_FIVE_SECONDS(45.seconds),
	ONE_MINUTE(1.minutes),
	THREE_MINUTE(3.minutes),
	FIVE_MINUTES(5.minutes)
}