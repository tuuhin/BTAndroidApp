package com.eva.bluetoothterminalapp.presentation.composables

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
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

	var hasScanPermission by remember { mutableStateOf(context.hasBTScanPermission) }
	var hasConnectPermission by remember { mutableStateOf(context.hasBTConnectPermission) }
	var hasAdvertisePermission by remember { mutableStateOf(context.hasBTAdvertisePermission) }

	val permissionLauncher = rememberLauncherForActivityResult(
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

		Log.d(
			"PERMISSIONS",
			"SCAN: $hasScanPermission CONNECT:$hasConnectPermission ADVERTISE: $hasAdvertisePermission"
		)

		val hasAllPermissions = hasScanPermission && hasConnectPermission && hasAdvertisePermission
		onResults(hasAllPermissions)
	}

	Button(
		onClick = {
			permissionLauncher.launch(
				arrayOf(
					Manifest.permission.BLUETOOTH_SCAN,
					Manifest.permission.BLUETOOTH_CONNECT,
					Manifest.permission.BLUETOOTH_ADVERTISE,
				)
			)
		},
		modifier = modifier,
		shape = MaterialTheme.shapes.medium
	) {
		Icon(imageVector = Icons.Default.Bluetooth, contentDescription = null)
		Spacer(modifier = Modifier.width(2.dp))
		Text(text = "Allow Permissions")
	}

}

@PreviewLightDark
@Composable
private fun BluetoothPermissionButtonPreview() = BlueToothTerminalAppTheme {
	BluetoothPermissionButton(onResults = {})
}