package com.eva.bluetoothterminalapp.presentation.feature_le_connect

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.bluetoothterminalapp.domain.bluetooth_le.BluetoothLEClientConnector
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEConnectionEvents
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLECharacteristicEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLEDeviceConfigEvent
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLEDeviceProfileState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.CharacteristicWriteDialogState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.CloseConnectionEvents
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.SelectedCharacteristicState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.WriteCharacteristicEvent
import com.eva.bluetoothterminalapp.presentation.navigation.args.BluetoothDeviceArgs
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import com.ramcosta.composedestinations.generated.navArgs
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
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
		transform = ::readCharacteristics
	).onStart {
		initiateConnection()
		observeBLEEvents()
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(10_000),
		initialValue = null
	)

	private val deviceRssi = bleConnector.connEvents
		.filterIsInstance<BLEConnectionEvents.OnRSSIUpdated>()
		.map { it.rssi }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = 0
		)

	val bLEProfile = combine(
		deviceRssi,
		bleConnector.bleServices,
		bleConnector.connectionState,
	) { rssi, services, connectState ->
		BLEDeviceProfileState(
			connectionState = connectState,
			device = bleConnector.connectedDevice,
			signalStrength = rssi,
			services = services.toImmutableList()
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(2000),
		initialValue = BLEDeviceProfileState()
	)

	private val _showCloseConnectionDialog = MutableStateFlow(false)
	val showConnectionDialog = _showCloseConnectionDialog.asStateFlow()


	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	private val navArgs: BluetoothDeviceArgs
		get() = savedStateHandle.navArgs()

	private val isNotifyOrIndicationRunning: Boolean
		get() = bleConnector.isNotifyOrIndicationRunning.value


	fun onCharacteristicEvent(event: BLECharacteristicEvent) {
		when (event) {
			is BLECharacteristicEvent.OnSelectCharacteristic -> _selectedCharacteristic.update { selected ->
				selected.copy(service = event.service, characteristic = event.characteristics)
			}

			BLECharacteristicEvent.OnUnSelectCharacteristic -> onUnSelectCharacteristics()
			is BLECharacteristicEvent.OnDescriptorRead -> readBLEDescriptor(event.desc)
			BLECharacteristicEvent.OnIndicateCharacteristic -> onIndicateOrNotifyBLECharacteristic()
			BLECharacteristicEvent.OnNotifyCharacteristic -> onIndicateOrNotifyBLECharacteristic()
			BLECharacteristicEvent.ReadCharacteristic -> onReadBLECharacteristics()
			BLECharacteristicEvent.WriteCharacteristic -> onWriteEvent(WriteCharacteristicEvent.OpenDialog)
			BLECharacteristicEvent.OnStopNotifyOrIndication -> stopIndications()
		}
	}

	fun onWriteEvent(event: WriteCharacteristicEvent) {
		when (event) {
			WriteCharacteristicEvent.CloseDialog -> _writeDialogState.update { state ->
				state.copy(showDialog = false)
			}

			is WriteCharacteristicEvent.OnTextFieldValueChange -> _writeDialogState.update { state ->
				state.copy(textFieldValue = event.value)
			}

			WriteCharacteristicEvent.OpenDialog -> _writeDialogState.update { selected ->
				selected.copy(showDialog = true)
			}

			WriteCharacteristicEvent.WriteCharacteristicValue -> onWriteBLECharacteristic()
		}
	}


	fun onConfigEvents(event: BLEDeviceConfigEvent) {
		when (event) {
			BLEDeviceConfigEvent.OnReadRssiStrength -> onRefreshRSSI()
			BLEDeviceConfigEvent.OnReDiscoverServices -> onRefreshServices()
			BLEDeviceConfigEvent.OnDisconnectEvent -> bleConnector.disconnect()
			BLEDeviceConfigEvent.OnReconnectEvent -> onClientReconnect()
			is BLEDeviceConfigEvent.OnUpdateMTU -> onUpdateMTU(event.newValue)
		}
	}

	fun onCloseConnectionEvent(event: CloseConnectionEvents) {
		when (event) {
			CloseConnectionEvents.CancelCloseConnection -> _showCloseConnectionDialog.update { false }
			CloseConnectionEvents.ShowCloseConnectionDialog -> _showCloseConnectionDialog.update { true }
			CloseConnectionEvents.ConfirmCloseDialog -> viewModelScope.launch {
				bleConnector.close()
				_uiEvents.emit(UiEvents.NavigateBack)
			}
		}
	}

	private fun onClientReconnect() = viewModelScope.launch {
		val result = bleConnector.reconnect()
		val message = if (result.isSuccess) "Reconnecting" else "Failed"
		_uiEvents.emit(UiEvents.ShowToast(message))
	}

	private fun stopIndications() = onIndicateOrNotifyBLECharacteristic(false)

	private fun onUnSelectCharacteristics() {
		// turn this off
		if (isNotifyOrIndicationRunning) stopIndications()

		_selectedCharacteristic.update { SelectedCharacteristicState() }
	}

	private fun readCharacteristics(
		characteristic: BLECharacteristicsModel?,
		selected: SelectedCharacteristicState,
		isSetNotificationActive: Boolean,
	): BLECharacteristicsModel? {
		// if not selected there is nothing to read to
		if (selected.characteristic == null) return null
		if (characteristic == null)
			return selected.characteristic.copy(isSetNotificationActive = isSetNotificationActive)
		val isSameCharacteristic = characteristic.uuid == selected.characteristic.uuid
				&& characteristic.instanceId == selected.characteristic.instanceId
		// any of the reader is started match the uuids to check for data
		val outResult = if (isSameCharacteristic) characteristic else selected.characteristic
		return outResult.copy(isSetNotificationActive = isSetNotificationActive)
	}

	private fun initiateConnection() {
		val address = navArgs.address
		// being connection
		viewModelScope.launch {
			bleConnector.connect(address)
		}
	}

	private fun onWriteBLECharacteristic() {

		val characteristic = _selectedCharacteristic.value.characteristic ?: return
		val service = _selectedCharacteristic.value.service ?: return
		val value = _writeDialogState.value.textFieldValue

		if (value.isBlank()) {
			_writeDialogState.update { state -> state.copy(errorText = "Cant send blank value") }
			return
		}

		val result = bleConnector.write(service, characteristic, value = value)

		result.fold(
			onFailure = { err ->
				val message = err.message ?: "Cannot perform write"
				val event = UiEvents.ShowSnackBar(message)
				viewModelScope.launch { _uiEvents.emit(event) }
			},
			onSuccess = {
				_writeDialogState.update { state ->
					state.copy(textFieldValue = "", showDialog = false)
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

		val event = if (results.isSuccess) {
			val message = if (isStart) "enabled" else "stopped"
			UiEvents.ShowToast("Characteristic Notification $message")
		} else {
			val error = results.exceptionOrNull()
			val message = error?.message ?: "problem with starting notification or indication"
			UiEvents.ShowSnackBar(message)
		}

		viewModelScope.launch { _uiEvents.emit(event) }
	}

	private fun readBLEDescriptor(descriptor: BLEDescriptorModel) {

		val characteristic = _selectedCharacteristic.value.characteristic ?: return
		val service = _selectedCharacteristic.value.service ?: return

		val results = bleConnector.readDescriptor(
			service = service,
			characteristic = characteristic,
			descriptor = descriptor
		)

		results.onFailure { error ->
			val error = error.message ?: "Cannot perform read operations"
			val uiEvent = UiEvents.ShowSnackBar(error)
			viewModelScope.launch { _uiEvents.emit(uiEvent) }
		}
	}


	private fun onReadBLECharacteristics() {

		val characteristic = _selectedCharacteristic.value.characteristic ?: return
		val service = _selectedCharacteristic.value.service ?: return

		val results = bleConnector.read(
			service = service,
			characteristic = characteristic
		)

		results.onFailure { error ->
			val error = error.message ?: "Cannot perform read operations"
			val uiEvent = UiEvents.ShowSnackBar(error)
			viewModelScope.launch { _uiEvents.emit(uiEvent) }
		}
	}

	private fun onRefreshRSSI() = viewModelScope.launch {
		val result = bleConnector.checkRssi()
		if (result.isSuccess) return@launch

		val error = result.exceptionOrNull()?.message ?: "Cannot perform refresh"
		_uiEvents.emit(UiEvents.ShowSnackBar(error))

	}

	private fun onRefreshServices() = viewModelScope.launch {
		val result = bleConnector.discoverServices()
		if (result.isSuccess) return@launch

		val error = result.exceptionOrNull()?.message ?: "Cannot perform refresh"
		_uiEvents.emit(UiEvents.ShowSnackBar(error))
	}

	private fun onUpdateMTU(unit: Int) = viewModelScope.launch {
		val result = bleConnector.onUpdateMTU(unit)
		if (result.isSuccess) return@launch

		val error = result.exceptionOrNull()?.message ?: "Cannot update mtu"
		_uiEvents.emit(UiEvents.ShowSnackBar(error))
	}

	private fun observeBLEEvents() {

		bleConnector.connEvents.onEach { event ->
			val message = when (event) {
				is BLEConnectionEvents.OnMTUUpdated -> "Device MTU updated :${event.mtu}"
				is BLEConnectionEvents.OnPhyUpdated -> "Device phy updated"
				is BLEConnectionEvents.OnRSSIUpdated -> "Device RSSI updated"
			}
			_uiEvents.emit(UiEvents.ShowToast(message))
		}.launchIn(viewModelScope)
	}

	override fun onCleared() {
		bleConnector.close()
	}
}
