package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.state.BTProfileEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.state.BTProfileScreenState
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.screens.navArgs
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class BluetoothProfileViewModel(
	private val connector: BluetoothClientConnector,
	private val savedStateHandle: SavedStateHandle,
) : AppViewModel() {

	private val _profile = MutableStateFlow(BTProfileScreenState())
	val profile = _profile.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	private val device: BluetoothDeviceArgs?
		get() = savedStateHandle.navArgs()

	init {
		//load the uuids
		loadUUIDs()
	}

	fun onEvent(event: BTProfileEvents) {
		when (event) {
			BTProfileEvents.OnRetryFetchUUID -> realodUUIDs()
		}
	}

	private fun realodUUIDs() {
		val isDiscovering = _profile.value.isDiscovering
		if (isDiscovering) return
		_profile.update { it.copy(isDiscovering = true) }
		loadUUIDs()
	}

	private fun loadUUIDs() {
		val address = device?.address ?: return
		connector.fetchUUIDs(address = address)
			.catch { err -> err.printStackTrace() }
			.onEach { deviceUUIDs ->
				_profile.update { state ->
					state.copy(
						deviceUUIDS = deviceUUIDs.toImmutableList(),
						isDiscovering = false
					)
				}
			}
			.launchIn(viewModelScope)
	}

}