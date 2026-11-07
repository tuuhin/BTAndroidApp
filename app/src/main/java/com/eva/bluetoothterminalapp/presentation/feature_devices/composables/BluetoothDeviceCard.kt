package com.eva.bluetoothterminalapp.presentation.feature_devices.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eva.bluetoothterminalapp.R
import com.eva.bluetoothterminalapp.domain.bluetooth.models.BluetoothDeviceModel
import com.eva.bluetoothterminalapp.presentation.composables.AppIconVectors
import com.eva.bluetoothterminalapp.presentation.util.PreviewFakes
import com.eva.bluetoothterminalapp.presentation.util.SharedElementTransitionKeys
import com.eva.bluetoothterminalapp.presentation.util.sharedBoundsWrapper
import com.eva.bluetoothterminalapp.ui.theme.BlueToothTerminalAppTheme

@Composable
fun BluetoothDeviceCard(
	device: BluetoothDeviceModel,
	onConnect: () -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.large,
	isPaired: Boolean = false,
	selectedColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
	iconShape: Shape = MaterialShapes.Clover8Leaf.toShape(),
	contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
) {
	var isExpanded by rememberSaveable { mutableStateOf(false) }

	val cardContainerColor by animateColorAsState(
		targetValue = if (isExpanded) selectedColor else containerColor,
		label = "Card container color",
		animationSpec = tween(durationMillis = 200)
	)

	Surface(
		onClick = { isExpanded = true },
		shape = shape,
		color = cardContainerColor,
		contentColor = contentColorFor(containerColor),
		modifier = modifier.sharedBoundsWrapper(SharedElementTransitionKeys.btProfileScreen(device.address))
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier.padding(contentPadding)
		) {
			BTDeviceIcon(
				device = device,
				shape = iconShape,
				deviceName = device.name,
				contentColor = if (isPaired) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer,
				containerColor = if (isPaired) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onTertiaryContainer
			)
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = device.name,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.titleMediumEmphasized,
					fontWeight = FontWeight.Medium,
					letterSpacing = 1.2.sp,
					color = MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = buildAnnotatedString {
						append(stringResource(id = R.string.bluetooth_device_mac_address_title))
						append(" ")
						withStyle(SpanStyle(fontFamily = FontFamily.Monospace)) {
							append(device.address)
						}
					},
					style = MaterialTheme.typography.bodyMediumEmphasized,
					fontWeight = FontWeight.Normal,
				)
			}
			Box {
				FilledIconButton(
					onClick = { isExpanded = true },
					colors = IconButtonDefaults.iconButtonColors(
						containerColor = MaterialTheme.colorScheme.secondary,
						contentColor = MaterialTheme.colorScheme.onSecondary,
					),
					modifier = Modifier
						.minimumInteractiveComponentSize()
						.size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
					shapes = IconButtonDefaults.shapes(
						shape = IconButtonDefaults.mediumRoundShape,
						pressedShape = IconButtonDefaults.mediumRoundShape,
					),
				) {
					Icon(
						imageVector = AppIconVectors.moreVert,
						contentDescription = stringResource(id = R.string.menu_option_more),
					)
				}
				DropdownMenu(
					expanded = isExpanded,
					onDismissRequest = { isExpanded = false },
					shape = MaterialTheme.shapes.medium,
					tonalElevation = 4.dp
				) {
					DropdownMenuItem(
						text = { Text(text = stringResource(R.string.connect_to_client)) },
						onClick = {
							isExpanded = false
							onConnect()
						},
						leadingIcon = {
							Icon(
								imageVector = AppIconVectors.cable,
								contentDescription = stringResource(R.string.connect_to_client)
							)
						},
					)
				}
			}
		}
	}
}


@PreviewLightDark
@Composable
private fun BluetoothDeviceCardPreview() = BlueToothTerminalAppTheme {
	BluetoothDeviceCard(
		device = PreviewFakes.FAKE_DEVICE_MODEL,
		onConnect = {}
	)
}