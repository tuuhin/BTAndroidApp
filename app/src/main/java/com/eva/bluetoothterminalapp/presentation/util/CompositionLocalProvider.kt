package com.eva.bluetoothterminalapp.presentation.util

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackBarProvider = staticCompositionLocalOf { SnackbarHostState() }
val LocalSharedTransitionScopeProvider = compositionLocalOf<SharedTransitionScope?> { null }