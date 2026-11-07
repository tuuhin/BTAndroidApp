package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServerServices
import com.eva.bluetoothterminalapp.presentation.composables.SheetTitleBar
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLEServerOptionSelectorBottomSheet(
	showSheet: Boolean,
	selectedList: ImmutableSet<BLEServerServices>,
	onSelectUnSelectOption: (BLEServerServices) -> Unit,
	modifier: Modifier = Modifier,
	onDismiss: () -> Unit = {},
	sheetState: SheetState = rememberBottomSheetState(
		initialValue = SheetValue.Hidden,
		enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
	)
) {

	if (!showSheet) return

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = sheetState,
		modifier = modifier
	) {
		BLEServerServicesSelectorContent(
			selectedList = selectedList,
			onDismiss = onDismiss,
			onSelectUnSelectOption = onSelectUnSelectOption,
			contentPadding = PaddingValues(
				start = dimensionResource(R.dimen.sc_padding),
				end = dimensionResource(R.dimen.sc_padding),
				bottom = dimensionResource(R.dimen.sc_padding_secondary)
			)
		)
	}
}

@Composable
private fun BLEServerServicesSelectorContent(
	selectedList: ImmutableSet<BLEServerServices>,
	onSelectUnSelectOption: (BLEServerServices) -> Unit,
	modifier: Modifier = Modifier,
	onDismiss: () -> Unit = {},
	contentPadding: PaddingValues = PaddingValues(12.dp)
) {
	Column(
		modifier = modifier.padding(contentPadding),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		SheetTitleBar(
			title = stringResource(R.string.ble_server_services_selector_title),
			onDismissSheet = onDismiss,
		)
		Text(
			text = stringResource(R.string.ble_server_services_selector_text),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
			contentPadding = PaddingValues(vertical = 4.dp)
		) {
			itemsIndexed(BLEServerServices.entries) { idx, entry ->
				SegmentedListItem(
					shapes = ListItemDefaults.segmentedShapes(
						idx,
						BLEServerServices.entries.size
					),
					checked = entry in selectedList,
					onCheckedChange = { onSelectUnSelectOption(entry) },
					trailingContent = {
						Checkbox(checked = entry in selectedList, onCheckedChange = null)
					},
					colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
					supportingContent = { Text(text = entry.descText) },
					content = { Text(text = entry.titleText) }
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun BLEServerServicesOptionsSelectorPreview() = BlueToothTerminalAppTheme {
	Surface(shape = BottomSheetDefaults.ExpandedShape) {
		BLEServerServicesSelectorContent(
			selectedList = persistentSetOf(BLEServerServices.ECHO_SERVICE),
			onSelectUnSelectOption = {},
			onDismiss = {},
			contentPadding = PaddingValues(20.dp)
		)
	}
}