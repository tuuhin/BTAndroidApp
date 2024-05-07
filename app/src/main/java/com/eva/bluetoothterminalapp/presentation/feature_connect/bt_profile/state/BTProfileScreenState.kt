package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data class BTProfileScreenState(
	val deviceUUIDS: ImmutableList<UUID> = persistentListOf(),
	val isDiscovering: Boolean = true,
)