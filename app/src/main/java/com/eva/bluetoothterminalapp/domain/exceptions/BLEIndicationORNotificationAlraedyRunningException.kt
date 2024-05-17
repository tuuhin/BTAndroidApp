package com.eva.bluetoothterminalapp.domain.exceptions

class BLEIndicationORNotificationAlraedyRunningException :
	Exception("Any of indicate or notify is already running cannot run both together")