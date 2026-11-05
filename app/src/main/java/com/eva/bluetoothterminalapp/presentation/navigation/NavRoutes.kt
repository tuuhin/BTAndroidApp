package com.eva.bluetoothterminalapp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
sealed interface NavRoutes : NavKey {

	@Serializable
	data object DevicesRoute : NavRoutes

	@Serializable
	data class DeviceProfileRoute(
		val address: String,
		val name: String? = null,
	) : NavRoutes

	@Serializable
	data class ClassicClientRoute(
		val address: String,
		val uuid: Uuid,
	) : NavRoutes

	@Serializable
	data class BLEClientRoute(
		val address: String,
		val name: String? = null,
	) : NavRoutes

	@Serializable
	data object ClassicServerRoute : NavRoutes

	@Serializable
	data object BLEServerRoute : NavRoutes

	@Serializable
	data object SettingsRoute : NavRoutes

	@Serializable
	data object AboutRoute : NavRoutes
}