package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BTAppNavigationDrawer(
	modifier: Modifier = Modifier,
	drawerShape: Shape = DrawerDefaults.shape,
	drawerContainerColor: Color = DrawerDefaults.modalContainerColor,
	onNavigateToClassicServer: () -> Unit = {},
	onNavigateToFeedBackRoute: () -> Unit = {},
	onNavigateToSettingsRoute: () -> Unit = {},
) {
	ModalDrawerSheet(
		drawerShape = drawerShape,
		drawerContainerColor = drawerContainerColor,
		drawerContentColor = contentColorFor(backgroundColor = drawerContainerColor),
		modifier = modifier,
	) {
		Column(
			modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {
			AppIconWithTitle()
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
			Text(
				text = stringResource(R.string.drawer_option_servers),
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
			)
			NavigationDrawerItem(
				label = { Text(text = stringResource(R.string.drawer_option_classic_server)) },
				selected = false,
				onClick = onNavigateToClassicServer,
				icon = {
					Icon(
						painter = painterResource(R.drawable.ic_server),
						contentDescription = "Server Icon"
					)
				}
			)
			HorizontalDivider(
				color = MaterialTheme.colorScheme.outlineVariant,
				modifier = Modifier.padding(vertical = 2.dp)
			)
			Text(
				text = stringResource(R.string.drawer_option_others),
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				style = MaterialTheme.typography.bodyMedium,
				modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
			)
			NavigationDrawerItem(
				label = { Text(text = stringResource(id = R.string.settings_route_title)) },
				selected = false,
				onClick = onNavigateToSettingsRoute,
				icon = {
					Icon(
						imageVector = Icons.Default.Settings,
						contentDescription = stringResource(id = R.string.settings_route_title)
					)
				},
				modifier = Modifier.sharedBoundsWrapper(SharedElementTransitionKeys.SETTINGS_ITEM_TO_SETTING_SCREEN)
			)
			NavigationDrawerItem(
				label = { Text(text = stringResource(id = R.string.about_route_title)) },
				selected = false,
				onClick = onNavigateToFeedBackRoute,
				icon = {
					Icon(
						imageVector = Icons.Outlined.Info,
						contentDescription = stringResource(id = R.string.about_route_title)
					)
				},
				modifier = Modifier.sharedBoundsWrapper(SharedElementTransitionKeys.ABOUT_ITEM_TO_ABOUT_SCREEN)
			)
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
		}
	}
}

@Composable
fun AppIconWithTitle(modifier: Modifier = Modifier) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(2.dp),
		modifier = modifier,
	) {
		Image(
			painter = painterResource(id = R.drawable.ic_launcher_foreground),
			contentDescription = stringResource(id = R.string.app_name),
			modifier = Modifier.size(56.dp),
			colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
		)
		Text(
			text = stringResource(id = R.string.app_name),
			style = MaterialTheme.typography.titleMedium,
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
