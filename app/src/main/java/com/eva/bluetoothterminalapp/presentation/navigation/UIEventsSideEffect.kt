package com.eva.bluetoothterminalapp.presentation.navigation

import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import kotlinx.coroutines.flow.Flow

@Composable
fun UIEventsSideEffect(
	events: () -> Flow<UiEvents>,
	onPopBack: () -> Unit = {}
) {

	val context = LocalContext.current
	val lifecyleOwner = LocalLifecycleOwner.current
	val snackBarHostState = LocalSnackBarProvider.current
	val currentOnPopBack by rememberUpdatedState(onPopBack)

	LaunchedEffect(lifecyleOwner, context) {
		lifecyleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			val eventFlow = events()
			eventFlow.collect { event ->
				when (event) {
					is UiEvents.ShowSnackBar -> snackBarHostState
						.showSnackbar(message = event.message)

					is UiEvents.ShowSnackBarWithActions -> {
						val result = snackBarHostState.showSnackbar(
							message = event.message,
							actionLabel = event.actionText,
							duration = SnackbarDuration.Short
						)
						when (result) {
							SnackbarResult.Dismissed -> {}
							SnackbarResult.ActionPerformed -> event.action()
						}
					}

					is UiEvents.ShowToast -> Toast
						.makeText(context, event.message, Toast.LENGTH_SHORT)
						.show()

					is UiEvents.NavigateBack -> currentOnPopBack()
				}
			}
		}
	}
}