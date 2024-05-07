package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.bluetooth.BTConstants
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConnectionProfileList(
	selectedUUID: UUID?,
	foundUUIDs: ImmutableList<UUID>,
	modifier: Modifier = Modifier,
	isDiscovering: Boolean = false,
	onUUIDSelect: (UUID) -> Unit = {},
	contentPadding: PaddingValues = PaddingValues(0.dp),
) {
	val isNoUUIDFound by remember(foundUUIDs) {
		derivedStateOf(foundUUIDs::isEmpty)
	}

	val isLocalInspectionMode = LocalInspectionMode.current

	val uuidsKey: ((Int, UUID) -> Any)? = remember {
		if (isLocalInspectionMode) null
		else { _, uuid -> uuid }
	}

	LazyColumn(
		modifier = modifier,
		contentPadding = contentPadding
	) {
		item {
			ListItem(
				headlineContent = {
					Text(
						text = stringResource(id = R.string.bl_connect_profile_server_uuid_text),
						style = MaterialTheme.typography.bodyLarge
					)
				},
				supportingContent = {
					Text(
						text = stringResource(id = R.string.bl_connect_profile_server_uuid_desc),
						style = MaterialTheme.typography.labelMedium
					)
				},
			)
		}
		item(
			key = BTConstants.SERVICE_UUID,
			contentType = UUID::class.simpleName
		) {
			SelectableUUIDCard(
				uuid = BTConstants.SERVICE_UUID,
				namedUUID = stringResource(id = R.string.bl_connect_profile_server_uuid),
				isSelected = selectedUUID == BTConstants.SERVICE_UUID,
				onSelect = { onUUIDSelect(BTConstants.SERVICE_UUID) },
			)
		}
		item { HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.lazy_colum_content_padding))) }
		item {
			ListItem(
				headlineContent = {
					Text(
						text = stringResource(id = R.string.bl_connect_profile_device_uuid_text),
						style = MaterialTheme.typography.bodyLarge
					)
				},
				supportingContent = {
					Text(text = stringResource(R.string.bl_connect_profile_device_uuid_desc))
				},
			)
		}

		if (isDiscovering) {
			item {
				Text(
					text = stringResource(id = R.string.bl_connect_profile_device_uuid_fetching),
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.animateItemPlacement(),
				)
			}
		} else if (isNoUUIDFound) {
			item {
				Text(
					text = stringResource(id = R.string.bl_connect_profile_device_uuids_not_found),
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.animateItemPlacement(),
				)
			}
		} else itemsIndexed(
			items = foundUUIDs,
			key = uuidsKey,
			contentType = { _, _ -> UUID::class.simpleName },
		) { _, uuid ->
			SelectableUUIDCard(
				uuid = uuid,
				isSelected = selectedUUID == uuid,
				onSelect = { onUUIDSelect(uuid) },
				modifier = Modifier.fillMaxWidth()
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun ConnectionProfileListPreview() = BlueToothTerminalAppTheme {
	Surface {
		ConnectionProfileList(
			selectedUUID = null,
			foundUUIDs = PreviewFakes.FAKE_UUID_LIST.toImmutableList()
		)
	}
}