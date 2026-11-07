package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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

	val colors =
		ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

	LazyColumn(
		modifier = modifier,
		contentPadding = contentPadding,
		verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
	) {
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_terminal))
		}
		item {
			BTCharsetSelector(
				shapes = ListItemDefaults.segmentedShapes(0, 4),
				colors = colors,
				selectedCharset = settings.btTerminalCharSet,
				onCharsetChange = { charset -> onEvent(BTSettingsEvent.OnCharsetChange(charset)) },
			)
		}
		item {
			BTDisplayModeSelector(
				shapes = ListItemDefaults.segmentedShapes(1, 4),
				colors = colors,
				mode = settings.displayMode,
				onModeChange = { mode -> onEvent(BTSettingsEvent.OnDisplayModeChange(mode)) },
			)
		}
		item {
			SegmentedListItem(
				shapes = ListItemDefaults.segmentedShapes(2, 4),
				colors = colors,
				checked = settings.showTimeStamp,
				onCheckedChange = { show -> onEvent(BTSettingsEvent.OnShowTimeStampValueChanged(show)) },
				trailingContent = {
					Switch(
						checked = settings.showTimeStamp,
						onCheckedChange = null
					)
				},
				content = { Text(text = stringResource(id = R.string.bt_classic_settings_show_time_title)) },
				supportingContent = { Text(text = stringResource(id = R.string.bt_classic_settings_show_time_desc)) },
			)
		}
		item {
			SegmentedListItem(
				shapes = ListItemDefaults.segmentedShapes(3, 4),
				colors = colors,
				checked = settings.autoScrollEnabled,
				onCheckedChange = { isEnabled ->
					onEvent(BTSettingsEvent.OnAutoScrollValueChanged(isEnabled))
				},
				trailingContent = {
					Switch(
						checked = settings.autoScrollEnabled,
						onCheckedChange = null
					)
				},
				content = { Text(text = stringResource(id = R.string.bt_classic_settings_auto_scroll_title)) },
				supportingContent = { Text(text = stringResource(id = R.string.bt_classic_settings_auto_scroll_desc)) },
			)
		}
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_recv))
		}
		item {
			BTNewLineSelector(
				shapes = ListItemDefaults.segmentedShapes(0, 1),
				colors = colors,
				newLineChar = settings.newLineCharReceive,
				onNewLineCharChange = { newLineChar ->
					onEvent(BTSettingsEvent.OnReceiveNewLineCharChanged(newLineChar))
				},
			)
		}
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_send))
		}
		item {
			BTNewLineSelector(
				shapes = ListItemDefaults.segmentedShapes(0, 3),
				colors = colors,
				newLineChar = settings.newLineCharSend,
				onNewLineCharChange = { newLineChar ->
					onEvent(BTSettingsEvent.OnSendNewLineCharChanged(newLineChar))
				},
			)
		}
		item {
			SegmentedListItem(
				shapes = ListItemDefaults.segmentedShapes(1, 3),
				colors = colors,
				checked = settings.localEchoEnabled,
				onCheckedChange = { isAllowed ->
					onEvent(BTSettingsEvent.OnLocalEchoValueChange(isAllowed))
				},
				trailingContent = {
					Switch(
						checked = settings.localEchoEnabled,
						onCheckedChange = null
					)
				},
				content = { Text(text = stringResource(id = R.string.bt_classic_settings_local_echo_title)) },
				supportingContent = { Text(text = stringResource(id = R.string.bt_classic_settings_local_echo_desc)) },
			)
		}
		item {
			SegmentedListItem(
				shapes = ListItemDefaults.segmentedShapes(2, 3),
				colors = colors,
				checked = settings.clearInputOnSend,
				onCheckedChange = { isAllowed ->
					onEvent(BTSettingsEvent.OnClearInputValueChange(isAllowed))
				},
				trailingContent = {
					Switch(
						checked = settings.clearInputOnSend,
						onCheckedChange = null
					)
				},
				content = { Text(text = stringResource(id = R.string.bt_classic_settings_clear_input_title)) },
			)
		}
		bTListItemTitle {
			Text(text = stringResource(id = R.string.bt_classic_settings_category_misc))
		}
		item {
			SegmentedListItem(
				shapes = ListItemDefaults.segmentedShapes(0, 1),
				colors = colors,
				checked = settings.keepScreenOnWhenConnected,
				onCheckedChange = { keepOn ->
					onEvent(BTSettingsEvent.OnKeepScreenOnValueChange(keepOn))
				},
				trailingContent = {
					Switch(
						checked = settings.localEchoEnabled,
						onCheckedChange = null
					)
				},
				content = { Text(text = stringResource(id = R.string.bt_classic_settings_keep_screen_on_title)) },
				supportingContent = { Text(text = stringResource(id = R.string.bt_classic_settings_keep_screen_on_desc)) },
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