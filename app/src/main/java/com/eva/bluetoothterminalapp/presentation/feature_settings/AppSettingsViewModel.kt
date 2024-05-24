package com.eva.bluetoothterminalapp.presentation.feature_settings

import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.domain.settings.repository.BLESettingsDataStore
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import com.eva.bluetoothterminalapp.presentation.feature_settings.util.BLESettingsEvent
import com.eva.bluetoothterminalapp.presentation.feature_settings.util.BTSettingsEvent
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppSettingsViewModel(
	private val bleDatastore: BLESettingsDataStore,
	private val btDatastore: BTSettingsDataSore,
) : AppViewModel() {

	val bleSettings = bleDatastore.settingsFlow
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(2000),
			initialValue = BLESettingsModel()
		)

	val btSettings = btDatastore.settingsFlow
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(2000),
			initialValue = BTSettingsModel()
		)

	val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	fun onBLEEvents(event: BLESettingsEvent) {
		when (event) {
			is BLESettingsEvent.OnPhyLayerChange -> viewModelScope.launch {
				bleDatastore.onUpdateSupportedLayer(layer = event.layer)
			}

			is BLESettingsEvent.OnScanModeChange -> viewModelScope.launch {
				bleDatastore.onUpdateScanMode(scanMode = event.mode)
			}

			is BLESettingsEvent.OnScanPeriodChange -> viewModelScope.launch {
				bleDatastore.onUpdateScanPeriod(timming = event.timmings)
			}

			is BLESettingsEvent.OnToggleIsLegacyAdvertisement -> viewModelScope.launch {
				bleDatastore.onIsAdvertiseExtensionChanged(event.isLegacy)
			}
		}
	}

	fun onBTClassicEvents(event: BTSettingsEvent) {
		when (event) {
			is BTSettingsEvent.OnCharsetChange -> viewModelScope.launch {
				btDatastore.onCharsetChange(event.charSet)
			}

			is BTSettingsEvent.OnDisplayModeChange -> viewModelScope.launch {
				btDatastore.onDisplayModeChange(event.mode)
			}

			is BTSettingsEvent.OnNewLineCharChange -> viewModelScope.launch {
				btDatastore.onNewLineCharChange(event.newLineChar)
			}

			is BTSettingsEvent.OnShowTimestampChange -> viewModelScope.launch {
				btDatastore.onShowTimestampChange(event.isChange)
			}
		}
	}
}