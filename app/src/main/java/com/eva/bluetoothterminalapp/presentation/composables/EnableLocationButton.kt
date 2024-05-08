package com.eva.bluetoothterminalapp.presentation.composables

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun EnableLocationButton(
	onFineLocationAccess: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.medium,
	contentPaddingValues: PaddingValues = ButtonDefaults.ContentPadding,
	colors: ButtonColors = ButtonDefaults.buttonColors()
) {
	val context = LocalContext.current

	var hasCoarseLocation by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.ACCESS_COARSE_LOCATION
			) == PermissionChecker.PERMISSION_GRANTED
		)
	}
	var hasFineLocationPerms by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PermissionChecker.PERMISSION_GRANTED
		)
	}

	val permissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestMultiplePermissions()
	) { perms ->
		hasCoarseLocation =
			perms.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
		hasFineLocationPerms =
			perms.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)

		onFineLocationAccess(hasFineLocationPerms)
	}

	TextButton(
		onClick = {
			permissionLauncher.launch(
				arrayOf(
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.ACCESS_FINE_LOCATION
				)
			)
		},
		shape = shape,
		contentPadding = contentPaddingValues,
		colors = colors,
		modifier = modifier,
	) {
		Icon(
			imageVector = Icons.Outlined.LocationOn,
			contentDescription = null
		)
		Spacer(modifier = Modifier.width(2.dp))
		Text(
			text = stringResource(id = R.string.allow_location_button_text)
		)
	}
}

@PreviewLightDark
@Composable
private fun EnableLocationButtonPReview() = BlueToothTerminalAppTheme {
	EnableLocationButton(onFineLocationAccess = {})
}