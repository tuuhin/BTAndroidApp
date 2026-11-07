package com.eva.bluetoothterminalapp.presentation.util

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.navigation3.ui.LocalNavAnimatedContentScope

private val NormalSpring = spring(
	stiffness = StiffnessMediumLow,
	visibilityThreshold = Rect.VisibilityThreshold
)


fun Modifier.sharedElementWrapper(
	key: Any,
	renderInOverlayDuringTransition: Boolean = true,
	zIndexInOverlay: Float = 0f,
	placeHolderSize: SharedTransitionScope.PlaceholderSize = SharedTransitionScope.PlaceholderSize.ContentSize,
	boundsTransform: BoundsTransform = BoundsTransform { _, _ -> NormalSpring },
	clipShape: Shape = RectangleShape,
) = composed {
	val transitionScope = LocalSharedTransitionScopeProvider.current ?: return@composed Modifier
	val visibilityScope = LocalNavAnimatedContentScope.current

	with(transitionScope) {
		val state = rememberSharedContentState(key)

		Modifier.sharedElement(
			sharedContentState = state,
			animatedVisibilityScope = visibilityScope,
			renderInOverlayDuringTransition = renderInOverlayDuringTransition,
			zIndexInOverlay = zIndexInOverlay,
			placeholderSize = placeHolderSize,
			boundsTransform = boundsTransform,
			clipInOverlayDuringTransition = OverlayClip(clipShape)
		)
	}
}

fun Modifier.sharedBoundsWrapper(
	key: Any,
	enter: EnterTransition = fadeIn(),
	exit: ExitTransition = fadeOut(),
	renderInOverlayDuringTransition: Boolean = true,
	resizeMode: SharedTransitionScope.ResizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(
		ContentScale.FillWidth,
		Center
	),
	zIndexInOverlay: Float = 0f,
	placeHolderSize: SharedTransitionScope.PlaceholderSize = SharedTransitionScope.PlaceholderSize.ContentSize,
	boundsTransform: BoundsTransform = BoundsTransform { _, _ -> NormalSpring },
	clipShape: Shape = RectangleShape,
) = composed {

	val transitionScope = LocalSharedTransitionScopeProvider.current ?: return@composed Modifier
	val visibilityScope = LocalNavAnimatedContentScope.current

	with(transitionScope) {

		val state = rememberSharedContentState(key)
		Modifier.sharedBounds(
			sharedContentState = state,
			animatedVisibilityScope = visibilityScope,
			exit = exit,
			enter = enter,
			boundsTransform = boundsTransform,
			renderInOverlayDuringTransition = renderInOverlayDuringTransition,
			zIndexInOverlay = zIndexInOverlay,
			placeholderSize = placeHolderSize,
			resizeMode = resizeMode,
			clipInOverlayDuringTransition = OverlayClip(clipShape)
		)
	}
}

@Composable
fun Modifier.sharedTransitionSkipChildSize(): Modifier {
	val transitionScope = LocalSharedTransitionScopeProvider.current ?: return this

	return with(transitionScope) {
		this@sharedTransitionSkipChildSize.skipToLookaheadSize()
	}
}

@Composable
fun Modifier.sharedTransitionSkipChildPosition(): Modifier {
	val transitionScope = LocalSharedTransitionScopeProvider.current ?: return this

	return with(transitionScope) {
		this@sharedTransitionSkipChildPosition
			.skipToLookaheadPosition()
	}
}


@Composable
fun Modifier.sharedTransitionRenderInOverlay(zIndexInOverlay: Float): Modifier {
	val transitionScope = LocalSharedTransitionScopeProvider.current ?: return this
	return with(transitionScope) {
		this@sharedTransitionRenderInOverlay
			.renderInSharedTransitionScopeOverlay(zIndexInOverlay = zIndexInOverlay)
	}
}