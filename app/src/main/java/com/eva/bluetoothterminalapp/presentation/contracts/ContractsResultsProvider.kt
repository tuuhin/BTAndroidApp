package com.eva.bluetoothterminalapp.presentation.contracts

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.MainThread
import com.eva.bluetoothterminalapp.R
import org.koin.core.annotation.Factory

@Factory
class ContractsResultsProvider(private val context: Context) {

	@MainThread
	fun handleBluetoothDiscovery(result: Int) {
		val message = when (result) {
			Activity.RESULT_CANCELED -> context.getString(R.string.device_discoverable_denied)
			else -> context.getString(R.string.device_discoverable_success, result)
		}
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
	}
}