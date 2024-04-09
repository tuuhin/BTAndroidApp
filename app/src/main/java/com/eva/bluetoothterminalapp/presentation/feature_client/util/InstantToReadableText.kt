package com.eva.bluetoothterminalapp.presentation.feature_client.util

import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char

fun Instant.toReadableTimeText(): String {
	return format(
		DateTimeComponents.Format {
			hour()
			char(':')
			minute()
			char(':')
			second()
		},
	)
}