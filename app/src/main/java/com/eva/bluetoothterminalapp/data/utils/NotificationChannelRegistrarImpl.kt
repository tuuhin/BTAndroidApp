package com.eva.bluetoothterminalapp.data.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import com.eva.bluetoothterminalapp.domain.utils.NotificationChannelRegistrar

class NotificationChannelRegistrarImpl(
	private val context: Context
) : NotificationChannelRegistrar {

	private val _manager by lazy { context.getSystemService<NotificationManager>() }

	override fun registerChannels() {
		_manager?.createNotificationChannels(listOf(createNotificationChannel()))
	}

	private fun createNotificationChannel(): NotificationChannel {
		return NotificationChannel(
			NotificationConstants.BT_CONNECTION_NOTIFICATION_ID,
			NotificationConstants.BT_CONNECTION_NOTIFICATION_CHANNEL_NAME,
			NotificationManager.IMPORTANCE_DEFAULT,
		).apply {
			setSound(null, null)
			lockscreenVisibility = Notification.VISIBILITY_PUBLIC
			description = NotificationConstants.BT_CONNECTION_NOTIFICATION_DESCRIPTION
		}
	}

}