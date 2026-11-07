package com.eva.bluetoothterminalapp.presentation.feature_settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors
import com.eva.bluetoothterminalapp.presentation.util.BTConstants
import com.eva.bluetoothterminalapp.presentation.util.SharedElementTransitionKeys
import com.eva.bluetoothterminalapp.presentation.util.sharedBoundsWrapper
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme


@Composable
fun AboutRoute(
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {}
) {
	val context = LocalContext.current
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = {
					Text(text = stringResource(id = R.string.about_route_title))
				},
				navigationIcon = navigation,
				scrollBehavior = scrollBehavior
			)
		},
		modifier = modifier
			.nestedScroll(scrollBehavior.nestedScrollConnection)
			.sharedBoundsWrapper(SharedElementTransitionKeys.ABOUT_ITEM_TO_ABOUT_SCREEN)
	) { scPadding ->
		LazyColumn(
			contentPadding = scPadding,
			verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = dimensionResource(id = R.dimen.sc_padding)),
		) {
			item {
				Text(
					text = stringResource(id = R.string.information_title_bluetooth_and_ble),
					style = MaterialTheme.typography.titleMediumEmphasized,
					color = MaterialTheme.colorScheme.secondary,
					modifier = Modifier.padding(6.dp)
				)
			}
			item {
				SegmentedListItem(
					shapes = ListItemDefaults.segmentedShapes(index = 0, count = 2),
					colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceContainer),
					leadingContent = {
						Icon(
							imageVector = AppIconVectors.speed_3,
							contentDescription = stringResource(id = R.string.information_baud_rate)
						)
					},
					supportingContent = { Text(text = stringResource(id = R.string.information_baud_rate_desc)) },
					content = { Text(text = stringResource(id = R.string.information_baud_rate)) }
				)
			}

			item {
				SegmentedListItem(
					shapes = ListItemDefaults.segmentedShapes(index = 1, count = 2),
					colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceContainer),
					trailingContent = {
						TextButton(
							onClick = context::openAssignedNumbersList,
							shapes = ButtonDefaults.shapes()
						) {
							Text(text = stringResource(id = R.string.info_visit_link))
						}
					},
					leadingContent = {
						Icon(
							imageVector = AppIconVectors.numbers,
							contentDescription = stringResource(id = R.string.ble_assigned_numbers_title)
						)
					},
					supportingContent = { Text(text = stringResource(id = R.string.ble_assigned_numbers_desc)) },
					content = { Text(text = stringResource(id = R.string.ble_assigned_numbers_title)) }
				)
				Spacer(modifier = Modifier.height(12.dp))
			}
			item {
				Text(
					text = stringResource(id = R.string.information_title_development),
					style = MaterialTheme.typography.titleMediumEmphasized,
					color = MaterialTheme.colorScheme.secondary,
					modifier = Modifier.padding(6.dp)
				)
			}
			item {
				SegmentedListItem(
					shapes = ListItemDefaults.segmentedShapes(index = 0, count = 2),
					colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceContainer),
					trailingContent = {
						TextButton(
							onClick = context::openGithub,
							shapes = ButtonDefaults.shapes()
						) {
							Text(text = stringResource(id = R.string.info_visit_github))
						}
					},
					leadingContent = {
						Icon(
							painter = painterResource(id = R.drawable.ic_github),
							contentDescription = stringResource(id = R.string.info_visit_github)
						)
					},
					supportingContent = { Text(text = stringResource(id = R.string.information_source_code_desc)) },
					content = { Text(text = stringResource(id = R.string.information_source_code)) }
				)
			}
			item {
				SegmentedListItem(
					shapes = ListItemDefaults.segmentedShapes(index = 1, count = 2),
					colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceContainer),
					trailingContent = {
						TextButton(
							onClick = context::sendEmailToMe,
							shapes = ButtonDefaults.shapes()
						) {
							Text(text = stringResource(id = R.string.mail_title))
						}
					},
					leadingContent = {
						Icon(
							imageVector = AppIconVectors.email,
							contentDescription = stringResource(id = R.string.contacts)
						)
					},
					supportingContent = { Text(text = stringResource(id = R.string.contracts_with_email)) },
					content = { Text(text = stringResource(id = R.string.contacts)) }
				)
			}
		}
	}
}

private fun Context.openAssignedNumbersList() {
	val uri = BTConstants.BLE_ASSIGNED_NUMBERS_WEBSITE.toUri()
	val error = getString(R.string.cannot_launch_activity)
	try {
		Intent(Intent.ACTION_VIEW).apply {
			data = uri
			startActivity(this@apply)
		}
	} catch (_: ActivityNotFoundException) {
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
	}
}

private fun Context.openGithub() {
	val uri = BTConstants.SOURCE_CODE_REPO.toUri()
	val error = getString(R.string.cannot_launch_activity)
	try {
		Intent(Intent.ACTION_VIEW).apply {
			data = uri
			startActivity(this@apply)
		}
	} catch (_: ActivityNotFoundException) {
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
	}
}

private fun Context.sendEmailToMe() {
	val errorText = getString(R.string.cannot_find_email_clients)
	val chooserTitle = getString(R.string.send_email_via)

	try {
		val intent = Intent(Intent.ACTION_SENDTO).apply {
			data = "mailto:".toUri()
			putExtra(Intent.EXTRA_EMAIL, arrayOf("tuhinbhowmick2513@gmail.com"))
			putExtra(Intent.EXTRA_SUBJECT, "Query regarding BluetoothAndroid App")
			putExtra(Intent.EXTRA_TEXT, "Query")
		}
		val chooser = Intent.createChooser(intent, chooserTitle)
		startActivity(chooser)

	} catch (_: ActivityNotFoundException) {
		Toast.makeText(this, errorText, Toast.LENGTH_SHORT)
			.show()
	}
}

@PreviewLightDark
@Composable
private fun InformationRoutePreview() = BlueToothTerminalAppTheme {
	AboutRoute(
		navigation = {
			Icon(
				imageVector = AppIconVectors.backArrow,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		},
	)
}