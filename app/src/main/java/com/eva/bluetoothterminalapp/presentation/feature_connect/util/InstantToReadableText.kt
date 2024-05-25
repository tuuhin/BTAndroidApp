package com.eva.bluetoothterminalapp.presentation.feature_connect.util

import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

fun Instant.toReadableTimeText(): String {
	return format(
		DateTimeComponents.Format {
			amPmHour(padding = Padding.ZERO)
			char(':')
			minute()
			char(':')
			second()
			char(' ')
			amPmMarker(am = "AM", pm = "PM")
		},
	)
}