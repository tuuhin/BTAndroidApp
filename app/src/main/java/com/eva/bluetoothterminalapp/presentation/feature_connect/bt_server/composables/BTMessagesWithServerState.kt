package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.zIndex
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.ServerConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.BTMessagesList
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BTMessagesWithServerState(
	connectionState: ServerConnectionState,
	messages: ImmutableList<BluetoothMessage>,
	modifier: Modifier = Modifier,
	scrollToEnd: Boolean = true,
	showTimestamps: Boolean = true,
) {
	Box(
		modifier = modifier
			.clip(MaterialTheme.shapes.medium)
			.background(MaterialTheme.colorScheme.surfaceContainer),
	) {
		ServerConnectionStateChip(
			connectionState = connectionState,
			modifier = Modifier
				.offset(y = dimensionResource(id = R.dimen.connection_chip_offset))
				.align(Alignment.TopCenter)
				.zIndex(1f)
		)
		BTMessagesList(
			messages = messages,
			scrollToEnd = scrollToEnd,
			showTimeInMessage = showTimestamps,
			contentPadding = PaddingValues(
				horizontal = dimensionResource(id = R.dimen.messages_list_horizontal_padding),
				vertical = dimensionResource(id = R.dimen.messages_list_vertical_padding)
			),
			modifier = Modifier.fillMaxSize()
		)
	}
}