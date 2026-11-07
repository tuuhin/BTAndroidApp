package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile

import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.state.BTProfileEvents
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.state.BTProfileScreenState
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import java.util.UUID

@KoinViewModel
class BluetoothProfileViewModel(
	@InjectedParam private val address: String,
	private val clientConnector: BluetoothClientConnector,
) : AppViewModel() {

	private val _profile = MutableStateFlow(BTProfileScreenState())
	val profile = _profile
		.onStart { loadDeviceUUIDs() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000L),
			initialValue = BTProfileScreenState()
		)

	override val uiEvents: SharedFlow<UiEvents>
		field = MutableSharedFlow<UiEvents>()

	fun onEvent(event: BTProfileEvents) {
		when (event) {
			BTProfileEvents.OnRetryFetchUUID -> refreshUUIDs()
			is BTProfileEvents.OnAddCustomSPP -> onAddCustomUUids(event.uuid)
		}
	}

	private fun onAddCustomUUids(uuidString: String) = viewModelScope.launch {
		try {
			val uuid = UUID.fromString(uuidString)
			_profile.update { state ->
				val newList = persistentListOf<UUID>()
					.addingAll(state.customUUids)
					.adding(uuid)
				state.copy(customUUids = newList)
			}
		} catch (_: IllegalArgumentException) {
			uiEvents.emit(UiEvents.ShowSnackBar("Failed to parse UUID"))
		}
	}

	private fun loadDeviceUUIDs() = viewModelScope.launch {
		val result = clientConnector.loadDeviceFeatureUUID(address)
		result.fold(
			onSuccess = { ids ->
				_profile.update {
					it.copy(
						deviceUUIDS = ids.toPersistentList(),
						isDiscovering = false
					)
				}
			},
			onFailure = {
				uiEvents.emit(UiEvents.ShowSnackBar("Unable to load features refresh to continue"))
			},
		)
	}

	private fun refreshUUIDs() {

		val isDiscovering = _profile.value.isDiscovering
		if (isDiscovering) return

		_profile.update { it.copy(isDiscovering = true) }
		clientConnector.refreshDeviceFeatureUUID(address = address)
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