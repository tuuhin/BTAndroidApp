package com.eva.bluetoothterminalapp.data.ble_server

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattService

@SuppressLint("MissingPermission")
class BLEServerServiceQueue(private val server: BluetoothGattServer) {

	private val _servicesQueue = mutableListOf<BluetoothGattService>()
	private var _onCompleteCallback: (() -> Unit)? = null

	fun addServices(services: List<BluetoothGattService>, onComplete: () -> Unit) {
		_servicesQueue.clear()
		_servicesQueue.addAll(services)
		_onCompleteCallback = onComplete
		addNextService()
	}

	fun addNextService() {
		if (_servicesQueue.isEmpty()) _onCompleteCallback?.invoke()
		else server.addService(_servicesQueue.removeAt(0))
	}
}