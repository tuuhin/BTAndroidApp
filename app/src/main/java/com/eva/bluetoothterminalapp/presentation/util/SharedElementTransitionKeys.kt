package com.eva.bluetoothterminalapp.presentation.util

object SharedElementTransitionKeys {

	fun leDeviceCardToLeDeviceProfile(address: String) =
		"shared-bounds-card-to-profile-ble-$address"

	const val SETTINGS_ITEM_TO_SETTING_SCREEN = "shared-bounds-settings-button-to-settings-screen"
	const val ABOUT_ITEM_TO_ABOUT_SCREEN = "shared-bounds-about-button-to-about-screen"
	const val CLASSIC_SERVER_ITEM_TO_SERVER =
		"shared-bounds-drawer-item-classic-server-to-server-screen"
	const val BLE_SERVER_ITEM_TO_SERVER = "shared-bounds-drawer-item-ble-server-to-server-screen"

	fun btClientScreen(address: String) = "shared-bounds-bt-client-connect-$address"
	fun btProfileScreen(address: String) = "shared-bounds-bt-client-profile-$address"
}