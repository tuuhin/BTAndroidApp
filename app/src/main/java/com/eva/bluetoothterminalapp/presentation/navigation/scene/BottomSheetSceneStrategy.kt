package com.eva.bluetoothterminalapp.presentation.navigation.scene

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

/**
 * A [SceneStrategy] that displays entries that have added [bottomSheet] to their [NavEntry.metadata]
 * within a [ModalBottomSheet] instance.
 *
 * This strategy should always be added before any non-overlay scene strategies.
 */
@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {

	@Suppress("UNCHECKED_CAST")
	override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
		val lastEntry = entries.lastOrNull()
		val sheetProperties = lastEntry?.metadata?.get(SheetKey)
		val isSkipPartiallyExpanded = lastEntry?.metadata?.get(SheetIsPartiallyExpanded) ?: false

		if (sheetProperties != null) {
			val scene = BottomSheetScene(
				key = lastEntry.contentKey as T,
				previousEntries = entries.dropLast(1),
				overlaidEntries = entries.dropLast(1),
				entry = lastEntry,
				isSkipPartiallyExpanded = isSkipPartiallyExpanded,
				modalBottomSheetProperties = sheetProperties,
				onBack = onBack,
			)
			return scene
		}
		return null
	}

	companion object {
		/**
		 * Function to be called on the [NavEntry.metadata] to mark this entry as something that
		 * should be displayed within a [ModalBottomSheet].
		 *
		 * @param properties properties that should be passed to the containing
		 * [ModalBottomSheet].
		 */
		@OptIn(ExperimentalMaterial3Api::class)
		fun bottomSheet(
			isSkipPartiallyExpanded: Boolean = false,
			properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
		): Map<String, Any> = metadata {
			put(SheetKey, properties)
			put(SheetIsPartiallyExpanded, isSkipPartiallyExpanded)
		}

		data object SheetKey : NavMetadataKey<ModalBottomSheetProperties>
		data object SheetIsPartiallyExpanded : NavMetadataKey<Boolean>
	}
}