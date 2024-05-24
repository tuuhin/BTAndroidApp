package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
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
		item {
			BTCharsetSelector(
				selectedCharset = settings.charset,
				onCharsetChange = { onEvent(BTSettingsEvent.OnCharsetChange(it)) },
			)
		}
		item {
			BTNewLineSelector(
				newLineChar = settings.newLineChar,
				onNewLineCharChange = { onEvent(BTSettingsEvent.OnNewLineCharChange(it)) })
		}
		item {
			BTDisplayModeSelector(
				mode = settings.displayMode,
				onModeChange = { onEvent(BTSettingsEvent.OnDisplayModeChange(it)) },
			)
		}
		item {
			BTShowTimestampSelector(
				showTimeStamp = settings.showTimeStamp,
				onChange = { onEvent(BTSettingsEvent.OnShowTimestampChange(it)) },
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
			modifier = Modifier.fillMaxSize()
		)
	}
}