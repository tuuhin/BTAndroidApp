package com.eva.bluetoothterminalapp.presentation.feature_client.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.models.BTClientStatus
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun ConnectionStatusCard(
	btClientStatus: BTClientStatus,
	modifier: Modifier = Modifier
) {
	Card(
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
		shape = MaterialTheme.shapes.medium,
		modifier = modifier
	) {
		Crossfade(
			targetState = btClientStatus,
			label = "Current Status",
			animationSpec = tween(durationMillis = 300, easing = LinearEasing),
			modifier = Modifier
				.padding(12.dp)
				.align(Alignment.CenterHorizontally)
		) { status ->
			when (status) {
				BTClientStatus.CONNECTION_DEVICE_FOUND -> Text(text = stringResource(id = R.string.connection_device_found))
				BTClientStatus.CONNECTION_DENIED -> Text(text = stringResource(id = R.string.connection_denied))
				BTClientStatus.CONNECTION_DISCONNECTED -> Text(text = stringResource(id = R.string.connection_disconnected))
				BTClientStatus.CONNECTION_BONDING -> Text(text = stringResource(id = R.string.connection_device_bonding))
				BTClientStatus.CONNECTION_BONDED -> Text(text = stringResource(id = R.string.connection_bonded))
				BTClientStatus.CONNECTION_INITIALIZING -> Text(text = stringResource(R.string.connection_init))
				BTClientStatus.CONNECTION_ACCEPTED -> Text(text = stringResource(id = R.string.connection_accpted))
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun ConnectionStatusCardPreview() = BlueToothTerminalAppTheme {
	ConnectionStatusCard(
		btClientStatus = BTClientStatus.CONNECTION_DEVICE_FOUND,
		modifier = Modifier.fillMaxWidth()
	)
}