package com.eva.bluetoothterminalapp.presentation.navigation.config

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

object RouteAnimation : DestinationStyle.Animated() {

	override val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?
		get() = {
			slideIntoContainer(
				AnimatedContentTransitionScope.SlideDirection.Up,
				animationSpec = tween(easing = EaseInOut)
			) + fadeIn(animationSpec = tween(easing = EaseInOut))
		}


	override val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?
		get() = {
			slideOutOfContainer(
				AnimatedContentTransitionScope.SlideDirection.Down,
				animationSpec = tween(easing = EaseOutBounce)
			) + fadeOut(animationSpec = tween(easing = EaseOutBounce))
		}

	override val popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?
		get() = {
			slideIntoContainer(
				AnimatedContentTransitionScope.SlideDirection.Up,
				animationSpec = tween(easing = EaseInOut)
			) + fadeIn(animationSpec = tween(easing = EaseInOut))
		}

	override val popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?
		get() = {
			slideOutOfContainer(
				AnimatedContentTransitionScope.SlideDirection.Down,
				animationSpec = tween(easing = EaseOutBounce)
			) + fadeOut(animationSpec = tween(easing = EaseOutBounce))
		}
}