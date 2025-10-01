package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.composables.BTNotEnabledBox
import com.eva.bluetoothterminalapp.presentation.composables.BtPermissionNotProvidedBox
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.BluetoothScreenType
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun DevicesScreenModeContainer(
	isActive: Boolean,
	hasPermission: Boolean,
	onBTPermissionChanged: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	content: @Composable BoxScope.() -> Unit,
) {
	val screenType by remember(hasPermission, isActive) {
		derivedStateOf {
			when {
				hasPermission && isActive -> BluetoothScreenType.BLUETOOTH_PERMISSION_GRANTED
				hasPermission && !isActive -> BluetoothScreenType.BLUETOOTH_NOT_ENABLED
				else -> BluetoothScreenType.BLUETOOTH_PERMISSION_DENIED
			}
		}
	}

	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center
	) {
		Crossfade(
			targetState = screenType,
			label = "Show some type of screen based on screen type",
			animationSpec = tween(durationMillis = 400),
		) { mode ->
			when (mode) {
				BluetoothScreenType.BLUETOOTH_NOT_ENABLED -> BTNotEnabledBox(
					modifier = Modifier
						.padding(dimensionResource(R.dimen.sc_padding))
				)

				BluetoothScreenType.BLUETOOTH_PERMISSION_DENIED -> BtPermissionNotProvidedBox(
					onPermissionChanged = onBTPermissionChanged,
					modifier = Modifier
						.padding(dimensionResource(R.dimen.sc_padding))
				)

				BluetoothScreenType.BLUETOOTH_PERMISSION_GRANTED -> content()
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun DevicesScreenModeContainerBTNotActivePreview() = BlueToothTerminalAppTheme {
	Surface {
		DevicesScreenModeContainer(
			isActive = false,
			hasPermission = true,
			onBTPermissionChanged = {},
			content = {},
		)
	}
}

@PreviewLightDark
@Composable
private fun DevicesScreenModeContainerPermissionNotProvided() = BlueToothTerminalAppTheme {
	Surface {
		DevicesScreenModeContainer(
			isActive = true,
			hasPermission = false,
			onBTPermissionChanged = {},
			content = {},
		)
	}
}