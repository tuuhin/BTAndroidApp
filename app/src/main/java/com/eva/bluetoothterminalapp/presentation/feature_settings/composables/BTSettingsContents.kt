package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalCharSet
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalDisplayMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_settings.util.BTSettingsEvent
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BTSettingsContent(
	settings: BTSettingsModel,
	onEvent: (BTSettingsEvent) -> Unit,
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp)
) {

	val onCharsetChange: (BTTerminalCharSet) -> Unit = remember {
		{ charset -> onEvent(BTSettingsEvent.OnCharsetChange(charset)) }
	}

	val onDisplayModeChange: (BTTerminalDisplayMode) -> Unit = remember {
		{ mode -> onEvent(BTSettingsEvent.OnDisplayModeChange(mode)) }
	}

	val onShowTimeStampChange: (Boolean) -> Unit = remember {
		{ show -> onEvent(BTSettingsEvent.OnShowTimeStampValueChanged(show)) }
	}

	val onScrollEnabled: (Boolean) -> Unit = remember {
		{ isEnabled -> onEvent(BTSettingsEvent.OnAutoScrollValueChanged(isEnabled)) }
	}

	val onNewLineRecvCharChange: (BTTerminalNewLineChar) -> Unit = remember {
		{ newLineChar -> onEvent(BTSettingsEvent.OnReceiveNewLineCharChanged(newLineChar)) }
	}

	val onNewLineSendCharChange: (BTTerminalNewLineChar) -> Unit = remember {
		{ newLineChar -> onEvent(BTSettingsEvent.OnSendNewLineCharChanged(newLineChar)) }
	}

	val onLocalEchoChange: (Boolean) -> Unit = remember {
		{ isAllowed -> onEvent(BTSettingsEvent.OnLocalEchoValueChange(isAllowed)) }
	}

	val onKeepScreenOnValueChange: (Boolean) -> Unit = remember {
		{ keepOn -> onEvent(BTSettingsEvent.OnKeepScreenOnValueChange(keepOn)) }
	}

	val onClearInputOnSend: (Boolean) -> Unit = remember {
		{ isAllowed -> onEvent(BTSettingsEvent.OnClearInputValueChange(isAllowed)) }
	}

	LazyColumn(
		modifier = modifier,
		contentPadding = contentPadding,
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_terminal))
		}
		item {
			BTCharsetSelector(
				selectedCharset = settings.btTerminalCharSet,
				onCharsetChange = onCharsetChange,
			)
		}
		item {
			BTDisplayModeSelector(
				mode = settings.displayMode,
				onModeChange = onDisplayModeChange,
			)
		}
		item {
			BTShowTimestampSelector(
				showTimeStamp = settings.showTimeStamp,
				onChange = onShowTimeStampChange,
			)
		}
		item {
			BTAutoScrollEnabled(
				isAutoScrollEnabled = settings.autoScrollEnabled,
				onAutoScrollChange = onScrollEnabled,
			)
		}
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_recv))
		}
		item {
			BTNewLineSelector(
				newLineChar = settings.newLineCharReceive,
				onNewLineCharChange = onNewLineRecvCharChange,
			)
		}
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_send))
		}
		item {
			BTNewLineSelector(
				newLineChar = settings.newLineCharSend,
				onNewLineCharChange = onNewLineSendCharChange,
			)
		}
		item {
			BTLocalEchoSelector(
				isLocalEcho = settings.localEchoEnabled,
				onLocalEchoChange = onLocalEchoChange,
			)
		}
		item {
			BTClearInputSelector(
				isClear = settings.clearInputOnSend,
				onClearSettingsChange = onClearInputOnSend,
			)
		}
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_misc))
		}
		item {
			BTKeppScreenOnSelector(
				isKeepScreenTrue = settings.keepScreenOnWhenConnected,
				onKeepScreenOnSettingsChange = onKeepScreenOnValueChange,
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun BTSettingsContentPreview() = BlueToothTerminalAppTheme {
	Surface {
		BTSettingsContent(
			settings = PreviewFakes.FAKE_BT_SETTINGS,
			onEvent = {},
			modifier = Modifier
				.fillMaxSize()
				.padding(all = dimensionResource(id = R.dimen.sc_padding))
		)
	}
}