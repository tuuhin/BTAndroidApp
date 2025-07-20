package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerDeviceState
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.state.BTServerScreenState
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.BTMessagesList
import com.eva.bluetoothterminalapp.presentation.feature_connect.composables.SendCommandTextField

@Composable
fun BTPeerInteractionContent(
	deviceState: BTServerDeviceState,
	messagesState: BTServerScreenState,
	btSettings: BTSettingsModel,
	onEvent: (BTServerEvents) -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
	) {
		BTServerConnectionHeader(
			device = deviceState,
			modifier = Modifier.align(Alignment.CenterHorizontally)
		)
		BTMessagesList(
			messages = messagesState.messages,
			scrollToEnd = btSettings.autoScrollEnabled,
			showTimeInMessage = btSettings.showTimeStamp,
			contentPadding = PaddingValues(dimensionResource(R.dimen.sc_padding_secondary)),
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f)
		)
		HorizontalDivider(
			color = MaterialTheme.colorScheme.outlineVariant,
			modifier = Modifier.padding(
				vertical = dimensionResource(id = R.dimen.messages_text_field_spacing)
			)
		)
		SendCommandTextField(
			value = messagesState.textFieldValue,
			isEnable = deviceState.isAccepted,
			onChange = { value -> onEvent(BTServerEvents.OnTextFieldValue(value)) },
			onImeAction = { onEvent(BTServerEvents.OnSendEvents) },
			modifier = Modifier
				.windowInsetsPadding(WindowInsets.navigationBars)
				.fillMaxWidth()
		)
	}
}