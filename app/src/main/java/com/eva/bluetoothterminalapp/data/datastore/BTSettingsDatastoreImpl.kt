package com.eva.bluetoothterminalapp.data.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.eva.bluetoothterminalapp.data.mapper.toModel
import com.eva.bluetoothterminalapp.data.mapper.toProto
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalCharSet
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalDisplayMode
import com.eva.bluetoothterminalapp.domain.settings.enums.BTTerminalNewLineChar
import com.eva.bluetoothterminalapp.domain.settings.models.BTSettingsModel
import com.eva.bluetoothterminalapp.domain.settings.repository.BTSettingsDataSore
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

class BTSettingsDatastoreImpl(
	private val context: Context
) : BTSettingsDataSore {

	override val settingsFlow: Flow<BTSettingsModel>
		get() = context.btClassicSettings.data.map(BluetoothClassicSettings::toModel)

	override suspend fun getSettings(): BTSettingsModel {
		return withContext(Dispatchers.IO) { settingsFlow.first() }
	}

	override suspend fun onCharsetChange(charSet: BTTerminalCharSet) {
		context.btClassicSettings.updateData { prefs ->
			prefs.copy {
				charset = charSet.toProto
			}
		}
	}

	override suspend fun onShowTimestampChange(isChange: Boolean) {
		context.btClassicSettings.updateData { prefs ->
			prefs.copy {
				showTimeStamp = isChange
			}
		}
	}

	override suspend fun onDisplayModeChange(mode: BTTerminalDisplayMode) {
		context.btClassicSettings.updateData { prefs ->
			prefs.copy {
				displayMode = mode.toProto
			}
		}
	}

	override suspend fun onNewLineCharChangeForReceive(newLineChar: BTTerminalNewLineChar) {
		context.btClassicSettings.updateData { prefs ->
			prefs.copy {
				newLineForReceive = newLineChar.toProto
			}
		}
	}

	override suspend fun onNewLineCharChangeForSend(newLineChar: BTTerminalNewLineChar) {
		context.btClassicSettings.updateData { prefs ->
			prefs.copy {
				newLineForSend = newLineChar.toProto
			}
		}
	}

	override suspend fun onLocalEchoValueChange(isLocalEcho: Boolean) {
		context.btClassicSettings.updateData { prefs ->
			prefs.copy {
				localEcho = isLocalEcho
			}
		}
	}

	override suspend fun onClearInputOnSendValueChange(canClear: Boolean) {
		context.btClassicSettings.updateData { prefs ->
			prefs.copy {
				clearInputOnSend = canClear
			}
		}
	}

	override suspend fun onKeepScreenOnConnectedValueChange(isKeepScreenOn: Boolean) {
		context.btClassicSettings.updateData { prefs ->
			prefs.copy {
				keepScreenOnWhenConnected = isKeepScreenOn
			}
		}
	}

	override suspend fun onAutoScrollValueChange(isEnabled: Boolean) {
		context.btClassicSettings.updateData { prefs ->
			prefs.copy {
				autoScrollTerminal = isEnabled
			}
		}
	}

}

private val Context.btClassicSettings by dataStore(
	fileName = DatastoreConstants.BT_CLASSIC_SETTINGS_FILE_NAME,
	serializer = object : Serializer<BluetoothClassicSettings> {

		override val defaultValue: BluetoothClassicSettings =
			BluetoothClassicSettings.getDefaultInstance()

		override suspend fun readFrom(input: InputStream): BluetoothClassicSettings {
			try {
				return BluetoothClassicSettings.parseFrom(input)
			} catch (exception: InvalidProtocolBufferException) {
				throw CorruptionException("Cannot read .proto file", exception)
			}
		}

		override suspend fun writeTo(t: BluetoothClassicSettings, output: OutputStream) =
			t.writeTo(output)
	},
)