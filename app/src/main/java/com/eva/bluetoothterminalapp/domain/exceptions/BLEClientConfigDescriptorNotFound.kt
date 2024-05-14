package com.eva.bluetoothterminalapp.domain.exceptions

class BLEClientConfigDescriptorNotFound :
	Exception("Indications and Notification are written on client characterisitc config descriptor which is not found")