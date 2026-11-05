package com.eva.bluetoothterminalapp.presentation.navigation.scene

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import kotlinx.coroutines.launch

/** An [OverlayScene] that renders an [entry] within a [ModalBottomSheet]. */
@OptIn(ExperimentalMaterial3Api::class)
data class BottomSheetScene<T : Any>(
	override val key: T,
	override val previousEntries: List<NavEntry<T>>,
	override val overlaidEntries: List<NavEntry<T>>,
	private val entry: NavEntry<T>,
	private val modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
	private val isSkipPartiallyExpanded: Boolean = false,
	private val onBack: () -> Unit,
) : OverlayScene<T> {

	override val entries: List<NavEntry<T>> = listOf(entry)
	private var sheetState: SheetState? = null

	override val content: @Composable (() -> Unit) = {

		val scope = rememberCoroutineScope()

		val sheetState = rememberBottomSheetState(
			initialValue = SheetValue.Hidden,
			enabledValues = buildSet {
				addAll(listOf(SheetValue.Hidden, SheetValue.Expanded))
				if (!isSkipPartiallyExpanded) add(SheetValue.PartiallyExpanded)
			},
		).apply {
			this@BottomSheetScene.sheetState = this
		}

		val scaleAnimation by animateFloatAsState(
			targetValue = when (sheetState.targetValue) {
				SheetValue.Hidden -> 0.4f
				else -> 0f
			},
			animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
			label = "sheetScale",
		)

		LaunchedEffect(Unit) {
			if (isSkipPartiallyExpanded) sheetState.show()
			else sheetState.partialExpand()
		}

		ModalBottomSheet(
			onDismissRequest = {
				scope.launch { sheetState.hide() }
					.invokeOnCompletion { onBack() }
			},
			sheetState = sheetState,
			properties = modalBottomSheetProperties,
			modifier = Modifier.graphicsLayer {
				scaleX = 1 - scaleAnimation
				scaleY = 1f - scaleAnimation * 0.15f
				transformOrigin = TransformOrigin(.5f, 1f)
			},
		) {
			Box(
				modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 12.dp),
				contentAlignment = Alignment.Center,
			) {
				entry.Content()
			}
		}
	}

	override suspend fun onRemove() {
		sheetState?.hide()
	}
}