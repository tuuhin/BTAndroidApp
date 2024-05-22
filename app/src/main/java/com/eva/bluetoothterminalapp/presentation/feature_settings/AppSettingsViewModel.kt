package com.eva.bluetoothterminalapp.presentation.feature_settings

import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel
import com.eva.bluetoothterminalapp.domain.settings.repository.BLESettingsDataStore
import com.eva.bluetoothterminalapp.presentation.feature_settings.util.BLESettingsEvent
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppSettingsViewModel(
	private val bleDatastore: BLESettingsDataStore
) : AppViewModel() {

	val bleSettings = bleDatastore.settingsFlow
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(2000),
			initialValue = BLESettingsModel()
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
}