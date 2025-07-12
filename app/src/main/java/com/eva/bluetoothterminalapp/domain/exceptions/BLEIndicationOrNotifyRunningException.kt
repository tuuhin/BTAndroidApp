package com.eva.bluetoothterminalapp.domain.exceptions

class BLEIndicationOrNotifyRunningException :
	Exception("Some of characteristics are using notify and indication turn it off to continue")