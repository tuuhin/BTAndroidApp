package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_profile.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eva.bluetoothterminalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionProfileTopBar(
	showRefresh: Boolean,
	onRefresh: () -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit,
	scrollBehavior: TopAppBarScrollBehavior? = null,
	colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors()
) {
	MediumTopAppBar(
		title = { Text(text = stringResource(id = R.string.bl_connect_profile_title)) },
		actions = {
			AnimatedVisibility(
				visible = showRefresh,
				enter = slideInVertically() + fadeIn(),
				exit = slideOutVertically() + fadeOut()
			) {
				TooltipBox(
					positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
					tooltip = {
						PlainTooltip {
							Text(text = stringResource(id = R.string.refresh_tooltip_text))
						}
					},
					state = rememberTooltipState()
				) {
					IconButton(
						onClick = onRefresh,
						colors = IconButtonDefaults
							.iconButtonColors(contentColor = colors.actionIconContentColor)
					) {
						Icon(
							imageVector = Icons.Default.Cached,
							contentDescription = stringResource(id = R.string.refresh_tooltip_text)
						)
					}
				}
			}
		},
		scrollBehavior = scrollBehavior,
		navigationIcon = navigation,
		modifier = modifier
	)
}