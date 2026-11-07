package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.util.BTConstants
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import java.util.UUID

@Composable
fun ConnectionProfileList(
	selected: UUID?,
	available: ImmutableList<UUID>,
	modifier: Modifier = Modifier,
	onAddCustomUUID: (String) -> Unit = {},
	isDiscovering: Boolean = false,
	onUUIDSelect: (UUID) -> Unit = {},
	customUuids: ImmutableList<UUID> = persistentListOf(),
	contentPadding: PaddingValues = PaddingValues(0.dp),
) {
	val isNoUuidFound by remember(available) {
		derivedStateOf(available::isEmpty)
	}

	val isLocalInspectionMode = LocalInspectionMode.current

	val uuidsKey: ((Int, UUID) -> Any)? = remember {
		if (isLocalInspectionMode) null else { _, uuid -> uuid }
	}

	val uuidContentType: ((Int, UUID) -> Any?) = remember {
		{ _, _ -> UUID::class.simpleName }
	}

	LazyColumn(
		modifier = modifier,
		contentPadding = contentPadding,
		verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
	) {
		stickyHeader {
			ListItem(
				content = { Text(text = stringResource(id = R.string.bl_connect_profile_server_uuid_text)) },
				supportingContent = { Text(text = stringResource(id = R.string.bl_connect_profile_server_uuid_desc)) },
			)
		}
		item(contentType = UUID::class.simpleName) {
			SelectableUUIDCard(
				uuid = BTConstants.SERVICE_UUID,
				uniqueName = stringResource(id = R.string.bl_connect_profile_server_uuid),
				isSelected = selected == BTConstants.SERVICE_UUID,
				onSelect = { onUUIDSelect(BTConstants.SERVICE_UUID) },
				fontFamily = FontFamily.Default,
				shape = ListItemDefaults.segmentedShapes(0, 1),
				modifier = Modifier.fillMaxWidth()
			)
			Spacer(modifier = Modifier.height(8.dp))
		}
		stickyHeader {
			ListItem(
				content = { Text(text = stringResource(id = R.string.bl_connect_profile_custom_connect_uuid)) },
				supportingContent = { Text(text = stringResource(id = R.string.bl_connect_profile_custom_connect_uuid_desc)) },
			)
		}
		itemsIndexed(
			items = customUuids,
			key = uuidsKey,
			contentType = uuidContentType
		) { idx, uuid ->
			SelectableUUIDCard(
				uuid = uuid,
				isSelected = selected == uuid,
				shape = ListItemDefaults.segmentedShapes(idx, available.size),
				onSelect = { onUUIDSelect(uuid) },
				modifier = Modifier.fillMaxWidth(),
			)
		}
		// custom spp uuid
		item {
			CustomSPPUuidReader(onAddCustomUuid = onAddCustomUUID)
			Spacer(modifier = Modifier.height(8.dp))
		}
		stickyHeader {
			ListItem(
				content = { Text(text = stringResource(id = R.string.bl_connect_profile_device_uuid_text)) },
				supportingContent = { Text(text = stringResource(R.string.bl_connect_profile_device_uuid_desc)) },
			)
		}

		if (isDiscovering) {
			item {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(80.dp)
						.animateItem(),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = stringResource(id = R.string.bl_connect_profile_device_uuid_fetching),
						style = MaterialTheme.typography.bodyMediumEmphasized,
						color = MaterialTheme.colorScheme.secondary,
						textAlign = TextAlign.Center,
					)
				}
			}
		} else if (isNoUuidFound) {
			item {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(80.dp)
						.animateItem(),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = stringResource(id = R.string.bl_connect_profile_device_uuids_not_found),
						style = MaterialTheme.typography.bodyMediumEmphasized,
						color = MaterialTheme.colorScheme.secondary,
						textAlign = TextAlign.Center,
					)
				}
			}
		} else itemsIndexed(
			items = available,
			key = uuidsKey,
			contentType = uuidContentType
		) { idx, uuid ->
			SelectableUUIDCard(
				uuid = uuid,
				isSelected = selected == uuid,
				shape = ListItemDefaults.segmentedShapes(idx, available.size),
				onSelect = { onUUIDSelect(uuid) },
				modifier = Modifier.fillMaxWidth(),
			)
		}
	}
}

private class IsDiscoveringBooleanParams :
	CollectionPreviewParameterProvider<Boolean>(listOf(true, false))

@PreviewLightDark
@Composable
private fun ConnectionProfileListPreview(
	@PreviewParameter(IsDiscoveringBooleanParams::class)
	isDiscovering: Boolean
) = BlueToothTerminalAppTheme {
	Surface {
		ConnectionProfileList(
			selected = BTConstants.SERVICE_UUID,
			available = PreviewFakes.FAKE_UUID_LIST.toImmutableList(),
			isDiscovering = isDiscovering,
			modifier = Modifier.fillMaxSize()
		)
	}
}