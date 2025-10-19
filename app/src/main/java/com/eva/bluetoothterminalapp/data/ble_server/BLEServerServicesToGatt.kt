package com.eva.bluetoothterminalapp.data.ble_server

import android.bluetooth.BluetoothGattService
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPermission
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEPropertyTypes
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServerServices
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServicesTypes

val BLEServerServices.toBLEService: BluetoothGattService
	get() = when (this) {
		BLEServerServices.DEVICE_SERVICE -> bleServiceOf(
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

		BLEServerServices.ECHO_SERVICE -> bleServiceOf(
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
					uuid = BLEServerUUID.CCC_DESCRIPTOR,
					permissions = listOf(
						BLEPermission.PERMISSION_READ,
						BLEPermission.PERMISSION_WRITE
					)
				)
				addDescriptor(descriptor)
			}
			addCharacteristic(characteristic)
		}

		BLEServerServices.UART_SERVICE -> bleServiceOf(
			uuid = BLEServerUUID.NORDIC_UART_SERVICE,
			serviceType = BLEServicesTypes.PRIMARY
		).apply {
			val rxCharacteristic = bleCharacteristicsOf(
				uuid = BLEServerUUID.NUS_RX_CHARACTERISTIC,
				properties = listOf(
					BLEPropertyTypes.PROPERTY_WRITE,
					BLEPropertyTypes.PROPERTY_WRITE_NO_RESPONSE
				),
				permissions = listOf(BLEPermission.PERMISSION_WRITE)
			)

			val txCharacteristic = bleCharacteristicsOf(
				uuid = BLEServerUUID.NUS_TX_CHARACTERISTIC,
				properties = listOf(
					BLEPropertyTypes.PROPERTY_NOTIFY
				),
				permissions = listOf(BLEPermission.PERMISSION_READ)
			).apply {
				val descriptor = bleDescriptorOf(
					uuid = BLEServerUUID.CCC_DESCRIPTOR,
					permissions = listOf(
						BLEPermission.PERMISSION_READ,
						BLEPermission.PERMISSION_WRITE
					)
				)
				addDescriptor(descriptor)
			}

			addCharacteristic(rxCharacteristic)
			addCharacteristic(txCharacteristic)
		}

		BLEServerServices.BATTERY_LEVEL_SERVICE -> bleServiceOf(
			uuid = BLEServerUUID.BATTERY_SERVICE,
			serviceType = BLEServicesTypes.PRIMARY
		).apply {
			val batteryLevelCharacteristics = bleCharacteristicsOf(
				uuid = BLEServerUUID.BATTERY_LEVEL_CHARACTERISTIC,
				properties = listOf(
					BLEPropertyTypes.PROPERTY_READ,
					BLEPropertyTypes.PROPERTY_NOTIFY
				),
				permissions = listOf(BLEPermission.PERMISSION_READ)
			).apply {
				val descriptor = bleDescriptorOf(
					uuid = BLEServerUUID.CCC_DESCRIPTOR,
					permissions = listOf(
						BLEPermission.PERMISSION_READ,
						BLEPermission.PERMISSION_WRITE
					)
				)
				addDescriptor(descriptor)
			}
			addCharacteristic(batteryLevelCharacteristics)
		}

		BLEServerServices.ENVIRONMENT_SENSING_SERVICE -> bleServiceOf(
			uuid = BLEServerUUID.ENVIRONMENTAL_SENSING_SERVICE,
			serviceType = BLEServicesTypes.PRIMARY
		).apply {
			val illuminanceCharacteristics = bleCharacteristicsOf(
				uuid = BLEServerUUID.ILLUMINANCE_CHARACTERISTIC,
				properties = listOf(
					BLEPropertyTypes.PROPERTY_NOTIFY,
					BLEPropertyTypes.PROPERTY_READ
				),
				permissions = listOf(BLEPermission.PERMISSION_READ)
			).apply {
				val descriptor = bleDescriptorOf(
					uuid = BLEServerUUID.CCC_DESCRIPTOR,
					permissions = listOf(
						BLEPermission.PERMISSION_READ,
						BLEPermission.PERMISSION_WRITE
					)
				)
				addDescriptor(descriptor)
			}
			addCharacteristic(illuminanceCharacteristics)
		}
	}