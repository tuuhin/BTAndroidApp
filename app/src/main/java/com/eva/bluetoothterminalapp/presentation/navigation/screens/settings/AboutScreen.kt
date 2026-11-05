package com.eva.bluetoothterminalapp.presentation.navigation.screens.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_settings.AboutRoute
import com.eva.bluetoothterminalapp.presentation.navigation.NavDestinationBuilder
import com.eva.bluetoothterminalapp.presentation.navigation.NavRoutes
import org.koin.core.annotation.Single

@Single(binds = [NavDestinationBuilder::class])
class AboutScreen : NavDestinationBuilder {

	override fun EntryProviderScope<NavKey>.navEntry(backStack: NavBackStack<NavKey>) =
		entry<NavRoutes.AboutRoute> {
			AboutRoute(
				navigation = {
					val onBackPress = dropUnlessResumed { backStack.removeLastOrNull() }
					IconButton(onClick = onBackPress) {
						Icon(
							imageVector = Icons.AutoMirrored.Default.ArrowBack,
							contentDescription = stringResource(id = R.string.back_arrow)
						)
					}
				},
			)
		}
}

