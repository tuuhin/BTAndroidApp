package com.eva.bluetoothterminalapp.presentation.feature_connect.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothMessage
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BTMessagesList(
	messages: ImmutableList<BluetoothMessage>,
	modifier: Modifier = Modifier,
	showTimeInMessage: Boolean = true,
	lazyListState: LazyListState = rememberLazyListState(),
	isReversed: Boolean = false,
	scrollToEnd: Boolean = true,
	contentPadding: PaddingValues = PaddingValues(0.dp),
	verticalArrangement: Arrangement.Vertical = Arrangement.Bottom,
) {

	val isInspectionMode = LocalInspectionMode.current

	val listKeys: ((Int, BluetoothMessage) -> Any)? = remember {
		if (isInspectionMode) null
		else { _, message -> message.logTime.toEpochMilliseconds() }
	}

	LaunchedEffect(key1 = messages) {
		if (!scrollToEnd) return@LaunchedEffect
		// check if there is any item
		if (lazyListState.layoutInfo.totalItemsCount < 1) return@LaunchedEffect
		// otherwise animate to the end of the list
		lazyListState.animateScrollToItem(lazyListState.layoutInfo.totalItemsCount - 1)
	}


	LazyColumn(
		state = lazyListState,
		modifier = modifier,
		verticalArrangement = verticalArrangement,
		contentPadding = contentPadding,
		reverseLayout = isReversed,
	) {
		itemsIndexed(
			items = messages,
			key = listKeys,
			contentType = { _, _ -> BluetoothMessage::class.simpleName }
		) { _, message ->
			BTMessageText(
				message = message,
				showTime = showTimeInMessage,
				modifier = Modifier.padding(vertical = 2.dp)
			)
		}
	}
}

