package com.eva.bluetoothterminalapp.presentation.feature_connect.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun Instant.toReadableTimeText(): String {
	return toLocalDateTime(TimeZone.currentSystemDefault()).format(
		LocalDateTime.Format {
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