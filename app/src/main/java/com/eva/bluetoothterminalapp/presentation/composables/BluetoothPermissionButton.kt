package com.eva.bluetoothterminalapp.presentation.composables

import android.Manifest
import android.os.Build
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
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BluetoothPermissionButton(
	modifier: Modifier = Modifier,
	onResults: (Boolean) -> Unit = {},
) {
	if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) return

	val context = LocalContext.current

	var hasScanPermission by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.BLUETOOTH_SCAN
			) == PermissionChecker.PERMISSION_GRANTED
		)
	}

	var hasConnectPermission by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.BLUETOOTH_SCAN
			) == PermissionChecker.PERMISSION_GRANTED
		)
	}

	val permissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestMultiplePermissions()
	) { perms ->
		hasScanPermission = perms.getOrDefault(Manifest.permission.BLUETOOTH_SCAN, false)
		hasConnectPermission = perms.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false)

		val hasBothPermission = hasScanPermission && hasConnectPermission
		onResults(hasBothPermission)
	}

	Button(
		onClick = {
			permissionLauncher.launch(
				arrayOf(
					Manifest.permission.BLUETOOTH_SCAN,
					Manifest.permission.BLUETOOTH_CONNECT
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