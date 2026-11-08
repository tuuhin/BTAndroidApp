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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class SPPConnectionService : LifecycleService() {

	private val _connector: BluetoothClientConnector by inject()
	private val _notificationManager by lazy { applicationContext.getSystemService<NotificationManager>() }
	private val _lock = Mutex()

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

		lifecycleScope.launch {
			try {
				// connect and send payload
				val result = connectAndSendPayload(address, uuid, finalPayload, startId)
				// in case we have an error
				if (result.isFailure) showFailure(startId, result.exceptionOrNull())
			} catch (e: CancellationException) {
				throw e
			} catch (e: Exception) {
				Log.e(TAG, "Something went south stoping service", e)
				// detach the foreground notification so it stays
				showFailure(startId, e)
			} finally {
				withContext(NonCancellable) {
					connectorCleanup(startId)
				}
			}
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


	private suspend fun connectAndSendPayload(
		address: String?,
		uuid: Uuid,
		finalPayload: ByteArray,
		startId: Int
	): Result<Unit> = _lock.withLock {
		// show a foreground message
		_notificationManager?.notify(NOTIFICATION_ID, buildNotification("Connecting to the client"))
		// close the client if connected
		_connector.closeClient()

		// create a new connection
		val connected = _connector
			.connectClient(address.orEmpty(), uuid.toJavaUuid(), secure = true)

		// in case we failed to connect
		if (connected.isFailure) {
			Log.w(TAG, "FAILED TO MAKE A CONNECTION STOPING SERVICE")
			return@withLock Result.failure(
				connected.exceptionOrNull()
					?: IllegalStateException("Should be connected state")
			)
		}
		val isSendSuccess = _connector.sendBytes(finalPayload)
		if (isSendSuccess.isFailure || !isSendSuccess.getOrDefault(false)) {
			_connector.closeClient()
			Log.w(TAG, "FAILED TO SEND PAYLOAD TO THE CLIENT")
			showFailure(startId, isSendSuccess.exceptionOrNull())
			return@withLock Result.failure(
				connected.exceptionOrNull()
					?: IllegalStateException("Unable to send data")
			)
		}
		_notificationManager?.notify(NOTIFICATION_ID, buildNotification("Payload sent"))
		Result.success(Unit)
	}

	private suspend fun connectorCleanup(startId: Int) = _lock.withLock {
		Log.i(TAG, "CLOSING CLIENT CONNECTION")
		_connector.closeClient()

		// remove the fg service
		if (stopSelfResult(startId)) {
			stopForeground(STOP_FOREGROUND_REMOVE)
		}
	}

	private fun showFailure(startId: Int, error: Throwable?) {
		Log.e(TAG, "SPP CONNECTION FAILED", error)

		val failedMessage = error?.message ?: "Failed to connect to bluetooth client"
		_notificationManager?.notify(NOTIFICATION_ID, buildNotification(failedMessage))

		// stop self and remove foreground
		if (stopSelfResult(startId))
			stopForeground(STOP_FOREGROUND_DETACH)
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