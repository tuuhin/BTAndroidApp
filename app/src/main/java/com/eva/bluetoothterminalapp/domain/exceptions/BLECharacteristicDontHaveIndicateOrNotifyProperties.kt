package com.eva.bluetoothterminalapp.domain.exceptions

class BLECharacteristicDontHaveIndicateOrNotifyProperties :
	Exception("Cannot have indication/notification for this characteristic")