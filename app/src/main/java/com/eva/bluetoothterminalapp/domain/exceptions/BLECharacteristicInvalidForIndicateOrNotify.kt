package com.eva.bluetoothterminalapp.domain.exceptions

class BLECharacteristicInvalidForIndicateOrNotify :
	Exception("Cannot have indication/notification for this characteristic")