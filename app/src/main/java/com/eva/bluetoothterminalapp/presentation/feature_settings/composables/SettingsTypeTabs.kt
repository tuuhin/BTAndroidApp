package com.eva.bluetoothterminalapp.presentation.feature_settings.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.presentation.util.BluetoothTypes
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BTSettingsTabs(
	classicTabContent: @Composable () -> Unit,
	leTabContent: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	initialTab: BluetoothTypes = BluetoothTypes.LOW_ENERGY,
	contentPadding: PaddingValues = PaddingValues(0.dp)
) {

	val pagerState = rememberPagerState(
		initialPage = initialTab.tabIdx,
		pageCount = { 2 },
	)

	val scope = rememberCoroutineScope()

	Column(
		modifier = modifier.padding(contentPadding),
		verticalArrangement = Arrangement.spacedBy(2.dp)
	) {
		TabRow(
			selectedTabIndex = pagerState.currentPage,
			divider = {
				HorizontalDivider(
					color = MaterialTheme.colorScheme.outlineVariant,
					modifier = Modifier.padding(bottom = 2.dp)
				)
			}
		) {
			BluetoothTypes.entries.forEach { tab ->
				Tab(
					selected = tab.tabIdx == pagerState.currentPage,
					onClick = {
						scope.launch {
							pagerState.animateScrollToPage(tab.tabIdx)
						}
					},
					text = { Text(text = stringResource(id = tab.text)) },
					selectedContentColor = MaterialTheme.colorScheme.onSurface,
					unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
		HorizontalPager(
			state = pagerState,
			contentPadding = PaddingValues(
				horizontal = dimensionResource(R.dimen.sc_padding),
				vertical = dimensionResource(R.dimen.sc_padding_secondary)
			),
			flingBehavior = PagerDefaults.flingBehavior(
				state = pagerState,
				snapPositionalThreshold = .4f,
				snapAnimationSpec = spring(
					dampingRatio = Spring.DampingRatioNoBouncy,
					stiffness = Spring.StiffnessLow
				)
			),
			modifier = Modifier.fillMaxSize(),
		) { idx ->
			when (idx) {
				BluetoothTypes.CLASSIC.tabIdx -> classicTabContent()
				BluetoothTypes.LOW_ENERGY.tabIdx -> leTabContent()
				else -> {}
			}
		}
	}
}