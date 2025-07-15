package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.presentation.util.BluetoothTypes
import com.eva.bluetoothterminalapp.presentation.util.textResource
import kotlinx.coroutines.launch

@OptIn(
	ExperimentalFoundationApi::class,
	ExperimentalMaterial3Api::class
)
@Composable
fun BTDevicesTabsLayout(
	isScanning: Boolean,
	onCurrentTabChanged: (BluetoothTypes) -> Unit,
	modifier: Modifier = Modifier,
	initialTab: BluetoothTypes = BluetoothTypes.CLASSIC,
	contentPadding: PaddingValues = PaddingValues(0.dp),
	classicTabContent: @Composable () -> Unit,
	leTabContent: @Composable () -> Unit,
) {

	val scope = rememberCoroutineScope()
	val onPageChange by rememberUpdatedState(newValue = onCurrentTabChanged)

	val pagerState = rememberPagerState(
		initialPage = initialTab.tabIdx,
		pageCount = { BluetoothTypes.entries.size }
	)

	val selectedTab by remember(pagerState.currentPage) {
		derivedStateOf { pagerState.currentPage }
	}

	val onTabChange: (Int) -> Unit = remember {
		{ index ->
			if (selectedTab != index) scope.launch {
				pagerState.animateScrollToPage(index)
			}
		}
	}

	LaunchedEffect(selectedTab) {
		val tab = if (BluetoothTypes.LOW_ENERGY.tabIdx == selectedTab)
			BluetoothTypes.LOW_ENERGY
		else BluetoothTypes.CLASSIC
		onPageChange(tab)
	}

	Column(
		verticalArrangement = Arrangement.spacedBy(2.dp)
	) {
		AnimatedVisibility(
			visible = isScanning,
			enter = slideInHorizontally() + fadeIn(tween(easing = FastOutSlowInEasing)),
			exit = slideOutHorizontally() + fadeOut(tween(easing = FastOutSlowInEasing)),
			label = "Indication that scan is running"
		) {
			LinearProgressIndicator(
				modifier = Modifier.fillMaxWidth(),
				color = MaterialTheme.colorScheme.primary,
				trackColor = MaterialTheme.colorScheme.surfaceVariant,
				strokeCap = StrokeCap.Butt
			)
		}
		PrimaryTabRow(
			selectedTabIndex = selectedTab,
			divider = {
				HorizontalDivider(
					color = MaterialTheme.colorScheme.outlineVariant,
					modifier = Modifier.padding(bottom = 2.dp)
				)
			}
		) {
			BluetoothTypes.entries.forEach { tab ->
				Tab(
					selected = tab.tabIdx == selectedTab,
					onClick = { onTabChange(tab.tabIdx) },
					text = { Text(text = tab.textResource) },
					selectedContentColor = MaterialTheme.colorScheme.onSurface,
					unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
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
					dampingRatio = Spring.DampingRatioLowBouncy,
					stiffness = Spring.StiffnessVeryLow
				)
			),
		) { idx ->
			when (idx) {
				BluetoothTypes.CLASSIC.tabIdx -> classicTabContent()
				BluetoothTypes.LOW_ENERGY.tabIdx -> leTabContent()
				else -> {}
			}
		}
	}
}