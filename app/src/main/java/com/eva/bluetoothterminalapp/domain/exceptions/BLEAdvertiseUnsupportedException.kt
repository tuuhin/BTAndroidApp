package com.eva.bluetoothterminalapp.domain.exceptions

class BLEAdvertiseUnsupportedException :
	Exception("Cannot start multiple ble server advertisements")