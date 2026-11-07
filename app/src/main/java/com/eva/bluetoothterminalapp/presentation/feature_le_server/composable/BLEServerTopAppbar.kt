package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R

@Composable
fun BLEServerTopAppBar(
	onStopServer: () -> Unit,
	modifier: Modifier = Modifier,
	isServerRunning: Boolean = false,
	navigation: @Composable () -> Unit = {},
	scrollBehavior: TopAppBarScrollBehavior? = null
) {
	MediumTopAppBar(
		title = { Text(text = stringResource(R.string.ble_server_title)) },
		actions = {
			AnimatedVisibility(
				visible = isServerRunning,
				enter = slideInVertically(),
				exit = slideOutVertically()
			) {
				AnimatedVisibility(
					visible = isServerRunning,
					enter = slideInVertically(),
					exit = slideOutVertically(),
					modifier = modifier
				) {
					FilledTonalButton(
						onClick = onStopServer,
						enabled = !transition.isRunning,
						contentPadding = ButtonDefaults.contentPaddingFor(
							ButtonDefaults.ExtraSmallContainerHeight,
							hasStartIcon = true
						),
						shapes = ButtonDefaults.shapes(
							pressedShape = ButtonDefaults.pressedShape,
							shape = ButtonDefaults.shape
						),
						colors = ButtonDefaults.buttonColors(
							containerColor = MaterialTheme.colorScheme.tertiaryContainer,
							contentColor = MaterialTheme.colorScheme.onTertiaryContainer
						)
					)
					{
						Text(stringResource(R.string.stop_ble_server))
					}
				}
			}
		},
		navigationIcon = navigation,
		scrollBehavior = scrollBehavior
	)
}