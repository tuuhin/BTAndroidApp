package com.eva.bluetoothterminalapp.presentation.feature_le_server

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServerServices
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables.CloseConnectionDialog
import com.eva.bluetoothterminalapp.presentation.feature_le_server.composable.BLEServerOptionSelectorBottomSheet
import com.eva.bluetoothterminalapp.presentation.feature_le_server.composable.BLEServerScreenContent
import com.eva.bluetoothterminalapp.presentation.feature_le_server.state.BLEServerScreenEvents
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLEServerRoute(
	connectedClients: ImmutableList<BluetoothDeviceModel>,
	connectedServices: ImmutableList<BLEServiceModel>,
	onEvent: (BLEServerScreenEvents) -> Unit,
	modifier: Modifier = Modifier,
	serverServicesOptions: ImmutableSet<BLEServerServices> = persistentSetOf(),
	isServerRunning: Boolean = false,
	showServiceOptionSelector: Boolean = false,
	showConnectionCloseDialog: Boolean = false,
	navigation: @Composable () -> Unit = {}
) {
	val snackBarHostState = LocalSnackBarProvider.current
	val layoutDirection = LocalLayoutDirection.current

	val scrollConnection = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

	val scope = rememberCoroutineScope()

	BackHandler(
		enabled = isServerRunning,
		onBack = { onEvent(BLEServerScreenEvents.OnShowServerRunningDialog) }
	)

	CloseConnectionDialog(
		showDialog = showConnectionCloseDialog,
		onConfirm = {
			onEvent(BLEServerScreenEvents.OnStopServer)
			onEvent(BLEServerScreenEvents.OnCloseServerRunningDialog)
		},
		onDismiss = { onEvent(BLEServerScreenEvents.OnCloseServerRunningDialog) }
	)

	BLEServerOptionSelectorBottomSheet(
		showSheet = showServiceOptionSelector,
		selectedList = serverServicesOptions,
		onSelectUnSelectOption = { onEvent(BLEServerScreenEvents.UpdateServiceOptions(it)) },
		onDismiss = {
			scope.launch { sheetState.hide() }
				.invokeOnCompletion { onEvent(BLEServerScreenEvents.OnToggleServiceSelector) }
		},
	)

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = { Text(text = stringResource(R.string.ble_server_title)) },
				actions = {
					AnimatedVisibility(
						visible = isServerRunning,
						enter = slideInVertically(),
						exit = slideOutVertically()
					) {
						TextButton(
							onClick = { onEvent(BLEServerScreenEvents.OnStopServer) },
							enabled = !transition.isRunning
						) {
							Text(stringResource(R.string.stop_ble_server))
						}
					}
				},
				navigationIcon = navigation,
				scrollBehavior = scrollConnection
			)
		},
		snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
		modifier = modifier.nestedScroll(scrollConnection.nestedScrollConnection),
	) { scPadding ->
		BLEServerScreenContent(
			connectedClients = connectedClients,
			services = connectedServices,
			isServerRunning = isServerRunning,
			onStartServer = { onEvent(BLEServerScreenEvents.OnStartServer) },
			onConfigureServices = {
				scope.launch { sheetState.show() }
					.invokeOnCompletion {
						onEvent(BLEServerScreenEvents.OnToggleServiceSelector)
					}
			},
			contentPadding = PaddingValues(
				top = scPadding.calculateTopPadding() + dimensionResource(R.dimen.sc_padding_secondary),
				bottom = scPadding.calculateBottomPadding() + dimensionResource(R.dimen.sc_padding_secondary),
				start = scPadding.calculateStartPadding(layoutDirection) + dimensionResource(R.dimen.sc_padding),
				end = scPadding.calculateEndPadding(layoutDirection) + dimensionResource(R.dimen.sc_padding)
			),
			modifier = Modifier.fillMaxSize()
		)
	}
}

class IsServerRunningPreviewParams :
	CollectionPreviewParameterProvider<Boolean>(listOf(true, false))

@PreviewLightDark
@Composable
private fun BLEServerRoutePreview(
	@PreviewParameter(IsServerRunningPreviewParams::class)
	isServerRunning: Boolean
) = BlueToothTerminalAppTheme {
	BLEServerRoute(
		connectedClients = persistentListOf(
			PreviewFakes.FAKE_DEVICE_MODEL,
			PreviewFakes.FAKE_DEVICE_MODEL,
			PreviewFakes.FAKE_DEVICE_MODEL
		),
		connectedServices = persistentListOf(PreviewFakes.FAKE_SERVICE_WITH_CHARACTERISTICS),
		isServerRunning = isServerRunning,
		onEvent = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = "Back",
			)
		},
	)
}