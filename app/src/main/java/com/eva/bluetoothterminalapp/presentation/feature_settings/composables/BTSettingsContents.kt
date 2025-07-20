package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
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
				onCharsetChange = { charset -> onEvent(BTSettingsEvent.OnCharsetChange(charset)) },
			)
		}
		item {
			BTDisplayModeSelector(
				mode = settings.displayMode,
				onModeChange = { mode -> onEvent(BTSettingsEvent.OnDisplayModeChange(mode)) },
			)
		}
		item {
			BTShowTimestampSelector(
				showTimeStamp = settings.showTimeStamp,
				onChange = { show -> onEvent(BTSettingsEvent.OnShowTimeStampValueChanged(show)) },
			)
		}
		item {
			BTAutoScrollEnabled(
				isAutoScrollEnabled = settings.autoScrollEnabled,
				onAutoScrollChange = { isEnabled ->
					onEvent(
						BTSettingsEvent.OnAutoScrollValueChanged(
							isEnabled
						)
					)
				},
			)
		}
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_recv))
		}
		item {
			BTNewLineSelector(
				newLineChar = settings.newLineCharReceive,
				onNewLineCharChange = { newLineChar ->
					onEvent(
						BTSettingsEvent.OnReceiveNewLineCharChanged(
							newLineChar
						)
					)
				},
			)
		}
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_send))
		}
		item {
			BTNewLineSelector(
				newLineChar = settings.newLineCharSend,
				onNewLineCharChange = { newLineChar ->
					onEvent(
						BTSettingsEvent.OnSendNewLineCharChanged(
							newLineChar
						)
					)
				},
			)
		}
		item {
			BTLocalEchoSelector(
				isLocalEcho = settings.localEchoEnabled,
				onLocalEchoChange = { isAllowed ->
					onEvent(
						BTSettingsEvent.OnLocalEchoValueChange(
							isAllowed
						)
					)
				},
			)
		}
		item {
			BTClearInputSelector(
				isClear = settings.clearInputOnSend,
				onClearSettingsChange = { isAllowed ->
					onEvent(
						BTSettingsEvent.OnClearInputValueChange(
							isAllowed
						)
					)
				},
			)
		}
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_misc))
		}
		item {
			BTKeppScreenOnSelector(
				isKeepScreenTrue = settings.keepScreenOnWhenConnected,
				onKeepScreenOnSettingsChange = { keepOn ->
					onEvent(
						BTSettingsEvent.OnKeepScreenOnValueChange(
							keepOn
						)
					)
				},
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