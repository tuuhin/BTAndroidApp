package com.eva.bluetoothterminalapp.domain.exceptions

class BLEServiceAndCharacteristicMatchNotFoundException :
	Exception("Invalid Service or characteristic for the connected device")