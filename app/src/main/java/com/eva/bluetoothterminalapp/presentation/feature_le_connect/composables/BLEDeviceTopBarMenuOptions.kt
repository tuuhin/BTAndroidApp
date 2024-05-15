package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLEdeviceTopBarMenuOptions(
	onReDiscoverServices: () -> Unit,
	onReadRemoteRssi: () -> Unit,
	modifier: Modifier = Modifier
) {

	var isMenuExpanded by remember { mutableStateOf(false) }
	var menuOffset by remember { mutableStateOf(DpOffset.Zero) }

	val interactionSource = remember { MutableInteractionSource() }

	Box(
		modifier = modifier
			.clip(CircleShape)
			.defaultMinSize(minWidth = 32.dp, minHeight = 32.dp)
			.indication(interactionSource, LocalIndication.current)
			.pointerInput(Unit) {
				detectTapGestures(
					onPress = {
						val press = PressInteraction.Press(it)
						interactionSource.emit(press)
						// set the offset when pressed
						menuOffset = DpOffset(it.x.toDp(), it.y.toDp())
						tryAwaitRelease()
						val release = PressInteraction.Release(press)
						interactionSource.emit(release)
						// open the menu when released
						isMenuExpanded = true
					},
				)
			},
		contentAlignment = Alignment.Center
	) {
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
			Icon(
				imageVector = Icons.Default.MoreVert,
				contentDescription = stringResource(id = R.string.menu_option_more),
			)
		}
		DropdownMenu(
			expanded = isMenuExpanded,
			onDismissRequest = { isMenuExpanded = false },
			offset = menuOffset
		) {
			DropdownMenuItem(
				text = { Text(text = "Read Remote RSSI") },
				onClick = onReadRemoteRssi,
				colors = MenuDefaults
					.itemColors(leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer)
			)
			DropdownMenuItem(
				text = { Text(text = "Refresh Characteristics") },
				onClick = onReDiscoverServices,
				colors = MenuDefaults
					.itemColors(leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer)
			)
		}
	}
}