package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_client.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionModeTopBar(
	canConnect: Boolean,
	onConnect: () -> Unit,
	onCancel: () -> Unit,
	modifier: Modifier = Modifier,
	colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
	scrollBehavior: TopAppBarScrollBehavior? = null
) {
	TopAppBar(
		title = { Text(text = stringResource(id = R.string.bl_connect_profile_title)) },
		navigationIcon = {
			IconButton(
				onClick = onCancel
			) {
				Icon(
					imageVector = Icons.AutoMirrored.Default.ArrowBack,
					contentDescription = null
				)
			}
		},
		actions = {
			AnimatedVisibility(
				visible = canConnect,
				enter = slideInVertically(),
				exit = slideOutVertically()
			) {
				TextButton(onClick = onConnect) {
					Text(text = stringResource(id = R.string.connect_to_client))
				}
			}
		},
		colors = colors,
		modifier = modifier,
		scrollBehavior = scrollBehavior
	)
}