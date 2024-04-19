package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.presentation.feature_devices.util.BTDeviceTabs

@OptIn(
	ExperimentalFoundationApi::class,
	ExperimentalMaterial3Api::class
)
@Composable
fun BTDevicesTabsLayout(
	pagerState: PagerState,
	isScanning: Boolean,
	onTabChange: (BTDeviceTabs) -> Unit,
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp),
	classicTabContent: @Composable () -> Unit,
	leTabContent: @Composable () -> Unit,
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(2.dp)
	) {
		AnimatedVisibility(
			visible = isScanning,
			enter = slideInVertically() + fadeIn(),
			exit = slideOutVertically() + fadeOut(),
			label = "Is scan running"
		) {
			LinearProgressIndicator(
				modifier = Modifier.fillMaxWidth(),
				color = MaterialTheme.colorScheme.primary,
				trackColor = MaterialTheme.colorScheme.surfaceVariant,
				strokeCap = StrokeCap.Square
			)
		}
		SecondaryTabRow(
			selectedTabIndex = pagerState.currentPage,
			divider = {
				HorizontalDivider(
					color = MaterialTheme.colorScheme.outlineVariant,
					modifier = Modifier.padding(bottom = 2.dp)
				)
			}
		) {
			BTDeviceTabs.entries.forEach { tab ->
				Tab(
					selected = tab.tabIdx == pagerState.currentPage,
					onClick = { onTabChange(tab) },
					text = { Text(text = stringResource(id = tab.text)) },
				)
			}
		}
		HorizontalPager(
			state = pagerState,
			modifier = modifier,
			contentPadding = contentPadding,
			flingBehavior = PagerDefaults.flingBehavior(
				state = pagerState,
				snapPositionalThreshold = .4f,
				snapAnimationSpec = spring(
					dampingRatio = Spring.DampingRatioNoBouncy,
					stiffness = Spring.StiffnessLow
				)
			)
		) { idx ->
			when (idx) {
				BTDeviceTabs.CLASSIC.tabIdx -> classicTabContent()
				BTDeviceTabs.LOW_ENERGY.tabIdx -> leTabContent()
				else -> {}
			}
		}
	}
}