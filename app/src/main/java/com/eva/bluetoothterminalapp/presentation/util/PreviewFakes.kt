package com.eva.bluetoothterminalapp.presentation.util

import com.eva.bluetoothterminalapp.domain.bluetooth.enums.BluetoothDeviceType
import com.eva.bluetoothterminalapp.domain.bluetooth.enums.BluetoothMode
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEConnectionState
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEWriteTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEDescriptorModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLEServiceModel
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BluetoothLEDeviceModel
import com.eva.bluetoothterminalapp.domain.settings.enums.BLEScanPeriodTimmings
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsScanMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BLESettingsSupportedLayer
import com.eva.bluetoothterminalapp.domain.settings.models.BLESettingsModel
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.state.BTProfileScreenState
import com.eva.bluetoothterminalapp.presentation.feature_devices.state.BTDevicesScreenState
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLEDeviceProfileState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID


object PreviewFakes {

	val ANDROID_NAME_AS_BYTEARRAY = byteArrayOf(
		0x4A,
		0x65,
		0x74,
		0x70,
		0x61,
		0x63,
		0x6B,
		0x20,
		0x43,
		0x6F,
		0x6D,
		0x70,
		0x6F,
		0x73,
		0x65
	)


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
		serviceType = BLEServicesTypes.PRIMARY
	)

	val FAKE_SERVICE_WITH_CHARACTERISTICS = BLEServiceModel(
		serviceId = 1,
		serviceUUID = UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a"),
		serviceType = BLEServicesTypes.SECONDARY
	).apply {
		characteristic = persistentListOf<BLECharacteristicsModel>(
			FAKE_BLE_CHARACTERISTIC_MODEL,
			FAKE_BLE_CHARACTERISTIC_MODEL_WITH_DATA
		)
	}

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

	val FAKE_BLE_DESCRIPTOR_MODEL_WITH_VALUE = BLEDescriptorModel(
		uuid = UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a"),
		permissions = listOf(BLEPermission.PERMISSION_READ),
		byteArray = byteArrayOf(0x4B, 0x6F, 0x6C, 0x74, 0x69, 0x6E)
	)

	val FAKE_BLE_DESCRIPTOR_WITH_ENABLE_NOTIFICATION_VALUE = BLEDescriptorModel(
		uuid = UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a"),
		permissions = listOf(BLEPermission.PERMISSION_READ),
		byteArray = byteArrayOf(0x00, 0x00)
	)

	val FAKE_BLE_CHARACTERISTIC_MODEL = BLECharacteristicsModel(
		instanceId = 1,
		uuid = UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a"),
		permission = BLEPermission.PERMISSION_WRITE,
		writeType = BLEWriteTypes.TYPE_UNKNOWN,
		properties = listOf(
			BLEPropertyTypes.PROPERTY_WRITE,
			BLEPropertyTypes.PROPERTY_READ,
			BLEPropertyTypes.PROPERTY_INDICATE,
			BLEPropertyTypes.PROPERTY_NOTIFY

		),
		descriptors = listOf(FAKE_BLE_DESCRIPTOR_MODEL).toPersistentList()
	).apply {
		probableName = "Compose"

	}

	val FAKE_BLE_CHARACTERISTIC_MODEL_WITH_DATA = BLECharacteristicsModel(
		instanceId = 1,
		uuid = UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a"),
		permission = BLEPermission.PERMISSION_WRITE,
		writeType = BLEWriteTypes.TYPE_UNKNOWN,
		properties = listOf(
			BLEPropertyTypes.PROPERTY_WRITE,
			BLEPropertyTypes.PROPERTY_READ,
			BLEPropertyTypes.PROPERTY_INDICATE,

			),
		descriptors = listOf(
			FAKE_BLE_DESCRIPTOR_MODEL,
			FAKE_BLE_DESCRIPTOR_MODEL_WITH_VALUE
		).toPersistentList(),
		byteArray = ANDROID_NAME_AS_BYTEARRAY
	).apply {
		probableName = "Compose"

	}

	val FAKE_UUID_LIST = List(10) {
		UUID.fromString("10297702-35bd-4fda-a904-1e693390e08a")
	}

	val FAKE_BT_DEVICE_PROFILE = BTProfileScreenState(
		isDiscovering = false,
		deviceUUIDS = FAKE_UUID_LIST.toImmutableList()
	)

	val FAKE_BLE_SETTINGS = BLESettingsModel(
		scanPeriod = BLEScanPeriodTimmings.FIVE_MINUTES,
		supportedLayer = BLESettingsSupportedLayer.LONG_RANGE,
		isLegacyOnly = true
	)

	val FAKE_BLE_SETTINGS_2 = BLESettingsModel(
		scanPeriod = BLEScanPeriodTimmings.TWELVE_SECONDS,
		supportedLayer = BLESettingsSupportedLayer.ALL,
		scanMode = BLESettingsScanMode.LOW_POWER,
		isLegacyOnly = false
	)

	val FAKE_BT_SETTINGS = BTSettingsModel()
}