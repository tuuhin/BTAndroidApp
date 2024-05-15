package com.eva.bluetoothterminalapp.presentation.navigation

import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.eva.bluetoothterminalapp.presentation.util.AppViewModel
import com.eva.bluetoothterminalapp.presentation.util.LocalSnackBarProvider
import com.eva.bluetoothterminalapp.presentation.util.UiEvents
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun <T : AppViewModel> UIEventsSideEffect(viewModel: T, navigator: DestinationsNavigator) {

	val context = LocalContext.current
	val lifecyleOwner = LocalLifecycleOwner.current
	val snackBarHostState = LocalSnackBarProvider.current

	LaunchedEffect(lifecyleOwner, context) {
		lifecyleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			viewModel.uiEvents.collect { event ->
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

					is UiEvents.NavigateBack -> navigator.popBackStack()
				}
			}
		}
	}
}