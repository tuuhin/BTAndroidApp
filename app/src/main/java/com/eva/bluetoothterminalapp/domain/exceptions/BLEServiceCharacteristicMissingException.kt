package com.eva.bluetoothterminalapp.domain.exceptions

class BLEServiceCharacteristicMissingException :
	Exception("Service or characteristics missing for the device")