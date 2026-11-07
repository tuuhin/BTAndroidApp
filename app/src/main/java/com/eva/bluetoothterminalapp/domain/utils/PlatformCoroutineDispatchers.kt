package com.eva.bluetoothterminalapp.domain.utils

import kotlinx.coroutines.CoroutineDispatcher

interface PlatformCoroutineDispatchers {
	val main: CoroutineDispatcher
	val mainImmediate: CoroutineDispatcher
	val io: CoroutineDispatcher
	val default: CoroutineDispatcher
	val unconfined: CoroutineDispatcher
}