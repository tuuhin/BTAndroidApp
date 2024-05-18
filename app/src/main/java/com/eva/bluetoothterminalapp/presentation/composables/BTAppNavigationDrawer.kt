package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTAppNavigationDrawer(
	modifier: Modifier = Modifier,
	drawerShape: Shape = DrawerDefaults.shape,
	drawerContainerColor: Color = DrawerDefaults.containerColor,
	drawerTonalElevation: Dp = 4.dp,
	onNavigateToClassicServer: () -> Unit = {},
	onNavigateToFeedBackRoute: () -> Unit = {},
	onNavigateToSettingsRoute: () -> Unit = {},
) {
	ModalDrawerSheet(
		modifier = modifier,
		drawerTonalElevation = drawerTonalElevation,
		drawerShape = drawerShape,
		drawerContentColor = contentColorFor(backgroundColor = drawerContainerColor),
		drawerContainerColor = drawerContainerColor
	) {
		Column(
			modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(2.dp)
			) {
				Image(
					painter = painterResource(id = R.drawable.ic_launcher_foreground),
					contentDescription = stringResource(id = R.string.app_name),
					modifier = Modifier.size(56.dp)
				)
				Text(
					text = stringResource(id = R.string.app_name),
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.SemiBold
				)
			}
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
			Text(
				text = "Servers",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
			)
			NavigationDrawerItem(
				label = { Text(text = "Classic Server") },
				selected = false,
				onClick = onNavigateToClassicServer,
				icon = { Icon(imageVector = Icons.Default.Bluetooth, contentDescription = null) }
			)
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
			Text(
				text = "Others",
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
				}
			)
			NavigationDrawerItem(
				label = { Text(text = stringResource(id = R.string.information_route_title)) },
				selected = false,
				onClick = onNavigateToFeedBackRoute,
				icon = {
					Icon(
						imageVector = Icons.Outlined.Info,
						contentDescription = stringResource(id = R.string.information_route_title)
					)
				}
			)
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
		}
	}
}

@PreviewLightDark
@Composable
private fun BTNavigationAppDrawerPreview() = BlueToothTerminalAppTheme {
	BTAppNavigationDrawer()
}
