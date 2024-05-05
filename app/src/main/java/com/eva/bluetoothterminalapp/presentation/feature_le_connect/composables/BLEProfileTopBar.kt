package com.eva.bluetoothterminalapp.presentation.feature_le_connect.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BLEProfileTopBar(
	onSelect: () -> Unit,
	modifier: Modifier = Modifier,
	showAction: Boolean = false,
	scrollConnection: TopAppBarScrollBehavior? = null,
	navigation: @Composable () -> Unit = {},
) {
	TopAppBar(
		title = { Text(text = stringResource(id = R.string.ble_device_profile)) },
		actions = {
			AnimatedVisibility(
				visible = showAction,
				enter = slideInVertically(),
				exit = slideOutVertically()
			) {
				IconButton(onClick = onSelect) {
					Icon(
						imageVector = Icons.Default.Check,
						contentDescription = null
					)
				}
			}
		},
		scrollBehavior = scrollConnection,
		navigationIcon = navigation,
		modifier = modifier
	)
}