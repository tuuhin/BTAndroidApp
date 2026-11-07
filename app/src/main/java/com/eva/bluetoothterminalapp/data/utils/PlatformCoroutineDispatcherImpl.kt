package com.eva.bluetoothterminalapp.data.utils

import com.eva.bluetoothterminalapp.domain.utils.PlatformCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PlatformCoroutineDispatcherImpl : PlatformCoroutineDispatchers {

	override val main: CoroutineDispatcher
		get() = Dispatchers.Main

	override val mainImmediate: CoroutineDispatcher
		get() = Dispatchers.Main.immediate

	override val io: CoroutineDispatcher
		get() = Dispatchers.IO

	override val default: CoroutineDispatcher
		get() = Dispatchers.Default

	override val unconfined: CoroutineDispatcher
		get() = Dispatchers.Unconfined
}