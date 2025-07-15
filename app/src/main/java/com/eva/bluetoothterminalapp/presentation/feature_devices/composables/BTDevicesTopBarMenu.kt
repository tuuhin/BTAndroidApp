package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTDevicesTopBarMenu(
	hasDiscoverPermission: Boolean,
	onStartDiscovery: () -> Unit,
	modifier: Modifier = Modifier
) {

	val context = LocalContext.current

	var isMenuExpanded by remember { mutableStateOf(false) }
	var menuOffset by remember { mutableStateOf(DpOffset.Zero) }


	Box(modifier = modifier) {
		TooltipBox(
			positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
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
			IconButton(onClick = { isMenuExpanded = true }) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = stringResource(id = R.string.menu_option_more),
				)
			}
		}
		DropdownMenu(
			expanded = isMenuExpanded,
			onDismissRequest = { isMenuExpanded = false },
			offset = menuOffset,
			shape = MaterialTheme.shapes.medium,
			tonalElevation = 2.dp
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
				enabled = hasDiscoverPermission,
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
