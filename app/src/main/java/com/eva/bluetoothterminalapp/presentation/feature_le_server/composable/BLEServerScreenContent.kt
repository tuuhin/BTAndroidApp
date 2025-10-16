package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.eva.bluetoothterminalapp.data.utils.hasBTAdvertisePermission
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BLEServerScreenContent(
	connectedClients: ImmutableList<BluetoothDeviceModel>,
	services: ImmutableList<BLEServiceModel>,
	onStartServer: () -> Unit,
	modifier: Modifier = Modifier,
	isServerRunning: Boolean = false,
	contentPadding: PaddingValues = PaddingValues.Zero
) {
	val context = LocalContext.current
	var canAdvertise by remember { mutableStateOf(context.hasBTAdvertisePermission) }
	var showDialog by remember { mutableStateOf(false) }

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		val launcher = rememberLauncherForActivityResult(
			contract = ActivityResultContracts.RequestPermission(),
			onResult = { result -> canAdvertise = result },
		)

		AdvertisePermissionDialog(
			showDialog = showDialog && !canAdvertise,
			onConfirm = { launcher.launch(Manifest.permission.BLUETOOTH_ADVERTISE) },
			onDismiss = { showDialog = false },
		)
	}

	Crossfade(
		targetState = isServerRunning,
		modifier = modifier,
	) { isRunning ->
		if (isRunning) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(contentPadding)
			) {
				BLEConnectedClients(
					connectedClients = connectedClients,
					modifier = Modifier.weight(1.2f)
				)
				BLEAdvertisingServices(
					services = services,
					modifier = Modifier.weight(.8f)
				)
			}
		} else {
			StartBLEServer(
				onStartServer = { if (canAdvertise) onStartServer() else showDialog = true },
				onConfigureServices = {},
				modifier = Modifier
					.fillMaxSize()
					.padding(contentPadding)
			)
		}
	}
}


