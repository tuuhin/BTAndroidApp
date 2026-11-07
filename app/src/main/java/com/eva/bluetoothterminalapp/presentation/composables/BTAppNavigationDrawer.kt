package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.util.SharedElementTransitionKeys
import com.eva.bluetoothterminalapp.presentation.util.sharedBoundsWrapper
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTAppNavigationDrawer(
	modifier: Modifier = Modifier,
	onNavigateToClassicServer: () -> Unit = {},
	onNavigateToFeedBackRoute: () -> Unit = {},
	onNavigateToSettingsRoute: () -> Unit = {},
	onNavigateToBLEServer: () -> Unit = {},
	drawerShape: Shape = DrawerDefaults.shape,
	drawerContainerColor: Color = DrawerDefaults.modalContainerColor,
	scrollState: ScrollState = rememberScrollState(),
	contentPadding: PaddingValues = PaddingValues(all = 4.dp),
) {
	ModalDrawerSheet(
		drawerShape = drawerShape,
		drawerContainerColor = drawerContainerColor,
		drawerContentColor = contentColorFor(backgroundColor = drawerContainerColor),
		modifier = modifier,
	) {
		Column(
			modifier = Modifier
				.padding(contentPadding)
				.verticalScroll(scrollState),
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {
			AppIconWithTitle()
			HorizontalDivider(
				color = MaterialTheme.colorScheme.outlineVariant,
				modifier = Modifier.padding(vertical = 4.dp)
			)
			Text(
				text = stringResource(R.string.drawer_option_servers),
				style = MaterialTheme.typography.labelLargeEmphasized,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
			)
			NavigationDrawerItem(
				label = {
					Text(
						text = stringResource(R.string.bt_server_route),
						style = MaterialTheme.typography.titleMediumEmphasized
					)
				},
				selected = false,
				onClick = onNavigateToClassicServer,
				colors = NavigationDrawerItemDefaults.colors(
					unselectedTextColor = MaterialTheme.colorScheme.primary,
					unselectedIconColor = MaterialTheme.colorScheme.secondary
				),
				icon = {
					Icon(
						painter = painterResource(R.drawable.ic_server),
						contentDescription = null
					)
				},
				modifier = Modifier.sharedBoundsWrapper(SharedElementTransitionKeys.CLASSIC_SERVER_ITEM_TO_SERVER)
			)
			NavigationDrawerItem(
				label = {
					Text(
						text = stringResource(R.string.drawer_option_ble_server),
						style = MaterialTheme.typography.titleMediumEmphasized
					)
				},
				selected = false,
				onClick = onNavigateToBLEServer,
				colors = NavigationDrawerItemDefaults.colors(
					unselectedTextColor = MaterialTheme.colorScheme.primary,
					unselectedIconColor = MaterialTheme.colorScheme.secondary
				),
				icon = {
					Icon(
						painter = painterResource(R.drawable.ic_server_2),
						contentDescription = null
					)
				},
				modifier = Modifier.sharedBoundsWrapper(SharedElementTransitionKeys.BLE_SERVER_ITEM_TO_SERVER)
			)
			HorizontalDivider(
				color = MaterialTheme.colorScheme.outlineVariant,
				modifier = Modifier.padding(vertical = 4.dp)
			)
			Text(
				text = stringResource(R.string.drawer_option_others),
				color = MaterialTheme.colorScheme.onSurface,
				style = MaterialTheme.typography.labelLargeEmphasized,
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
			)
			NavigationDrawerItem(
				label = {
					Text(
						text = stringResource(id = R.string.settings_route_title),
						style = MaterialTheme.typography.titleMediumEmphasized
					)
				},
				selected = false,
				onClick = onNavigateToSettingsRoute,
				colors = NavigationDrawerItemDefaults.colors(
					unselectedTextColor = MaterialTheme.colorScheme.primary,
					unselectedIconColor = MaterialTheme.colorScheme.secondary
				),
				icon = {
					Icon(
						painter = painterResource(R.drawable.ic_app_settings),
						contentDescription = stringResource(id = R.string.settings_route_title)
					)
				},
				modifier = Modifier.sharedBoundsWrapper(SharedElementTransitionKeys.SETTINGS_ITEM_TO_SETTING_SCREEN)
			)
			NavigationDrawerItem(
				label = {
					Text(
						text = stringResource(id = R.string.about_route_title),
						style = MaterialTheme.typography.titleMediumEmphasized
					)
				},
				selected = false,
				onClick = onNavigateToFeedBackRoute,
				colors = NavigationDrawerItemDefaults.colors(
					unselectedTextColor = MaterialTheme.colorScheme.primary,
					unselectedIconColor = MaterialTheme.colorScheme.secondary
				),
				icon = {
					Icon(
						painter = painterResource(R.drawable.ic_info),

						contentDescription = stringResource(id = R.string.about_route_title)
					)
				},
				modifier = Modifier.sharedBoundsWrapper(SharedElementTransitionKeys.ABOUT_ITEM_TO_ABOUT_SCREEN)
			)
		}
	}
}

@Composable
private fun AppIconWithTitle(modifier: Modifier = Modifier) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(6.dp),
		modifier = modifier,
	) {

		Icon(
			painter = painterResource(id = R.drawable.ic_launcher_foreground),
			contentDescription = stringResource(id = R.string.app_name),
			modifier = Modifier.size(48.dp),
			tint = MaterialTheme.colorScheme.primary
		)

		Text(
			text = stringResource(id = R.string.app_name),
			style = MaterialTheme.typography.titleMediumEmphasized,
			color = MaterialTheme.colorScheme.onSurface,
			fontWeight = FontWeight.SemiBold
		)
	}
}

@PreviewLightDark
@Composable
private fun BTNavigationAppDrawerPreview() = BlueToothTerminalAppTheme {
	BTAppNavigationDrawer()
}
