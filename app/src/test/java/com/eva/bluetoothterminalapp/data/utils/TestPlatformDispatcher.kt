package com.eva.bluetoothterminalapp.data.utils

import com.eva.bluetoothterminalapp.domain.utils.PlatformCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestPlatformDispatcher(
	private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()
) : PlatformCoroutineDispatchers {

	override val default: CoroutineDispatcher
		get() = testDispatcher

	override val io: CoroutineDispatcher
		get() = testDispatcher

	override val main: CoroutineDispatcher
		get() = testDispatcher

	override val mainImmediate: CoroutineDispatcher
		get() = testDispatcher

	override val unconfined: CoroutineDispatcher
		get() = testDispatcher
}