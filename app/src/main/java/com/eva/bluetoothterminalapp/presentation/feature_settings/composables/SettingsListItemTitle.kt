package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyListScope.bTListItemTitle(
	key: Any? = null,
	contentType: Any? = null,
	content: @Composable BoxScope.() -> Unit,
) = stickyHeader(key = key, contentType = contentType) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.surface),
	) {
		CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
			ProvideTextStyle(
				value = MaterialTheme.typography.titleMedium,
			) {
				Box(
					modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
					content = content,
				)
			}
		}
	}
}