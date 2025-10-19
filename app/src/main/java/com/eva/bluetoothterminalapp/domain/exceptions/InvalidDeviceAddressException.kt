package com.eva.bluetoothterminalapp.domain.exceptions

class InvalidDeviceAddressException :
	Exception("Invalid hardware address, must be upper case, in big endian byte order")
