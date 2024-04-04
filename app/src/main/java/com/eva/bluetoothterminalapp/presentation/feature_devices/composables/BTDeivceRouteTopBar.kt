package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.SettingsApplications
import androidx.compose.material.icons.outlined.SettingsBluetooth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTDeviceRouteTopBar(
	navigation: @Composable () -> Unit,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current

	var isMenuExpanded by remember { mutableStateOf(false) }
	var menuOffset by remember { mutableStateOf(DpOffset.Zero) }
	val interactionSource = remember { MutableInteractionSource() }

	TopAppBar(
		title = { Text(text = stringResource(id = R.string.devices_route)) },
		navigationIcon = navigation,
		actions = {
			TooltipBox(
				positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
				tooltip = {
					PlainTooltip {
						Text(
							text = stringResource(id = R.string.settings_tooltip_text),
							style = MaterialTheme.typography.labelMedium
						)
					}
				},
				state = rememberTooltipState()
			) {
				Box(
					modifier = Modifier
						.defaultMinSize(minHeight = 32.dp, minWidth = 32.dp)
						.clip(CircleShape)
						.indication(interactionSource, LocalIndication.current)
						.pointerInput(Unit) {
							detectTapGestures(
								onPress = {
									val press = PressInteraction.Press(it)
									interactionSource.emit(press)
									isMenuExpanded = true

									tryAwaitRelease()
									val release = PressInteraction.Release(press)
									interactionSource.emit(release)
								},
								onTap = {
									menuOffset = DpOffset(it.x.toDp(), it.y.toDp())
								},
							)
						},
					contentAlignment = Alignment.Center
				) {
					Icon(
						imageVector = Icons.Default.Settings,
						contentDescription = null,
					)
				}
			}

			DropdownMenu(
				expanded = isMenuExpanded,
				onDismissRequest = { isMenuExpanded = false },
				offset = menuOffset
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
							imageVector = Icons.Outlined.SettingsApplications,
							contentDescription = stringResource(id = R.string.permission_settings)
						)
					}
				)
				DropdownMenuItem(
					text = { Text(text = stringResource(id = R.string.bluetooth_settings)) },
					onClick = {
						val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
						context.startActivity(intent)
					},
					leadingIcon = {
						Icon(
							imageVector = Icons.Outlined.SettingsBluetooth,
							contentDescription = stringResource(id = R.string.bluetooth_settings)
						)
					},
				)
			}
		},
		modifier = modifier
	)
}