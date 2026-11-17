package com.eva.bluetoothterminalapp.data.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.utils.NotificationConstants
import com.eva.bluetoothterminalapp.domain.bluetooth.BluetoothClientConnector
import com.eva.bluetoothterminalapp.domain.utils.PlatformCoroutineDispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import kotlin.uuid.Uuid

class SPPConnectionService : LifecycleService() {

	private val _connector: BluetoothClientConnector by inject()
	private val _dispatchers: PlatformCoroutineDispatchers by inject()
	private val _notificationManager by lazy { applicationContext.getSystemService<NotificationManager>() }
	private val _lock = Mutex()

	// connector session maintainer
	private val session by lazy { SPPConnectionSession(_connector, _dispatchers) }

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		super.onStartCommand(intent, flags, startId)

		// start the foreground asap
		startForeground(NOTIFICATION_ID, buildNotification(isConnected = false))

		if (intent == null || intent.action !in validActions) {
			// stop the foreground and stop the service
			stopForeground(STOP_FOREGROUND_REMOVE)
			stopSelfResult(startId)
			return START_NOT_STICKY
		}

		val address = intent.getStringExtra(SPPConnectionConstants.EXTRA_DEVICE_ADDRESS) ?: ""
		val sppUuidString = intent.getStringExtra(SPPConnectionConstants.EXTRA_SPP_UUID) ?: ""
		val payload = intent.getByteArrayExtra(SPPConnectionConstants.EXTRA_PAYLOAD)
		val payloadHex = intent.getStringExtra(SPPConnectionConstants.EXTRA_PAYLOAD_HEX) ?: ""

		lifecycleScope.launch {
			try {
				_lock.withLock {
					when (intent.action) {
						SPPConnectionConstants.ACTION_SPP_DISCONNECT -> disconnect(startId)
						SPPConnectionConstants.ACTION_SPP_SEND -> {
							SPPConnectionConstants
								.validateArgs(address, sppUuidString, payload, payloadHex)

							val uuid = Uuid.parse(sppUuidString)
							val payload =
								payload ?: SPPConnectionConstants.hexToByteArray(payloadHex)
							connectAndSendPayload(address, uuid, payload)

							val notification = buildNotification(isConnected = true)
							_notificationManager?.notify(NOTIFICATION_ID, notification)
						}

						else -> failSession(startId, IllegalStateException("Invalid action"))
					}
				}
			} catch (e: Exception) {
				Log.e(TAG, "SPP command failed", e)
				// in case of any failed session set a failed session
				withContext(NonCancellable) { _lock.withLock { failSession(startId, e) } }
				// cancel so throw out the exception
				if (e is CancellationException) throw e
			}
		}
		// not sticky so system will not restart if there is a process death
		return START_NOT_STICKY
	}

	override fun onDestroy() {
		Log.w(TAG, "CLOSING SPP CLIENT")
		session.closeBlocking()
		super.onDestroy()
	}


	private suspend fun connectAndSendPayload(address: String, uuid: Uuid, payload: ByteArray) {
		val notification = buildNotification(isConnected = false)
		_notificationManager?.notify(NOTIFICATION_ID, notification)
		session.send(address, uuid, payload)
	}

	private suspend fun disconnect(startId: Int) {
		session.close()
		if (stopSelfResult(startId)) stopForeground(STOP_FOREGROUND_REMOVE)
	}

	private suspend fun failSession(startId: Int, error: Throwable) {
		session.close()

		val notification = buildNotification(isConnected = false)
		_notificationManager?.notify(NOTIFICATION_ID, notification)

		if (stopSelfResult(startId)) stopForeground(STOP_FOREGROUND_DETACH)
	}

	private fun buildNotification(isConnected: Boolean): Notification {
		val message = applicationContext.getString(
			if (isConnected) R.string.connection_accepted
			else R.string.connection_disconnected
		)

		val builder = NotificationCompat.Builder(
			this,
			NotificationConstants.BT_CONNECTION_NOTIFICATION_ID
		)
			.setSmallIcon(R.drawable.ic_bluetooth_settings)
			.setContentTitle(getString(R.string.app_name))
			.setContentText(message)
			.setOngoing(true)
			.setPriority(NotificationCompat.PRIORITY_HIGH)

		val pendingIntent = PendingIntent.getService(
			applicationContext,
			DISCONNECT_REQUEST_CODE,
			Intent(this, SPPConnectionService::class.java).apply {
				action = SPPConnectionConstants.ACTION_SPP_DISCONNECT
			},
			PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		)

		val action = NotificationCompat.Action.Builder(
			IconCompat.createWithResource(applicationContext, R.drawable.ic_disconnect),
			applicationContext.getString(R.string.disconnect_from_client),
			pendingIntent
		).build()

		builder.addAction(action)
		return builder.build()
	}


	companion object {
		private val validActions = arrayOf(
			SPPConnectionConstants.ACTION_SPP_DISCONNECT,
			SPPConnectionConstants.ACTION_SPP_SEND
		)
		private const val TAG = "SPPConnectionService"
		private const val NOTIFICATION_ID = 7101
		private const val DISCONNECT_REQUEST_CODE = 7102
	}
}