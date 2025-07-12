@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.eva.bluetoothterminalapp.presentation.util


import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.ScaleToBounds
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale


private val NormalSpring = spring(
	stiffness = StiffnessMediumLow,
	visibilityThreshold = Rect.VisibilityThreshold
)


fun Modifier.sharedElementWrapper(
	key: Any,
	renderInOverlayDuringTransition: Boolean = true,
	zIndexInOverlay: Float = 0f,
	placeHolderSize: SharedTransitionScope.PlaceHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize,
	boundsTransform: BoundsTransform = BoundsTransform { _, _ -> NormalSpring },
) = composed {
	val transitionScope = LocalSharedTransitionScopeProvider.current ?: return@composed Modifier
	val visibilityScope =
		LocalSharedTransitionVisibilityScopeProvider.current ?: return@composed Modifier

	with(transitionScope) {
		val state = rememberSharedContentState(key)

		Modifier.sharedElement(
			sharedContentState = state,
			animatedVisibilityScope = visibilityScope,
			renderInOverlayDuringTransition = renderInOverlayDuringTransition,
			zIndexInOverlay = zIndexInOverlay,
			placeHolderSize = placeHolderSize,
			boundsTransform = boundsTransform
		)
	}
}

fun Modifier.sharedBoundsWrapper(
	key: Any,
	enter: EnterTransition = fadeIn(),
	exit: ExitTransition = fadeOut(),
	renderInOverlayDuringTransition: Boolean = true,
	resizeMode: ResizeMode = ScaleToBounds(ContentScale.FillWidth, Center),
	zIndexInOverlay: Float = 0f,
	placeHolderSize: SharedTransitionScope.PlaceHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize,
	boundsTransform: BoundsTransform = BoundsTransform { _, _ -> NormalSpring },
) = composed {

	val transitionScope = LocalSharedTransitionScopeProvider.current ?: return@composed Modifier
	val visibilityScope =
		LocalSharedTransitionVisibilityScopeProvider.current ?: return@composed Modifier

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
			placeHolderSize = placeHolderSize,
			resizeMode = resizeMode,
		)
	}
}