package com.eva.bluetoothterminalapp.presentation.feature_le_server.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R

@Composable
fun StartBLEServer(
	onStartServer: () -> Unit,
	onConfigureServices: () -> Unit,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Surface(
			color = MaterialTheme.colorScheme.tertiaryContainer,
			contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
			shape = MaterialShapes.Clover8Leaf.toShape(),
			modifier = Modifier.sizeIn(minWidth = 220.dp, minHeight = 220.dp)
		) {
			Icon(
				painter = painterResource(R.drawable.ic_network_mesh),
				contentDescription = null,
				tint = MaterialTheme.colorScheme.onTertiaryContainer,
				modifier = Modifier.padding(40.dp),
			)
		}
		Spacer(modifier = Modifier.height(12.dp))
		Text(
			text = stringResource(id = R.string.bt_server_start_connection_text),
			style = MaterialTheme.typography.headlineSmallEmphasized,
			color = MaterialTheme.colorScheme.primary
		)
		Text(
			text = stringResource(R.string.ble_server_start_text),
			style = MaterialTheme.typography.bodyMediumEmphasized,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.fillMaxWidth(.75f),
			textAlign = TextAlign.Center
		)
		Spacer(modifier = Modifier.height(12.dp))
		SplitButtonLayout(
			leadingButton = {
				SplitButtonDefaults.LeadingButton(
					onClick = onStartServer,
					shapes = SplitButtonDefaults.leadingButtonShapesFor(ButtonDefaults.MediumContainerHeight),
					contentPadding = SplitButtonDefaults.leadingButtonContentPaddingFor(
						ButtonDefaults.MediumContainerHeight
					),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.primaryContainer,
						contentColor = MaterialTheme.colorScheme.onPrimaryContainer
					)
				) {
					Text(
						text = stringResource(id = R.string.bt_server_start_connection_text),
						fontWeight = FontWeight.SemiBold
					)
				}
			},
			trailingButton = {
				SplitButtonDefaults.TrailingButton(
					onClick = onConfigureServices,
					shapes = SplitButtonDefaults.trailingButtonShapesFor(ButtonDefaults.MediumContainerHeight),
					contentPadding = SplitButtonDefaults.leadingButtonContentPaddingFor(
						ButtonDefaults.MediumContainerHeight
					),
					colors = ButtonDefaults.buttonColors(
						containerColor = MaterialTheme.colorScheme.secondaryContainer,
						contentColor = MaterialTheme.colorScheme.onSecondaryContainer
					)
				) {
					Text(
						text = stringResource(R.string.ble_server_set_services),
						style = MaterialTheme.typography.titleSmall
					)
				}
			},
		)
	}
}