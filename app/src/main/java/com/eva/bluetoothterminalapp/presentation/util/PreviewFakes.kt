package com.eva.bluetoothterminalapp.presentation.util

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEWriteTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.models.BluetoothDeviceType
import com.eva.bluetoothterminalapp.domain.models.BluetoothMode
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.state.BTProfileScreenState
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID


object PreviewFakes {


	val FAKE_DEVICE_MODEL = BluetoothDeviceModel(
		name = "Android HeadSet",
		address = "00:11:22:33:FF:EE",
		mode = BluetoothMode.BLUETOOTH_DEVICE_CLASSIC,
		type = BluetoothDeviceType.AUDIO_VIDEO
	)

	val FAKE_BLE_DEVICE_MODEL = BluetoothLEDeviceModel(
		deviceName = "Android Headset", deviceModel = FAKE_DEVICE_MODEL
	)


	val FAKE_DEVICE_STATE_WITH_NO_DEVICE = BTDevicesScreenState()

	val FAKE_DEVICE_STATE_WITH_PAIRED_DEVICE = BTDevicesScreenState(
		pairedDevices = List(3) { FAKE_DEVICE_MODEL }.toPersistentList()
	)

	val FAKE_DEVICE_STATE_WITH_PAIRED_AND_AVAILABLE_DEVICES = BTDevicesScreenState(
		pairedDevices = List(3) { FAKE_DEVICE_MODEL }.toPersistentList(),
		availableDevices = List(2) { FAKE_DEVICE_MODEL }.toPersistentList()
	)

	val FAKE_DEVICE_STATE_WITH_SOME_BLE_DEVICES = BTDevicesScreenState(
		leDevices = List(2) { FAKE_BLE_DEVICE_MODEL }.toPersistentList()
	)

	val FAKE_BLE_SERVICE = BLEServiceModel(
		serviceId = 1,
		serviceUUID = UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a"),
		serviceType = BLEServicesTypes.UNKNOWN
	)

	val FAKE_BLE_PROFILE_STATE = BLEDeviceProfileState(
		device = FAKE_DEVICE_MODEL,
		connectionState = BLEConnectionState.CONNECTED,
		signalStrength = 100,
		services = List(4) { FAKE_BLE_SERVICE }.toImmutableList()
	)

	val FAKE_BLE_PROFILE_STATE_CONNECTING = BLEDeviceProfileState(
		device = FAKE_DEVICE_MODEL,
		connectionState = BLEConnectionState.CONNECTING,
		signalStrength = 100,
	)

	val FAKE_BLE_DESCRIPTOR_MODEL = BLEDescriptorModel(
		uuid = UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a"),
		permissions = listOf(BLEPermission.PERMISSION_WRITE),
	)

	val FAKE_BLE_CHARACTERISTIC_MODEL = BLECharacteristicsModel(
		characteristicsId = 1,
		uuid = UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a"),
		permission = BLEPermission.PERMISSION_WRITE,
		writeType = BLEWriteTypes.TYPE_UNKNOWN,
		properties = listOf(
			BLEPropertyTypes.PROPERTY_WRITE,
			BLEPropertyTypes.PROPERTY_READ,
			BLEPropertyTypes.PROPERTY_INDICATE,
			BLEPropertyTypes.PROPERTY_NOTIFY
		),

		).apply {
		probableName = "Compose"
		descriptors = listOf(FAKE_BLE_DESCRIPTOR_MODEL).toPersistentList()
	}

	val FAKE_BLE_CHAR_MODEL_SECOND = BLECharacteristicsModel(
		characteristicsId = 1,
		uuid = UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a"),
		permission = BLEPermission.PERMISSION_WRITE,
		writeType = BLEWriteTypes.TYPE_NO_RESPONSE,
		properties = listOf(BLEPropertyTypes.PROPERTY_WRITE),

		).apply {
		probableName = "Compose 2"
	}


	val FAKE_UUID_LIST = List(10) {
		UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a")
	}

	val FAKE_BT_DEVICE_PROFILE = BTProfileScreenState(
		isDiscovering = false,
		deviceUUIDS = FAKE_UUID_LIST.toImmutableList()
	)
}