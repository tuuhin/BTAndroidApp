package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTDevicesTopBarMenu(
	onStartDiscovery: () -> Unit,
	modifier: Modifier = Modifier,
	isBTEnabled: Boolean = true,
) {

	val context = LocalContext.current

	var isMenuExpanded by rememberSaveable { mutableStateOf(false) }
	var menuOffset by remember { mutableStateOf(DpOffset.Zero) }

	Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
		TooltipBox(
			positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Below),
			tooltip = {
				PlainTooltip(
					modifier = Modifier.padding(4.dp),
					shape = MaterialTheme.shapes.medium
				) {
					Text(
						text = stringResource(id = R.string.settings_tooltip_text),
						style = MaterialTheme.typography.labelMedium
					)
				}
			},
			state = rememberTooltipState()
		) {
			FilledIconButton(
				onClick = { isMenuExpanded = true },
				colors = IconButtonDefaults.iconButtonColors(
					containerColor = MaterialTheme.colorScheme.secondary,
					contentColor = MaterialTheme.colorScheme.onSecondary,
				),
				modifier = Modifier
					.minimumInteractiveComponentSize()
					.size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
				shapes = IconButtonDefaults.shapes(
					shape = IconButtonDefaults.smallRoundShape,
					pressedShape = IconButtonDefaults.smallPressedShape,
				),
			) {
				Icon(
					imageVector = AppIconVectors.moreVert,
					contentDescription = stringResource(id = R.string.menu_option_more),
				)
			}
		}
		DropdownMenu(
			expanded = isMenuExpanded,
			offset = menuOffset,
			onDismissRequest = { isMenuExpanded = false },
			shape = MaterialTheme.shapes.extraLarge
		) {
			DropdownMenuItem(
				text = { Text(text = stringResource(id = R.string.permission_settings)) },
				onClick = {
					val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
						data = Uri.fromParts("package", context.packageName, null)
					}
					context.startActivity(intent)
				},
				leadingIcon = {
					Icon(
						painter = painterResource(id = R.drawable.ic_bl_settings),
						contentDescription = stringResource(id = R.string.permission_settings)
					)
				},
				colors = MenuDefaults
					.itemColors(leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer)
			)
			DropdownMenuItem(
				text = { Text(text = stringResource(id = R.string.bluetooth_settings)) },
				onClick = {
					try {
						val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
						context.startActivity(intent)
					} catch (_: ActivityNotFoundException) {

					}
				},
				leadingIcon = {
					Icon(
						painter = painterResource(id = R.drawable.ic_bluetooth_settings),
						contentDescription = stringResource(id = R.string.bluetooth_settings)
					)
				},
				colors = MenuDefaults
					.itemColors(leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer)
			)
			DropdownMenuItem(
				text = { Text(text = stringResource(id = R.string.location_settings)) },
				onClick = {
					try {
						val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
						context.startActivity(intent)
					} catch (_: ActivityNotFoundException) {

					}
				},
				leadingIcon = {
					Icon(
						painter = painterResource(id = R.drawable.ic_location_pin),
						contentDescription = stringResource(id = R.string.location_settings)
					)
				},
				colors = MenuDefaults
					.itemColors(leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer)
			)
			DropdownMenuItem(
				text = { Text(text = stringResource(id = R.string.allow_discover)) },
				onClick = onStartDiscovery,
				enabled = isBTEnabled,
				leadingIcon = {
					Icon(
						painter = painterResource(id = R.drawable.ic_radar),
						contentDescription = stringResource(id = R.string.allow_discover)
					)
				},
				colors = MenuDefaults
					.itemColors(leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer)
			)
		}
	}
}
