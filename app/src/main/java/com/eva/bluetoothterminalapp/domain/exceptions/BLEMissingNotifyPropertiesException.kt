package com.eva.bluetoothterminalapp.domain.exceptions

class BLEMissingNotifyPropertiesException :
	Exception("Missing properties indication or notify, these are required to allow BLE notify or indication")