package com.eva.bluetoothterminalapp.data.ble_server

import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes

val bleDeviceInfoService = bleServiceOf(
	uuid = BLEServerUUID.DEVICE_INFO_SERVICE,
	serviceType = BLEServicesTypes.PRIMARY,
).apply {
	listOf(
		BLEServerUUID.MANUFACTURER_NAME,
		BLEServerUUID.MODEL_NUMBER,
		BLEServerUUID.HARDWARE_REVISION,
		BLEServerUUID.SOFTWARE_REVISION,
	).forEach { uid ->
		val char = bleCharacteristicsOf(
			uuid = uid,
			properties = listOf(BLEPropertyTypes.PROPERTY_READ),
			permissions = listOf(BLEPermission.PERMISSION_READ)
		)
		addCharacteristic(char)
	}
}

val echoService = bleServiceOf(
	uuid = BLEServerUUID.ECHO_SERVICE,
	serviceType = BLEServicesTypes.SECONDARY
).apply {
	val characteristic = bleCharacteristicsOf(
		uuid = BLEServerUUID.ECHO_CHARACTERISTIC,
		properties = listOf(
			BLEPropertyTypes.PROPERTY_READ,
			BLEPropertyTypes.PROPERTY_WRITE,
			BLEPropertyTypes.PROPERTY_NOTIFY
		),
		permissions = listOf(BLEPermission.PERMISSION_READ, BLEPermission.PERMISSION_WRITE)
	).apply {
		val descriptor = bleDescriptorOf(
			uuid = BLEServerUUID.ECHO_DESCRIPTOR,
			permissions = listOf(BLEPermission.PERMISSION_READ, BLEPermission.PERMISSION_WRITE)
		)
		addDescriptor(descriptor)
	}
	addCharacteristic(characteristic)
}

val nordicUARTService = bleServiceOf(
	uuid = BLEServerUUID.NORDIC_UART_SERVICE,
	serviceType = BLEServicesTypes.PRIMARY
).apply {
	val rxCharacteristic = bleCharacteristicsOf(
		uuid = BLEServerUUID.NUS_RX_CHARACTERISTIC, properties = listOf(
			BLEPropertyTypes.PROPERTY_READ,
			BLEPropertyTypes.PROPERTY_WRITE,
			BLEPropertyTypes.PROPERTY_NOTIFY
		),
		permissions = listOf(BLEPermission.PERMISSION_READ, BLEPermission.PERMISSION_WRITE)
	).apply {
		val descriptor = bleDescriptorOf(
			uuid = BLEServerUUID.NUS_DESCRIPTOR,
			permissions = listOf(BLEPermission.PERMISSION_READ, BLEPermission.PERMISSION_WRITE)
		)
		addDescriptor(descriptor)
	}

	val txCharacteristic = bleCharacteristicsOf(
		uuid = BLEServerUUID.NUS_TX_CHARACTERISTIC,
		properties = listOf(
			BLEPropertyTypes.PROPERTY_READ,
			BLEPropertyTypes.PROPERTY_WRITE,
			BLEPropertyTypes.PROPERTY_NOTIFY
		),
		permissions = listOf(BLEPermission.PERMISSION_READ, BLEPermission.PERMISSION_WRITE)
	).apply {
		val descriptor = bleDescriptorOf(
			uuid = BLEServerUUID.NUS_DESCRIPTOR,
			permissions = listOf(BLEPermission.PERMISSION_READ, BLEPermission.PERMISSION_WRITE)
		)
		addDescriptor(descriptor)
	}

	addCharacteristic(rxCharacteristic)
	addCharacteristic(txCharacteristic)
}