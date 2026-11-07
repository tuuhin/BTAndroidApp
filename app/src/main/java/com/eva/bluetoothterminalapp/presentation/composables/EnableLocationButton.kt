package com.eva.bluetoothterminalapp.presentation.composables

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.data.utils.hasCoarseLocationPermission
import com.eva.bluetoothterminalapp.data.utils.hasLocationPermission
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun EnableLocationButton(
	onFineLocationAccess: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	val context = LocalContext.current

	var hasCoarseLocation by rememberSaveable { mutableStateOf(context.hasCoarseLocationPermission) }
	var hasFineLocationPerms by rememberSaveable { mutableStateOf(context.hasLocationPermission) }

	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestMultiplePermissions()
	) { perms ->
		hasCoarseLocation = perms.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
		hasFineLocationPerms = perms.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
		onFineLocationAccess(hasFineLocationPerms)
	}

	Button(
		onClick = {
			launcher.launch(
				arrayOf(
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.ACCESS_FINE_LOCATION
				)
			)
		},
		contentPadding = ButtonDefaults.contentPaddingFor(
			ButtonDefaults.MediumContainerHeight,
			hasStartIcon = true
		),
		shapes = ButtonDefaults.shapes(
			pressedShape = ButtonDefaults.mediumPressedShape,
			shape = ButtonDefaults.shape
		),
		modifier = modifier,
	) {
		Icon(
			imageVector = AppIconVectors.locationOn,
			contentDescription = null,
			modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MediumContainerHeight)),
		)
		Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MediumContainerHeight)))
		Text(
			text = stringResource(id = R.string.allow_location_button_text),
			style = ButtonDefaults.textStyleFor(ButtonDefaults.MediumContainerHeight),
		)
	}
}

@PreviewLightDark
@Composable
private fun EnableLocationButtonPReview() = BlueToothTerminalAppTheme {
	EnableLocationButton(onFineLocationAccess = {})
}