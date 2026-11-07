package com.eva.bluetoothterminalapp.presentation.feature_connect.bt_server.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun StartServerBox(
	onStartServer: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier.widthIn(max = dimensionResource(R.dimen.permission_box_min_size)),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Surface(
			color = MaterialTheme.colorScheme.tertiaryContainer,
			contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
			shape = MaterialShapes.Cookie12Sided.toShape(),
			modifier = Modifier.sizeIn(minWidth = 200.dp, minHeight = 200.dp)
		) {
			Icon(
				painter = painterResource(R.drawable.ic_connect_variant),
				contentDescription = stringResource(id = R.string.bt_server_start_connection_text),
				modifier = Modifier.padding(20.dp),
			)
		}

		Spacer(modifier = Modifier.height(12.dp))
		Text(
			text = stringResource(id = R.string.bt_server_start_connection_text),
			style = MaterialTheme.typography.headlineSmallEmphasized,
			color = MaterialTheme.colorScheme.primary
		)
		Text(
			text = stringResource(id = R.string.bt_server_start_connection_desc),
			style = MaterialTheme.typography.bodyMediumEmphasized,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.fillMaxWidth(.9f),
			textAlign = TextAlign.Center
		)
		Spacer(modifier = Modifier.height(12.dp))
		Button(
			onClick = onStartServer,
			contentPadding = ButtonDefaults.contentPaddingFor(
				ButtonDefaults.MediumContainerHeight,
				hasStartIcon = true
			),
			shapes = ButtonDefaults.shapes(
				pressedShape = ButtonDefaults.mediumPressedShape,
				shape = ButtonDefaults.shape
			),
			colors = ButtonDefaults.buttonColors(
				containerColor = MaterialTheme.colorScheme.primaryContainer,
				contentColor = MaterialTheme.colorScheme.onPrimaryContainer
			),
			modifier = Modifier.fillMaxWidth(.75f)
		) {
			Text(
				text = stringResource(id = R.string.bt_server_start_connection_text),
				fontWeight = FontWeight.SemiBold,
				style = ButtonDefaults.textStyleFor(ButtonDefaults.MediumContainerHeight)
			)
		}
		Spacer(modifier = Modifier.height(12.dp))
		Text(
			text = stringResource(R.string.bt_server_start_connection_extra_info),
			style = MaterialTheme.typography.labelMediumEmphasized,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.fillMaxWidth(.75f),
			textAlign = TextAlign.Center
		)
	}
}

@PreviewLightDark
@Composable
private fun StartServerBoxPreview() = BlueToothTerminalAppTheme {
	Surface {
		StartServerBox(
			onStartServer = { },
			modifier = Modifier.fillMaxSize()
		)
	}
}