package com.eva.bluetoothterminalapp.presentation.feature_le_connect.util

interface CloseConnectionEvents {

	data object ShowCloseConnectionDialog : CloseConnectionEvents

	data object ConfirmCloseDialog : CloseConnectionEvents

	data object CancelCloseConnection : CloseConnectionEvents
}