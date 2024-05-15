package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.models.BLECharacteristicsModel
import com.eva.bluetoothterminalapp.presentation.feature_le_connect.util.BLECharacteristicEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLECharacteristicSheet(
	isExpanded: Boolean,
	characteristic: BLECharacteristicsModel?,
	onEvent: (BLECharacteristicEvent) -> Unit,
	modifier: Modifier = Modifier,
	sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
	if (!isExpanded) return

	ModalBottomSheet(
		sheetState = sheetState,
		containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
		onDismissRequest = { onEvent(BLECharacteristicEvent.OnUnSelectCharactetistic) },
		modifier = modifier
	) {
		BLEReadWriteContentSheet(
			characteristic = characteristic,
			onEvent = onEvent,
			modifier = Modifier.padding(dimensionResource(id = R.dimen.sc_padding))
		)
	}
}
