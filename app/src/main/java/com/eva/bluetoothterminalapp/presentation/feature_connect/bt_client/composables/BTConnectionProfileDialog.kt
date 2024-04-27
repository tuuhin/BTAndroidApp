package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.ConnectProfileState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state.InitiateConnectionEvent
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BTWConnectionProfileDialog(
	uuids: ImmutableList<UUID>,
	onUUIDChanged: (UUID) -> Unit,
	onConnect: () -> Unit,
	modifier: Modifier = Modifier,
	selectedUUID: UUID? = null,
	isDiscovering: Boolean = false,
	onCancel: () -> Unit = {},
) {

	val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

	Scaffold(
		topBar = {
			ConnectionModeTopBar(
				canConnect = selectedUUID != null,
				onConnect = onConnect,
				onCancel = onCancel
			)
		},
		modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
	) { scPadding ->
		ConnectionProfileList(
			selectedUUID = selectedUUID,
			deviceUUIDs = uuids,
			onUUIDSelect = onUUIDChanged,
			isDiscovering = isDiscovering,
			contentPadding = scPadding
		)
	}

}

@Composable
fun BTWConnectionProfileDialog(
	state: ConnectProfileState,
	onEvent: (InitiateConnectionEvent) -> Unit,
	modifier: Modifier = Modifier,
	properties: DialogProperties = DialogProperties(
		dismissOnBackPress = false,
		dismissOnClickOutside = false,
		usePlatformDefaultWidth = false,
		decorFitsSystemWindows = false
	)
) {
	if (!state.showProfileDialog) return

	Dialog(
		onDismissRequest = {},
		properties = properties,
	) {
		BTWConnectionProfileDialog(
			uuids = state.deviceUUIDS,
			selectedUUID = state.selectedUUID,
			isDiscovering = state.isDiscovering,
			onUUIDChanged = { type ->
				onEvent(InitiateConnectionEvent.OnSelectUUID(type))
			},
			onCancel = { onEvent(InitiateConnectionEvent.OnCancelAndNavigateBack) },
			onConnect = { onEvent(InitiateConnectionEvent.OnAcceptConnection) },
			modifier = modifier,
		)
	}
}

@PreviewLightDark
@Composable
private fun BTConnectionProfileRoutePreview() = BlueToothTerminalAppTheme {
	BTWConnectionProfileDialog(
		selectedUUID = UUID.fromString("d5ac5345-e023-40e6-9238-41d6f695cd45"),
		uuids = persistentListOf(
			UUID.fromString("d5ac5345-e023-40e6-9238-41d6f695cd45"),
			UUID.fromString("9297ad46-9f23-4ff5-b09e-72b3b3dab91b")
		),
		onUUIDChanged = {},
		onCancel = {},
		onConnect = {}
	)
}