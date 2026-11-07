package com.eva.bluetoothterminalapp.presentation.composables

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.utils.hasBTAdvertisePermission
import com.eva.bluetoothterminalapp.data.utils.hasBTConnectPermission
import com.eva.bluetoothterminalapp.data.utils.hasBTScanPermission
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BluetoothPermissionButton(
	modifier: Modifier = Modifier,
	onResults: (Boolean) -> Unit = {},
) {
	if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) return

	val context = LocalContext.current

	var hasScanPermission by rememberSaveable { mutableStateOf(context.hasBTScanPermission) }
	var hasConnectPermission by rememberSaveable { mutableStateOf(context.hasBTConnectPermission) }
	var hasAdvertisePermission by rememberSaveable { mutableStateOf(context.hasBTAdvertisePermission) }

	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestMultiplePermissions()
	) { perms ->
		if (perms.containsKey(Manifest.permission.BLUETOOTH_SCAN)) {
			hasScanPermission = perms.getOrDefault(Manifest.permission.BLUETOOTH_SCAN, false)
		}
		if (perms.containsKey(Manifest.permission.BLUETOOTH_CONNECT)) {
			hasConnectPermission = perms.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false)
		}
		if (perms.containsKey(Manifest.permission.BLUETOOTH_ADVERTISE)) {
			hasAdvertisePermission =
				perms.getOrDefault(Manifest.permission.BLUETOOTH_ADVERTISE, false)
		}
		val hasAllPermissions = hasScanPermission && hasConnectPermission && hasAdvertisePermission
		onResults(hasAllPermissions)
	}

	Button(
		onClick = {
			val permissions = buildList {
				addAll(
					setOf(
						Manifest.permission.BLUETOOTH_SCAN,
						Manifest.permission.BLUETOOTH_CONNECT,
						Manifest.permission.BLUETOOTH_ADVERTISE
					)
				)
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
					add(Manifest.permission.POST_NOTIFICATIONS)

			}
			launcher.launch(permissions.toTypedArray())
		},
		contentPadding = ButtonDefaults.contentPaddingFor(
			ButtonDefaults.MediumContainerHeight,
			hasStartIcon = true
		),
		shapes = ButtonDefaults.shapes(
			pressedShape = ButtonDefaults.mediumPressedShape,
			shape = ButtonDefaults.shape
		),
		modifier = modifier
	) {
		Icon(
			imageVector = AppIconVectors.bluetooth,
			contentDescription = null,
			modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MediumContainerHeight)),
		)
		Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MediumContainerHeight)))
		Text(
			text = stringResource(R.string.allow_bluetooth_permission),
			style = ButtonDefaults.textStyleFor(ButtonDefaults.MediumContainerHeight),
		)
	}

}

@PreviewLightDark
@Composable
private fun BluetoothPermissionButtonPreview() = BlueToothTerminalAppTheme {
	BluetoothPermissionButton(onResults = {})
}