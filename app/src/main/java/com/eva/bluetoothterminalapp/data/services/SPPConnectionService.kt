package com.eva.bluetoothterminalapp.data.services

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.utils.NotificationConstants
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class SPPConnectionService : LifecycleService() {

	private val _connector: BluetoothClientConnector by inject()
	private val _notificationManager by lazy { applicationContext.getSystemService<NotificationManager>() }

	private var commandJob: Job? = null

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		super.onStartCommand(intent, flags, startId)

		val address = intent?.getStringExtra(SPPConnectionConstants.EXTRA_DEVICE_ADDRESS)

		// show foreground with notification
		val message = if (address != null) "Preparing connection to $address"
		else "Preparing connection"
		startForeground(NOTIFICATION_ID, buildNotification(message))

		// check spp uuid
		val uuid = runCatching {
			val uuidString = intent?.getStringExtra(SPPConnectionConstants.EXTRA_SPP_UUID)
				?: throw IllegalArgumentException("Missing spp_uuid")
			Uuid.parse(uuidString)
		}.getOrElse {
			Log.w(TAG, "FAILED TO PARSE SPP UUID STOPING SERVICE")
			stopSelf(startId)
			return START_NOT_STICKY
		}

		// read in the payload
		val payload = intent?.getByteArrayExtra(SPPConnectionConstants.EXTRA_PAYLOAD)
		val payloadHex = intent?.getStringExtra(SPPConnectionConstants.EXTRA_PAYLOAD_HEX)
		val finalPayload = try {
			when {
				payload != null -> payload
				!payloadHex.isNullOrBlank() -> SPPConnectionConstants.hexToByteArray(payloadHex)
				else -> throw IllegalArgumentException("Missing payload")
			}.also { require(it.isNotEmpty()) { "Payload must not be empty" } }
		} catch (e: IllegalArgumentException) {
			Log.w(TAG, "FAILED TO PARSE IN THE PAYLOAD", e)
			stopSelf(startId)
			return START_NOT_STICKY
		}


		try {
			if (commandJob?.isActive == true) {
				Log.w(TAG, "PREVIOUS CONNECTION WAS ALIVE NEED CANCELLING IT")
				commandJob?.cancel()
			}
			commandJob = lifecycleScope.launch {
				// close the client if connected
				_connector.closeClient()
				// create a new connection
				val connected =
					_connector.connectClient(address.orEmpty(), uuid.toJavaUuid(), secure = true)
				if (connected.isFailure) {
					Log.w(TAG, "FAILED TO MAKE A CONNECTION STOPING SERVICE")
					showFailure(connected.exceptionOrNull())
					stopSelf(startId)
					return@launch
				}

				val isSendSuccess = _connector.sendBytes(finalPayload)
				if (isSendSuccess.isFailure || !isSendSuccess.getOrDefault(false)) {
					Log.w(TAG, "FAILED TO SEND PAYLOAD TO THE CLIENT")
					Log.w(TAG, "CLOSING CONNECTION")
					showFailure(isSendSuccess.exceptionOrNull())

					_connector.closeClient()
					stopSelf(startId)
					return@launch
				}
				// send payload success
				_notificationManager?.notify(NOTIFICATION_ID, buildNotification("Payload sent"))
			}
		} catch (e: Exception) {
			Log.e(TAG, "Something went south stoping service", e)

			// detach the foreground notification so it stays
			_notificationManager?.notify(NOTIFICATION_ID, buildNotification("Failed to connect"))
			stopForeground(STOP_FOREGROUND_DETACH)

			stopSelf(startId)
			return START_NOT_STICKY
		}
		// not sticky so system will not restart if there is a process death
		return START_NOT_STICKY
	}

	override fun onDestroy() {
		Log.w(TAG, "CANCELING JOB AND CLOSING CLIENT")
		commandJob?.cancel()
		_connector.closeClient()
		super.onDestroy()
	}

	private fun showFailure(error: Throwable?) {
		Log.e(TAG, "SPP CONNECTION FAILED", error)

		val failedMessage = error?.message ?: "Failed to connect to bluetooth client"
		_notificationManager?.notify(NOTIFICATION_ID, buildNotification(failedMessage))
	}

	private fun buildNotification(message: String): Notification =
		NotificationCompat.Builder(this, NotificationConstants.BT_CONNECTION_NOTIFICATION_ID)
			.setSmallIcon(R.drawable.ic_bluetooth_settings)
			.setContentTitle(getString(R.string.app_name))
			.setContentText(message)
			.setOngoing(true)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.build()

	companion object {
		private const val TAG = "SPPConnectionService"
		private const val NOTIFICATION_ID = 7101
	}
}