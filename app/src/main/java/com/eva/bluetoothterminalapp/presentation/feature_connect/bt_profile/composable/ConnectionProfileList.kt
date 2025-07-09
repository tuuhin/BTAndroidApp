package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.util.BTConstants
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConnectionProfileList(
	selected: UUID?,
	available: ImmutableList<UUID>,
	modifier: Modifier = Modifier,
	isDiscovering: Boolean = false,
	onUUIDSelect: (UUID) -> Unit = {},
	contentPadding: PaddingValues = PaddingValues(0.dp),
	verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp)
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
		verticalArrangement = verticalArrangement
	) {
		stickyHeader {
			ListItem(
				headlineContent = { Text(text = stringResource(id = R.string.bl_connect_profile_server_uuid_text)) },
				supportingContent = { Text(text = stringResource(id = R.string.bl_connect_profile_server_uuid_desc)) },
			)
		}
		item(contentType = UUID::class.simpleName) {
			SelectableUUIDCard(
				uuid = BTConstants.SERVICE_UUID,
				specialName = stringResource(id = R.string.bl_connect_profile_server_uuid),
				isSelected = selected == BTConstants.SERVICE_UUID,
				onSelect = { onUUIDSelect(BTConstants.SERVICE_UUID) },
				modifier = Modifier.fillMaxWidth()
			)
		}
		stickyHeader {
			ListItem(
				headlineContent = { Text(text = stringResource(id = R.string.bl_connect_profile_device_uuid_text)) },
				supportingContent = { Text(text = stringResource(R.string.bl_connect_profile_device_uuid_desc)) },
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
						.animateItem(),
				)
			}
		} else if (isNoUuidFound) {
			item {
				Text(
					text = stringResource(id = R.string.bl_connect_profile_device_uuids_not_found),
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.animateItem(),
				)
			}
		} else itemsIndexed(
			items = available,
			key = uuidsKey,
			contentType = uuidContentType
		) { _, uuid ->
			SelectableUUIDCard(
				uuid = uuid,
				isSelected = selected == uuid,
				onSelect = { onUUIDSelect(uuid) },
				modifier = Modifier.fillMaxWidth(),
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun ConnectionProfileListPreview() = BlueToothTerminalAppTheme {
	Surface {
		ConnectionProfileList(
			selected = BTConstants.SERVICE_UUID,
			available = PreviewFakes.FAKE_UUID_LIST.toImmutableList()
		)
	}
}