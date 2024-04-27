package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data class ConnectProfileState(
	val showProfileDialog: Boolean = true,
	val isDiscovering: Boolean = true,
	val selectedUUID: UUID? = null,
	val deviceUUIDS: ImmutableList<UUID> = persistentListOf()
)