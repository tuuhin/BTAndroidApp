package com.eva.bluetoothterminalapp.presentation.feature_le_connect

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLECharacteristicEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceConfigEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.CharacteristicWriteDialogState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.SelectedCharacteristicState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.WriteCharacteristicEvent
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.navigation.screens.navArgs
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BLEDeviceViewModel(
	private val bleConnector: BluetoothLEClientConnector,
	private val savedStateHandle: SavedStateHandle,
) : AppViewModel() {

	private val _selectedCharacteristic = MutableStateFlow(SelectedCharacteristicState())
	val selectedCharacteristic = _selectedCharacteristic.asStateFlow()

	private val _writeDialogState = MutableStateFlow(CharacteristicWriteDialogState())
	val writeDialogState = _writeDialogState.asStateFlow()

	val readCharacteristic = combine(
		bleConnector.readForCharacteristic,
		selectedCharacteristic,
		bleConnector.isNotifyOrIndicationRunning,
		transform = ::evalutateReadCharacteristics
	).stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(2000),
		initialValue = null
	)

	val bLEProfile = combine(
		bleConnector.deviceRssi,
		bleConnector.bleServices,
		bleConnector.connectionState,
	) { deviceRssi, services, connectState ->
		BLEDeviceProfileState(
			connectionState = connectState,
			device = bleConnector.connectedDevice,
			signalStrength = deviceRssi,
			services = services.toImmutableList()
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(2000),
		initialValue = BLEDeviceProfileState()
	)

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	private val navArgs: BluetoothDeviceArgs?
		get() = savedStateHandle.navArgs()

	private val isNotifyOrIndicationRunning: Boolean
		get() = bleConnector.isNotifyOrIndicationRunning.value

	init {
		navArgs?.address?.let(bleConnector::connect)
	}

	fun onCharacteristicEvent(event: BLECharacteristicEvent) {
		when (event) {
			is BLECharacteristicEvent.OnSelectCharacteristic -> _selectedCharacteristic
				.update { char ->
					char.copy(service = event.service, characteristic = event.characteristics)
				}

			BLECharacteristicEvent.OnUnSelectCharactetistic -> onUnselectCharacterisitc()

			is BLECharacteristicEvent.OnDescriptorRead -> readBLEDescriptor(event.desc)
			BLECharacteristicEvent.OnIndicateCharacteristic -> onIndicateOrNotifyBLECharacteristic()
			BLECharacteristicEvent.OnNotifyCharacteristic -> onIndicateOrNotifyBLECharacteristic()
			BLECharacteristicEvent.ReadCharacteristic -> onReadBLECharactetistics()
			BLECharacteristicEvent.WriteCharacteristic -> onWriteEvent(WriteCharacteristicEvent.OpenDialog)
			BLECharacteristicEvent.OnStopNotifyOrIndication -> stopIndications()
		}
	}

	fun onWriteEvent(event: WriteCharacteristicEvent) {
		when (event) {
			WriteCharacteristicEvent.CloseDialog -> _writeDialogState.update { state ->
				state.copy(showWriteDialog = false)
			}

			is WriteCharacteristicEvent.OnTextFieldValueChange -> _writeDialogState.update { state ->
				state.copy(writeTextFieldValue = event.value)
			}

			WriteCharacteristicEvent.OpenDialog -> _writeDialogState.update { selected ->
				selected.copy(showWriteDialog = true)
			}

			WriteCharacteristicEvent.WriteCharacteristicValue -> onWriteBLECharacteristic()
		}
	}

	fun stopIndications() = onIndicateOrNotifyBLECharacteristic(false)

	fun onConfigEvents(event: BLEDeviceConfigEvent) {
		when (event) {
			BLEDeviceConfigEvent.OnReadRssiStrength -> bleConnector.discoverServices()
			BLEDeviceConfigEvent.OnRefreshCharacteristics -> bleConnector.checkRssi()
			BLEDeviceConfigEvent.OnDisconnectEvent -> bleConnector.disconnect()
			BLEDeviceConfigEvent.OnReconnectEvent -> bleConnector.reconnect()
		}
	}

	private fun onUnselectCharacterisitc() {
		if (isNotifyOrIndicationRunning)
			onIndicateOrNotifyBLECharacteristic(isStart = false)

		_selectedCharacteristic.update { SelectedCharacteristicState() }
	}

	private suspend fun evalutateReadCharacteristics(
		readCharactertistic: BLECharacteristicsModel?,
		selected: SelectedCharacteristicState,
		isSetNoticationActive: Boolean,
	): BLECharacteristicsModel? {
		// if not selected there is nothing to read to
		if (selected.characteristic == null) return null
		if (readCharactertistic == null)
			return selected.characteristic.copy(isSetNotificationActive = isSetNoticationActive)
		val isSameCharacteristic = readCharactertistic.uuid == selected.characteristic.uuid
				&& readCharactertistic.instanceId == selected.characteristic.instanceId
		// any of the reader is started match the uuids to check for data
		val outResult = if (isSameCharacteristic) readCharactertistic else selected.characteristic
		return outResult.copy(isSetNotificationActive = isSetNoticationActive)
	}

	private fun onWriteBLECharacteristic() {

		val characteristic = _selectedCharacteristic.value.characteristic ?: return
		val service = _selectedCharacteristic.value.service ?: return
		val value = _writeDialogState.value.writeTextFieldValue

		if (value.isBlank()) {
			_writeDialogState.update { state -> state.copy(error = "Cant send blank value") }
			return
		}

		val result = bleConnector.write(
			service = service,
			characteristic = characteristic,
			value = value
		)

		result.fold(
			onFailure = { err ->
				viewModelScope.launch {
					_uiEvents.emit(UiEvents.ShowSnackBar(err.message ?: "Cannot perform action"))
				}
			},
			onSuccess = {
				_writeDialogState.update { state ->
					state.copy(
						writeTextFieldValue = "",
						showWriteDialog = false
					)
				}
			},
		)
	}


	private fun onIndicateOrNotifyBLECharacteristic(isStart: Boolean = true) {
		val characteristic = _selectedCharacteristic.value.characteristic ?: return
		val service = _selectedCharacteristic.value.service ?: return

		val results = bleConnector.startIndicationOrNotification(
			service = service,
			characteristic = characteristic,
			enable = isStart
		)
		viewModelScope.launch {
			results.fold(
				onSuccess = { isOke ->
					if (isOke) {
						val message = if (isStart) "enabled" else "stopped"
						_uiEvents.emit(UiEvents.ShowToast("Charcteristic Notifcation $message"))
					}
				},
				onFailure = { err ->
					_uiEvents.emit(UiEvents.ShowToast(err.message ?: "Cannot perform action"))
				}
			)
		}
	}

	private fun readBLEDescriptor(descriptor: BLEDescriptorModel) {

		val characteristic = _selectedCharacteristic.value.characteristic ?: return
		val service = _selectedCharacteristic.value.service ?: return

		val results = bleConnector.readDescriptor(
			service = service,
			characteristic = characteristic,
			descriptor = descriptor
		)

		results.onFailure { err ->
			viewModelScope.launch {
				_uiEvents.emit(
					UiEvents.ShowSnackBar(err.message ?: "Cannot perform action")
				)
			}
		}
	}


	private fun onReadBLECharactetistics() {

		val characteristic = _selectedCharacteristic.value.characteristic ?: return
		val service = _selectedCharacteristic.value.service ?: return

		val results = bleConnector.read(
			service = service,
			characteristic = characteristic
		)

		results.onFailure { err ->
			viewModelScope.launch {
				_uiEvents.emit(
					UiEvents.ShowSnackBar(err.message ?: "Cannot perform read operation")
				)
			}
		}
	}

	override fun onCleared() {
		// close the ble connection
		bleConnector.close()
		// clear the viewmodel
		super.onCleared()
	}
}
