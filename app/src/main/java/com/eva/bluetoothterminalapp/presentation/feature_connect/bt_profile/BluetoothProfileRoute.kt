package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile

import android.os.ParcelUuid
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.composable.ConnectionProfileList
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.composable.ConnectionProfileTopBar
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.state.BTProfileEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.state.BTProfileScreenState
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.presentation.util.SharedElementTransitionKeys
import com.eva.bluetoothterminalapp.presentation.util.sharedBoundsWrapper
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import java.util.UUID

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalSharedTransitionApi::class
)
@Composable
fun BluetoothProfileRoute(
	address: String,
	state: BTProfileScreenState,
	onEvent: (BTProfileEvents) -> Unit,
	onConnect: (UUID) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
) {

	val isInspectionMode = LocalInspectionMode.current
	val snackBarHost = LocalSnackBarProvider.current

	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	var selectedUUID by rememberSaveable(stateSaver = uuidSaver) {
		mutableStateOf(null)
	}

	val showConnectButton by remember(selectedUUID, state.isDiscovering) {
		derivedStateOf { selectedUUID != null && !state.isDiscovering }
	}

	Scaffold(
		topBar = {
			ConnectionProfileTopBar(
				showRefresh = !state.isDiscovering,
				onRefresh = {
					onEvent(BTProfileEvents.OnRetryFetchUUID)
					selectedUUID = null
				},
				navigation = navigation,
				scrollBehavior = scrollBehavior,
			)
		},
		floatingActionButton = {
			AnimatedVisibility(
				visible = showConnectButton || isInspectionMode,
				enter = slideInVertically() + fadeIn(),
				exit = slideOutVertically() + fadeOut()
			) {
				ExtendedFloatingActionButton(
					onClick = { selectedUUID?.let(onConnect) },
					shape = MaterialTheme.shapes.large,
					modifier = Modifier.sharedBoundsWrapper(
						SharedElementTransitionKeys.btClientScreen(address)
					)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_connect_variant),
						contentDescription = "Connect"
					)
					Spacer(modifier = Modifier.widthIn(4.dp))
					Text(text = stringResource(id = R.string.dialog_action_connect))
				}
			}
		},
		snackbarHost = { SnackbarHost(hostState = snackBarHost) },
		modifier = modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection)
			.sharedBoundsWrapper(SharedElementTransitionKeys.btProfileScreen(address)),
	) { scPadding ->
		ConnectionProfileList(
			selected = selectedUUID,
			isDiscovering = state.isDiscovering,
			available = state.deviceUUIDS,
			onUUIDSelect = { uuid -> selectedUUID = uuid },
			contentPadding = scPadding
		)
	}
}

private val uuidSaver = object : Saver<UUID?, ParcelUuid> {
	override fun restore(value: ParcelUuid): UUID? {
		return value.uuid
	}

	override fun SaverScope.save(value: UUID?): ParcelUuid? {
		return value?.let(::ParcelUuid)
	}
}

@PreviewLightDark
@Composable
private fun BluetoothProfileRoutePreview() = BlueToothTerminalAppTheme {
	BluetoothProfileRoute(
		address = "",
		state = PreviewFakes.FAKE_BT_DEVICE_PROFILE,
		onEvent = {},
		onConnect = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		},
	)
}