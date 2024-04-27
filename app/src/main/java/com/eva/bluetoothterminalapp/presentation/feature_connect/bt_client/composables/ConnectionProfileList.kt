package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.bluetooth.BTConstants
import kotlinx.collections.immutable.ImmutableList
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConnectionProfileList(
	selectedUUID: UUID?,
	deviceUUIDs: ImmutableList<UUID>,
	modifier: Modifier = Modifier,
	isDiscovering: Boolean = false,
	onUUIDSelect: (UUID) -> Unit = {},
	contentPadding: PaddingValues = PaddingValues(0.dp),
) {
	val isUUIDListEmpty by remember(deviceUUIDs) {
		derivedStateOf(deviceUUIDs::isEmpty)
	}

	LazyColumn(
		modifier = modifier,
		contentPadding = contentPadding
	) {
		item {
			ListItem(
				headlineContent = { Text(text = stringResource(id = R.string.bl_connect_profile_server_uuid_text)) },
				supportingContent = {
					Text(
						text = stringResource(id = R.string.bl_connect_profile_server_uuid_desc),
						style = MaterialTheme.typography.labelMedium
					)
				},
			)
		}
		item(contentType = UUID::class.simpleName) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = dimensionResource(id = R.dimen.lazy_colum_content_padding)),
				horizontalArrangement = Arrangement.spacedBy(4.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				RadioButton(
					selected = selectedUUID == BTConstants.SERVICE_UUID,
					onClick = { onUUIDSelect(BTConstants.SERVICE_UUID) },
					colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.secondary)
				)
				Text(
					text = stringResource(id = R.string.bl_connect_profile_server_uuid),
					style = MaterialTheme.typography.labelLarge
				)
			}
		}
		item {
			ListItem(
				headlineContent = { Text(text = stringResource(id = R.string.bl_connect_profile_device_uuid_text)) },
				supportingContent = {
					Text(text = stringResource(R.string.bl_connect_profile_device_uuid_desc))
				},
			)
		}
		item {
			if (isDiscovering) {
				Text(
					text = stringResource(id = R.string.bl_connect_profile_device_uuid_fetching),
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier
						.fillMaxWidth()
						.animateItemPlacement(),
					textAlign = TextAlign.Center
				)
			} else if (isUUIDListEmpty) {
				Text(
					text = stringResource(id = R.string.bl_connect_profile_device_uuids_not_found),
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier
						.fillMaxWidth()
						.animateItemPlacement(),
					textAlign = TextAlign.Center
				)
			}
		}

		itemsIndexed(
			items = deviceUUIDs,
			key = { _, uuid -> uuid },
			contentType = { _, uuid -> uuid::class.simpleName },
		) { _, uuid ->
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = dimensionResource(id = R.dimen.lazy_colum_content_padding)),
				horizontalArrangement = Arrangement.spacedBy(4.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				RadioButton(
					selected = selectedUUID == uuid,
					onClick = { onUUIDSelect(uuid) },
					colors = RadioButtonDefaults
						.colors(selectedColor = MaterialTheme.colorScheme.secondary)
				)
				Text(
					text = "$uuid",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurface
				)
			}
		}
	}
}