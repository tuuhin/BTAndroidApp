package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.state.BLECharacteristicEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLECharacteristicSheet(
	isExpanded: Boolean,
	characteristic: BLECharacteristicsModel?,
	onEvent: (BLECharacteristicEvent) -> Unit,
	modifier: Modifier = Modifier,
	sheetState: SheetState = rememberBottomSheetState(
		initialValue = SheetValue.Hidden,
		enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
	),
) {

	val scope = rememberCoroutineScope()

	if (!isExpanded) return

	ModalBottomSheet(
		sheetState = sheetState,
		onDismissRequest = {
			scope.launch { sheetState.hide() }
				.invokeOnCompletion { onEvent(BLECharacteristicEvent.OnUnSelectCharacteristic) }
		},
		tonalElevation = 4.dp,
		modifier = modifier
	) {
		BLEReadWriteContentSheet(
			characteristic = characteristic,
			onDismissSheet = {
				scope.launch { sheetState.hide() }
					.invokeOnCompletion { onEvent(BLECharacteristicEvent.OnUnSelectCharacteristic) }
			},
			onEvent = onEvent,
			modifier = Modifier.padding(dimensionResource(id = R.dimen.sc_padding))
		)
	}
}
