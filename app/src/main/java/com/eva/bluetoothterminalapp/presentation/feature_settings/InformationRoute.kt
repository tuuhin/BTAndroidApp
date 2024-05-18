package com.eva.bluetoothterminalapp.presentation.feature_settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationRoute(
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {}
) {
	val context = LocalContext.current
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = { Text(text = stringResource(id = R.string.information_route_title)) },
				navigationIcon = navigation,
				scrollBehavior = scrollBehavior
			)
		},
		modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
	) { scPadding ->
		LazyColumn(
			contentPadding = scPadding,
			modifier = Modifier.padding(
				horizontal = dimensionResource(id = R.dimen.sc_padding)
			), verticalArrangement = Arrangement.spacedBy(8.dp)
		) {

			item {
				Text(
					text = stringResource(id = R.string.information_title_bluetooth_and_ble),
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier.padding(6.dp)
				)
			}
			item {
				ListItem(
					headlineContent = { Text(text = stringResource(id = R.string.information_baud_rate)) },
					supportingContent = {
						Text(
							text = stringResource(id = R.string.information_baud_rate_desc),
							style = MaterialTheme.typography.labelLarge
						)
					},
					leadingContent = {
						Icon(
							imageVector = Icons.Default.Speed,
							contentDescription = stringResource(id = R.string.information_baud_rate)
						)
					},
					tonalElevation = 2.dp,
					modifier = Modifier.clip(MaterialTheme.shapes.small)
				)
			}

			item {
				ListItem(
					headlineContent = { Text(text = stringResource(id = R.string.ble_assigned_numbers_title)) },
					supportingContent = {
						Text(
							text = stringResource(id = R.string.ble_assigned_numbers_desc),
							style = MaterialTheme.typography.labelLarge
						)
					},
					leadingContent = {
						Icon(
							imageVector = Icons.Outlined.PictureAsPdf,
							contentDescription = stringResource(id = R.string.ble_assigned_numbers_title)
						)
					},
					trailingContent = {
						TextButton(onClick = context::openAssignedNumbersList) {
							Text(text = stringResource(id = R.string.info_visit_link))
						}
					},
					tonalElevation = 2.dp,
					modifier = Modifier.clip(MaterialTheme.shapes.small),
				)
			}
			item {
				Text(
					text = stringResource(id = R.string.information_title_development),
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier.padding(6.dp)
				)
			}
			item {
				ListItem(
					headlineContent = { Text(text = stringResource(id = R.string.information_source_code)) },
					supportingContent = {
						Text(
							text = stringResource(id = R.string.information_source_code_desc),
							style = MaterialTheme.typography.labelLarge
						)
					},
					leadingContent = {
						Icon(
							painter = painterResource(id = R.drawable.ic_github),
							contentDescription = stringResource(id = R.string.info_visit_github)
						)
					},
					trailingContent = {
						TextButton(onClick = context::openGithub) {
							Text(text = stringResource(id = R.string.info_visit_github))
						}
					},
					tonalElevation = 2.dp,
					modifier = Modifier.clip(MaterialTheme.shapes.small)
				)
			}
			item {
				ListItem(
					headlineContent = { Text(text = "Contact") },
					supportingContent = {
						Text(
							text = "Contact developer with email"
						)
					},
					leadingContent = {
						Icon(
							imageVector = Icons.Outlined.Mail,
							contentDescription = null
						)
					},
					trailingContent = {
						TextButton(onClick = context::sendEmailToMe) {
							Text(text = "Mail")
						}
					},
					tonalElevation = 2.dp,
					modifier = Modifier.clip(MaterialTheme.shapes.small)
				)
			}
		}
	}
}

private fun Context.openAssignedNumbersList() {
	val uri = getString(R.string.information_ble_assign_url)
	val error = getString(R.string.cannot_launch_activity)
	try {
		Intent(Intent.ACTION_VIEW).apply {
			data = Uri.parse(uri)
			startActivity(this@apply)
		}
	} catch (_: ActivityNotFoundException) {
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
	}
}

private fun Context.openGithub() {
	val uri = getString(R.string.information_source_code_link)
	val error = getString(R.string.cannot_launch_activity)
	try {
		Intent(Intent.ACTION_VIEW).apply {
			data = Uri.parse(uri)
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
			data = Uri.parse("mailto:")
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
	InformationRoute(
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Default.ArrowBack,
				contentDescription = stringResource(id = R.string.back_arrow)
			)
		},
	)
}