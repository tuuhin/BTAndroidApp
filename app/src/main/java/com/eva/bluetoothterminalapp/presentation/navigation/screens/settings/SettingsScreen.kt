package com.eva.bluetoothterminalapp.presentation.navigation.screens.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.dropUnlessResumed
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_settings.AppSettingsRoute
import com.eva.bluetoothterminalapp.presentation.navigation.config.RouteAnimation
import com.eva.bluetoothterminalapp.presentation.navigation.config.Routes
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination(
	route = Routes.SETTINGS_ROUTE,
	style = RouteAnimation::class
)
@Composable
fun SettingsScreen(
	navigator: DestinationsNavigator,
	modifier: Modifier = Modifier
) {
	AppSettingsRoute(
		modifier = modifier,
		navigation = {
			val onBackPress = dropUnlessResumed(block = navigator::popBackStack)
			IconButton(onClick = onBackPress) {
				Icon(
					imageVector = Icons.AutoMirrored.Default.ArrowBack,
					contentDescription = stringResource(id = R.string.back_arrow)
				)
			}
		},
	)
}