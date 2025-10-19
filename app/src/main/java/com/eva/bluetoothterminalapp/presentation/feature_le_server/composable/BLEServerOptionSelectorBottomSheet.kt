package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth_le.enums.BLEServerServices
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
	sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {

	if (!showSheet) return

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = sheetState,
		modifier = modifier
	) {
		BLEServerServicesSelectorContent(
			selectedList = selectedList,
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
	contentPadding: PaddingValues = PaddingValues(12.dp)
) {
	Column(
		modifier = modifier.padding(contentPadding),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Text(
			text = stringResource(R.string.ble_server_services_selector_title),
			style = MaterialTheme.typography.headlineMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Text(
			text = stringResource(R.string.ble_server_services_selector_text),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Spacer(modifier = Modifier.height(4.dp))
		BLEServerServices.entries.forEach { entry ->
			val isSelected = entry in selectedList
			val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
			else MaterialTheme.colorScheme.surfaceContainerHigh

			key(entry) {
				Surface(
					color = containerColor,
					onClick = { onSelectUnSelectOption(entry) },
					contentColor = contentColorFor(containerColor),
					shape = MaterialTheme.shapes.large,
					modifier = Modifier.semantics {
						role = Role.Checkbox
					}
				) {
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 12.dp, vertical = 6.dp),
						horizontalArrangement = Arrangement.spacedBy(12.dp),
						verticalAlignment = Alignment.CenterVertically,
					) {
						Checkbox(
							checked = isSelected,
							onCheckedChange = { onSelectUnSelectOption(entry) },
						)
						Column(
							verticalArrangement = Arrangement.spacedBy(2.dp),
							modifier = Modifier.weight(1f)
						) {
							Text(
								text = entry.titleText,
								style = MaterialTheme.typography.titleSmall
							)
							Text(
								text = entry.descText,
								style = MaterialTheme.typography.bodySmall
							)
						}
					}
				}
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
			contentPadding = PaddingValues(20.dp)
		)
	}
}