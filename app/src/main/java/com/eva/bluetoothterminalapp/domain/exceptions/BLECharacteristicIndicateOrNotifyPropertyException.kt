package com.eva.bluetoothterminalapp.domain.exceptions

class BLECharacteristicIndicateOrNotifyPropertyException :
	Exception("Missing properties indication or notify, these are required to allow BLE notify or indication")