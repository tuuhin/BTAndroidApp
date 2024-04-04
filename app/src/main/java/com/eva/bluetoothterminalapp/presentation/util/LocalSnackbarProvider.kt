package com.eva.bluetoothterminalapp.presentation.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

val LocalSnackBarProvider = compositionLocalOf { SnackbarHostState() }