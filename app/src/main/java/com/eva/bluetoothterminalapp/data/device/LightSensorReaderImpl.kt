package com.eva.bluetoothterminalapp.data.device

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.domain.device.LightSensorReader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

private const val TAG = "LIGHT_SENSOR_READER"

class LightSensorReaderImpl(private val context: Context) : LightSensorReader {

	private val _sensorManager by lazy { context.getSystemService<SensorManager>() }

	private val _isSensorAvailable: Boolean
		get() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT)

	private val _lightSensor: Sensor?
		get() = _sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)


	@OptIn(ExperimentalCoroutinesApi::class)
	override suspend fun readCurrentValue(): Float? {
		return suspendCancellableCoroutine { cont ->
			if (!_isSensorAvailable) {
				cont.resume(null, onCancellation = {})
				return@suspendCancellableCoroutine
			}
			val sensor = _lightSensor ?: run {
				cont.resume(null, onCancellation = {})
				return@suspendCancellableCoroutine
			}

			val listener = object : SensorEventListener {
				override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
				override fun onSensorChanged(event: SensorEvent?) {
					val lux = event?.values?.firstOrNull()
					if (cont.isActive) cont.resume(lux, onCancellation = {})
					Log.d(TAG, "LIGHT SENSOR LISTENER UN-REGISTERED")
					_sensorManager?.unregisterListener(this)
				}
			}

			Log.d(TAG, "LIGHT SENSOR LISTENER REGISTERED")
			_sensorManager?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

			cont.invokeOnCancellation {
				Log.d(TAG, "LIGHT SENSOR LISTENER UN-REGISTERED (CANCELLED)")
				_sensorManager?.unregisterListener(listener)
			}
		}

	}

	override fun readValuesFlow(): Flow<Float> {
		val sensor = _lightSensor ?: return flowOf(0f)
		if (!_isSensorAvailable) return flowOf(0f)
		return callbackFlow {

			val listener = object : SensorEventListener {
				override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
				override fun onSensorChanged(event: SensorEvent?) {
					if (event?.sensor?.type != Sensor.TYPE_LIGHT) return
					event.values.firstOrNull()?.let { value -> trySend(value) }
				}
			}

			Log.d(TAG, "LIGHT SENSOR LISTENER REGISTERED")
			_sensorManager?.registerListener(
				listener,
				sensor,
				1.seconds.toInt(DurationUnit.MICROSECONDS)
			)
			awaitClose {
				Log.d(TAG, "LIGHT SENSOR LISTENER UNREGISTERED")
				_sensorManager?.unregisterListener(listener)
			}
		}
	}
}