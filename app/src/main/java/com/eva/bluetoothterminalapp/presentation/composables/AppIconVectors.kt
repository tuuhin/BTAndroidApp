package com.eva.bluetoothterminalapp.presentation.composables

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object AppIconVectors {

	val backArrow: ImageVector
		get() {
			if (_arrow_back != null) {
				return _arrow_back!!
			}
			_arrow_back =
				ImageVector.Builder(
					name = "arrow_back",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(7.83f, 13f)
							lineToRelative(5.6f, 5.6f)
							lineTo(12f, 20f)
							lineTo(4f, 12f)
							lineTo(12f, 4f)
							lineToRelative(1.43f, 1.4f)
							lineTo(7.83f, 11f)
							horizontalLineTo(20f)
							verticalLineToRelative(2f)
							horizontalLineTo(7.83f)
							close()
						}
					}
					.build()
			return _arrow_back!!
		}

	val bluetooth: ImageVector
		get() {
			if (_bluetooth != null) {
				return _bluetooth!!
			}
			_bluetooth =
				ImageVector.Builder(
					name = "bluetooth",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(11f, 22f)
							verticalLineTo(14.4f)
							lineTo(6.4f, 19f)
							lineTo(5f, 17.6f)
							lineTo(10.6f, 12f)
							lineTo(5f, 6.4f)
							lineTo(6.4f, 5f)
							lineTo(11f, 9.6f)
							verticalLineTo(2f)
							horizontalLineToRelative(1f)
							lineToRelative(5.7f, 5.7f)
							lineTo(13.4f, 12f)
							lineToRelative(4.3f, 4.3f)
							lineTo(12f, 22f)
							horizontalLineTo(11f)
							close()
							moveTo(13f, 9.6f)
							lineTo(14.9f, 7.7f)
							lineTo(13f, 5.85f)
							verticalLineTo(9.6f)
							close()
							moveToRelative(0f, 8.55f)
							lineTo(14.9f, 16.3f)
							lineTo(13f, 14.4f)
							verticalLineToRelative(3.75f)
							close()
						}
					}
					.build()
			return _bluetooth!!
		}

	val locationOn: ImageVector
		get() {
			if (_location_on != null) {
				return _location_on!!
			}
			_location_on =
				ImageVector.Builder(
					name = "location_on",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(13.41f, 11.41f)
							quadTo(14f, 10.83f, 14f, 10f)
							quadTo(14f, 9.17f, 13.41f, 8.59f)
							reflectiveQuadTo(12f, 8f)
							reflectiveQuadTo(10.59f, 8.59f)
							reflectiveQuadTo(10f, 10f)
							reflectiveQuadToRelative(0.59f, 1.41f)
							reflectiveQuadTo(12f, 12f)
							reflectiveQuadToRelative(1.41f, -0.59f)
							close()
							moveTo(12f, 19.35f)
							quadToRelative(3.05f, -2.8f, 4.53f, -5.09f)
							quadTo(18f, 11.98f, 18f, 10.2f)
							quadTo(18f, 7.47f, 16.26f, 5.74f)
							quadTo(14.53f, 4f, 12f, 4f)
							reflectiveQuadTo(7.74f, 5.74f)
							quadTo(6f, 7.47f, 6f, 10.2f)
							quadToRelative(0f, 1.78f, 1.48f, 4.06f)
							reflectiveQuadTo(12f, 19.35f)
							close()
							moveTo(12f, 22f)
							quadTo(7.98f, 18.58f, 5.99f, 15.64f)
							reflectiveQuadTo(4f, 10.2f)
							quadTo(4f, 6.45f, 6.41f, 4.22f)
							reflectiveQuadTo(12f, 2f)
							reflectiveQuadToRelative(5.59f, 2.22f)
							reflectiveQuadTo(20f, 10.2f)
							quadToRelative(0f, 2.5f, -1.99f, 5.44f)
							quadTo(16.03f, 18.58f, 12f, 22f)
							close()
							moveTo(12f, 10f)
							close()
						}
					}
					.build()
			return _location_on!!
		}

	val bluetoothDisabled: ImageVector
		get() {
			if (_bluetooth_disabled != null) {
				return _bluetooth_disabled!!
			}
			_bluetooth_disabled =
				ImageVector.Builder(
					name = "bluetooth_disabled",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(19.8f, 22.6f)
							lineTo(15.6f, 18.4f)
							lineTo(12f, 22f)
							horizontalLineTo(11f)
							verticalLineTo(14.4f)
							lineTo(6.4f, 19f)
							lineTo(5f, 17.6f)
							lineTo(9.9f, 12.7f)
							lineTo(1.4f, 4.2f)
							lineTo(2.8f, 2.8f)
							lineTo(21.2f, 21.2f)
							lineToRelative(-1.4f, 1.4f)
							close()
							moveTo(13f, 18.15f)
							lineTo(14.15f, 17f)
							lineTo(13f, 15.85f)
							verticalLineToRelative(2.3f)
							close()
							moveTo(14.1f, 11.3f)
							lineTo(12.7f, 9.9f)
							lineTo(14.9f, 7.7f)
							lineTo(13f, 5.85f)
							verticalLineTo(10.2f)
							lineToRelative(-2f, -2f)
							verticalLineTo(2f)
							horizontalLineToRelative(1f)
							lineToRelative(5.7f, 5.7f)
							lineToRelative(-3.6f, 3.6f)
							close()
						}
					}
					.build()
			return _bluetooth_disabled!!
		}

	val moreVert: ImageVector
		get() {
			if (_more_vert != null) {
				return _more_vert!!
			}
			_more_vert =
				ImageVector.Builder(
					name = "more_vert",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(12f, 20f)
							quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
							reflectiveQuadTo(10f, 18f)
							reflectiveQuadToRelative(0.59f, -1.41f)
							reflectiveQuadTo(12f, 16f)
							reflectiveQuadToRelative(1.41f, 0.59f)
							quadTo(14f, 17.18f, 14f, 18f)
							reflectiveQuadToRelative(-0.59f, 1.41f)
							reflectiveQuadTo(12f, 20f)
							close()
							moveToRelative(0f, -6f)
							quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
							reflectiveQuadTo(10f, 12f)
							reflectiveQuadToRelative(0.59f, -1.41f)
							reflectiveQuadTo(12f, 10f)
							reflectiveQuadToRelative(1.41f, 0.59f)
							quadTo(14f, 11.18f, 14f, 12f)
							reflectiveQuadToRelative(-0.59f, 1.41f)
							reflectiveQuadTo(12f, 14f)
							close()
							moveTo(12f, 8f)
							quadTo(11.18f, 8f, 10.59f, 7.41f)
							reflectiveQuadTo(10f, 6f)
							reflectiveQuadTo(10.59f, 4.59f)
							reflectiveQuadTo(12f, 4f)
							reflectiveQuadToRelative(1.41f, 0.59f)
							quadTo(14f, 5.18f, 14f, 6f)
							reflectiveQuadTo(13.41f, 7.41f)
							reflectiveQuadTo(12f, 8f)
							close()
						}
					}
					.build()
			return _more_vert!!
		}

	val cable: ImageVector
		get() {
			if (_cable != null) {
				return _cable!!
			}
			_cable =
				ImageVector.Builder(
					name = "cable",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(5f, 21f)
							quadTo(4.58f, 21f, 4.29f, 20.71f)
							quadTo(4f, 20.43f, 4f, 20f)
							verticalLineTo(19f)
							horizontalLineTo(3f)
							verticalLineTo(15f)
							quadTo(3f, 14.58f, 3.29f, 14.29f)
							reflectiveQuadTo(4f, 14f)
							horizontalLineTo(5f)
							verticalLineTo(7f)
							quadTo(5f, 5.35f, 6.18f, 4.17f)
							reflectiveQuadTo(9f, 3f)
							reflectiveQuadToRelative(2.83f, 1.17f)
							reflectiveQuadTo(13f, 7f)
							verticalLineTo(17f)
							quadToRelative(0f, 0.82f, 0.59f, 1.41f)
							reflectiveQuadTo(15f, 19f)
							reflectiveQuadToRelative(1.41f, -0.59f)
							reflectiveQuadTo(17f, 17f)
							verticalLineTo(10f)
							horizontalLineTo(16f)
							quadTo(15.58f, 10f, 15.29f, 9.71f)
							reflectiveQuadTo(15f, 9f)
							verticalLineTo(5f)
							horizontalLineToRelative(1f)
							verticalLineTo(4f)
							quadTo(16f, 3.57f, 16.29f, 3.29f)
							reflectiveQuadTo(17f, 3f)
							horizontalLineToRelative(2f)
							quadToRelative(0.43f, 0f, 0.71f, 0.29f)
							reflectiveQuadTo(20f, 4f)
							verticalLineTo(5f)
							horizontalLineToRelative(1f)
							verticalLineTo(9f)
							quadToRelative(0f, 0.42f, -0.29f, 0.71f)
							reflectiveQuadTo(20f, 10f)
							horizontalLineTo(19f)
							verticalLineToRelative(7f)
							quadToRelative(0f, 1.65f, -1.18f, 2.82f)
							reflectiveQuadTo(15f, 21f)
							reflectiveQuadTo(12.18f, 19.83f)
							reflectiveQuadTo(11f, 17f)
							verticalLineTo(7f)
							quadTo(11f, 6.18f, 10.41f, 5.59f)
							reflectiveQuadTo(9f, 5f)
							quadTo(8.18f, 5f, 7.59f, 5.59f)
							quadTo(7f, 6.18f, 7f, 7f)
							verticalLineToRelative(7f)
							horizontalLineTo(8f)
							quadToRelative(0.43f, 0f, 0.71f, 0.29f)
							reflectiveQuadTo(9f, 15f)
							verticalLineToRelative(4f)
							horizontalLineTo(8f)
							verticalLineToRelative(1f)
							quadToRelative(0f, 0.43f, -0.29f, 0.71f)
							reflectiveQuadTo(7f, 21f)
							horizontalLineTo(5f)
							close()
						}
					}
					.build()
			return _cable!!
		}

	val ecgHeart: ImageVector
		get() {
			if (_ecg_heart != null) {
				return _ecg_heart!!
			}
			_ecg_heart =
				ImageVector.Builder(
					name = "ecg_heart",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(12f, 12f)
							close()
							moveToRelative(0f, 9f)
							quadToRelative(-0.45f, 0f, -0.86f, -0.16f)
							quadTo(10.73f, 20.68f, 10.4f, 20.35f)
							lineTo(3.7f, 13.63f)
							quadToRelative(-0.88f, -0.88f, -1.29f, -2f)
							reflectiveQuadTo(2f, 9.27f)
							quadTo(2f, 6.7f, 3.68f, 4.85f)
							reflectiveQuadTo(7.85f, 3f)
							quadToRelative(1.2f, 0f, 2.26f, 0.47f)
							reflectiveQuadTo(12f, 4.8f)
							quadTo(12.8f, 3.95f, 13.86f, 3.47f)
							reflectiveQuadTo(16.13f, 3f)
							quadToRelative(2.5f, 0f, 4.19f, 1.85f)
							reflectiveQuadTo(22f, 9.25f)
							quadToRelative(0f, 1.22f, -0.42f, 2.35f)
							reflectiveQuadToRelative(-1.28f, 2f)
							lineToRelative(-6.73f, 6.75f)
							quadToRelative(-0.33f, 0.32f, -0.72f, 0.49f)
							reflectiveQuadTo(12f, 21f)
							close()
							moveTo(13f, 8f)
							quadToRelative(0.25f, 0f, 0.48f, 0.13f)
							reflectiveQuadToRelative(0.35f, 0.32f)
							lineTo(15.53f, 11f)
							horizontalLineToRelative(4.15f)
							quadToRelative(0.18f, -0.43f, 0.26f, -0.86f)
							quadTo(20.03f, 9.7f, 20.03f, 9.25f)
							quadTo(19.98f, 7.52f, 18.88f, 6.29f)
							quadTo(17.78f, 5.05f, 16.13f, 5.05f)
							quadToRelative(-0.77f, 0f, -1.49f, 0.3f)
							reflectiveQuadTo(13.4f, 6.22f)
							lineTo(12.73f, 6.95f)
							quadTo(12.6f, 7.1f, 12.4f, 7.19f)
							quadTo(12.2f, 7.27f, 12f, 7.27f)
							reflectiveQuadTo(11.6f, 7.19f)
							reflectiveQuadTo(11.25f, 6.95f)
							lineTo(10.58f, 6.22f)
							quadTo(10.05f, 5.65f, 9.35f, 5.32f)
							reflectiveQuadTo(7.85f, 5f)
							quadTo(6.2f, 5f, 5.1f, 6.26f)
							reflectiveQuadTo(4f, 9.25f)
							quadTo(4f, 9.7f, 4.08f, 10.14f)
							reflectiveQuadTo(4.33f, 11f)
							horizontalLineTo(9f)
							quadToRelative(0.25f, 0f, 0.47f, 0.13f)
							reflectiveQuadToRelative(0.35f, 0.32f)
							lineToRelative(0.88f, 1.3f)
							lineTo(12.05f, 8.7f)
							quadToRelative(0.1f, -0.3f, 0.36f, -0.5f)
							reflectiveQuadTo(13f, 8f)
							close()
							moveToRelative(0.3f, 3.25f)
							lineTo(11.95f, 15.3f)
							quadToRelative(-0.1f, 0.3f, -0.38f, 0.5f)
							reflectiveQuadTo(10.98f, 16f)
							quadTo(10.73f, 16f, 10.5f, 15.88f)
							quadTo(10.28f, 15.75f, 10.15f, 15.55f)
							lineTo(8.45f, 13f)
							horizontalLineTo(5.9f)
							lineToRelative(5.93f, 5.93f)
							quadToRelative(0.05f, 0.05f, 0.09f, 0.06f)
							reflectiveQuadTo(12f, 19f)
							reflectiveQuadToRelative(0.09f, -0.01f)
							reflectiveQuadToRelative(0.09f, -0.06f)
							lineTo(18.08f, 13f)
							horizontalLineTo(15f)
							quadToRelative(-0.25f, 0f, -0.47f, -0.13f)
							reflectiveQuadTo(14.15f, 12.55f)
							lineTo(13.3f, 11.25f)
							close()
						}
					}
					.build()
			return _ecg_heart!!
		}

	val toys: ImageVector
		get() {
			if (_toys != null) {
				return _toys!!
			}
			_toys =
				ImageVector.Builder(
					name = "toys",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(7f, 20f)
							quadTo(5.88f, 20f, 5.04f, 19.29f)
							reflectiveQuadTo(4.05f, 17.45f)
							quadTo(3.1f, 16.95f, 2.55f, 16.02f)
							reflectiveQuadTo(2f, 14f)
							quadTo(2f, 12.68f, 2.76f, 11.64f)
							reflectiveQuadTo(4.8f, 10.2f)
							lineTo(3f, 8.4f)
							lineTo(2.7f, 8.7f)
							quadTo(2.43f, 8.98f, 2f, 8.98f)
							quadTo(1.58f, 8.98f, 1.3f, 8.7f)
							quadTo(1.03f, 8.42f, 1.03f, 8f)
							quadTo(1.03f, 7.57f, 1.3f, 7.3f)
							lineToRelative(2f, -2f)
							quadTo(3.58f, 5.02f, 4f, 5.02f)
							reflectiveQuadTo(4.7f, 5.3f)
							quadTo(4.98f, 5.57f, 4.98f, 6f)
							reflectiveQuadTo(4.7f, 6.7f)
							lineTo(4.4f, 7f)
							lineTo(5.8f, 8.4f)
							lineTo(6.6f, 6.05f)
							quadTo(6.9f, 5.13f, 7.69f, 4.56f)
							reflectiveQuadTo(9.45f, 4f)
							horizontalLineToRelative(5.1f)
							quadToRelative(0.97f, 0f, 1.76f, 0.56f)
							reflectiveQuadTo(17.4f, 6.05f)
							lineToRelative(1.35f, 4.05f)
							quadToRelative(1.43f, 0.28f, 2.34f, 1.38f)
							reflectiveQuadTo(22f, 14f)
							quadToRelative(0f, 1.1f, -0.55f, 2.02f)
							reflectiveQuadToRelative(-1.5f, 1.43f)
							quadToRelative(-0.15f, 1.13f, -0.99f, 1.84f)
							reflectiveQuadTo(17f, 20f)
							quadToRelative(-0.95f, 0f, -1.71f, -0.55f)
							reflectiveQuadTo(14.2f, 18f)
							horizontalLineTo(9.8f)
							quadTo(9.48f, 18.9f, 8.71f, 19.45f)
							reflectiveQuadTo(7f, 20f)
							close()
							moveTo(7.4f, 10f)
							horizontalLineTo(11f)
							verticalLineTo(6f)
							horizontalLineTo(9.45f)
							quadTo(9.13f, 6f, 8.88f, 6.19f)
							reflectiveQuadTo(8.5f, 6.7f)
							lineTo(7.4f, 10f)
							close()
							moveTo(13f, 10f)
							horizontalLineToRelative(3.6f)
							lineTo(15.5f, 6.7f)
							quadTo(15.38f, 6.38f, 15.13f, 6.19f)
							reflectiveQuadTo(14.55f, 6f)
							horizontalLineTo(13f)
							verticalLineToRelative(4f)
							close()
							moveTo(9.8f, 16f)
							horizontalLineToRelative(4.4f)
							quadToRelative(0.32f, -0.9f, 1.09f, -1.45f)
							reflectiveQuadTo(17f, 14f)
							quadToRelative(0.75f, 0f, 1.4f, 0.35f)
							reflectiveQuadToRelative(1.1f, 0.95f)
							quadToRelative(0.23f, -0.27f, 0.36f, -0.61f)
							reflectiveQuadTo(20f, 14f)
							quadToRelative(0f, -0.83f, -0.59f, -1.41f)
							reflectiveQuadTo(18f, 12f)
							horizontalLineTo(6f)
							quadTo(5.18f, 12f, 4.59f, 12.59f)
							quadTo(4f, 13.18f, 4f, 14f)
							quadToRelative(0f, 0.35f, 0.14f, 0.69f)
							reflectiveQuadTo(4.5f, 15.3f)
							quadTo(4.95f, 14.7f, 5.6f, 14.35f)
							reflectiveQuadTo(7f, 14f)
							quadToRelative(0.95f, 0f, 1.71f, 0.55f)
							reflectiveQuadTo(9.8f, 16f)
							close()
							moveTo(7.71f, 17.71f)
							quadTo(8f, 17.43f, 8f, 17f)
							reflectiveQuadTo(7.71f, 16.29f)
							reflectiveQuadTo(7f, 16f)
							quadTo(6.58f, 16f, 6.29f, 16.29f)
							reflectiveQuadTo(6f, 17f)
							reflectiveQuadToRelative(0.29f, 0.71f)
							reflectiveQuadTo(7f, 18f)
							reflectiveQuadTo(7.71f, 17.71f)
							close()
							moveToRelative(10f, 0f)
							quadTo(18f, 17.43f, 18f, 17f)
							reflectiveQuadTo(17.71f, 16.29f)
							reflectiveQuadTo(17f, 16f)
							reflectiveQuadToRelative(-0.71f, 0.29f)
							reflectiveQuadTo(16f, 17f)
							reflectiveQuadToRelative(0.29f, 0.71f)
							reflectiveQuadTo(17f, 18f)
							reflectiveQuadToRelative(0.71f, -0.29f)
							close()
							moveTo(12f, 14f)
							close()
						}
					}
					.build()
			return _toys!!
		}

	val watchScreenTime: ImageVector
		get() {
			if (_watch != null) {
				return _watch!!
			}
			_watch =
				ImageVector.Builder(
					name = "watch_screentime",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(9f, 14f)
							verticalLineTo(10f)
							horizontalLineToRelative(1.5f)
							verticalLineToRelative(4f)
							horizontalLineTo(9f)
							close()
							moveToRelative(2.25f, 0f)
							verticalLineTo(11f)
							horizontalLineToRelative(1.5f)
							verticalLineToRelative(3f)
							horizontalLineToRelative(-1.5f)
							close()
							moveToRelative(2.25f, 0f)
							verticalLineTo(12f)
							horizontalLineTo(15f)
							verticalLineToRelative(2f)
							horizontalLineTo(13.5f)
							close()
							moveTo(10.5f, 4f)
							horizontalLineToRelative(3f)
							quadTo(13.3f, 4f, 12.89f, 4f)
							reflectiveQuadTo(12f, 4f)
							quadTo(11.53f, 4f, 11.11f, 4f)
							reflectiveQuadTo(10.5f, 4f)
							close()
							moveToRelative(0f, 16f)
							quadToRelative(0.2f, 0f, 0.61f, 0f)
							reflectiveQuadTo(12f, 20f)
							reflectiveQuadToRelative(0.89f, 0f)
							reflectiveQuadToRelative(0.61f, 0f)
							horizontalLineToRelative(-3f)
							close()
							moveTo(9f, 22f)
							lineTo(7.65f, 17.45f)
							quadTo(6.45f, 16.5f, 5.73f, 15.08f)
							reflectiveQuadTo(5f, 12f)
							reflectiveQuadTo(5.73f, 8.92f)
							reflectiveQuadTo(7.65f, 6.55f)
							lineTo(9f, 2f)
							horizontalLineToRelative(6f)
							lineToRelative(1.35f, 4.55f)
							quadToRelative(1.2f, 0.95f, 1.93f, 2.37f)
							reflectiveQuadTo(19f, 12f)
							reflectiveQuadToRelative(-0.72f, 3.07f)
							reflectiveQuadToRelative(-1.93f, 2.38f)
							lineTo(15f, 22f)
							horizontalLineTo(9f)
							close()
							moveToRelative(6.54f, -6.46f)
							quadTo(17f, 14.08f, 17f, 12f)
							quadTo(17f, 9.92f, 15.54f, 8.46f)
							reflectiveQuadTo(12f, 7f)
							quadTo(9.93f, 7f, 8.46f, 8.46f)
							reflectiveQuadTo(7f, 12f)
							reflectiveQuadToRelative(1.46f, 3.54f)
							reflectiveQuadTo(12f, 17f)
							reflectiveQuadToRelative(3.54f, -1.46f)
							close()
							moveTo(10.1f, 5.25f)
							quadToRelative(0.5f, -0.13f, 0.96f, -0.2f)
							quadTo(11.53f, 4.97f, 12f, 4.97f)
							reflectiveQuadToRelative(0.94f, 0.08f)
							reflectiveQuadToRelative(0.96f, 0.2f)
							lineTo(13.5f, 4f)
							horizontalLineToRelative(-3f)
							lineTo(10.1f, 5.25f)
							close()
							moveTo(10.5f, 20f)
							horizontalLineToRelative(3f)
							lineToRelative(0.4f, -1.25f)
							quadToRelative(-0.5f, 0.13f, -0.96f, 0.19f)
							reflectiveQuadTo(12f, 19f)
							quadToRelative(-0.47f, 0f, -0.94f, -0.06f)
							reflectiveQuadTo(10.1f, 18.75f)
							lineTo(10.5f, 20f)
							close()
						}
					}
					.build()
			return _watch!!
		}

	val radiology: ImageVector
		get() {
			if (_radiology != null) {
				return _radiology!!
			}
			_radiology =
				ImageVector.Builder(
					name = "radiology",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(5f, 22f)
							quadTo(4.18f, 22f, 3.59f, 21.41f)
							reflectiveQuadTo(3f, 20f)
							verticalLineTo(6f)
							quadTo(3f, 5.18f, 3.59f, 4.59f)
							reflectiveQuadTo(5f, 4f)
							horizontalLineTo(6f)
							verticalLineTo(2f)
							horizontalLineTo(8f)
							verticalLineTo(4f)
							horizontalLineToRelative(8f)
							verticalLineTo(2f)
							horizontalLineToRelative(2f)
							verticalLineTo(4f)
							horizontalLineToRelative(1f)
							quadToRelative(0.83f, 0f, 1.41f, 0.59f)
							quadTo(21f, 5.18f, 21f, 6f)
							verticalLineTo(20f)
							quadToRelative(0f, 0.82f, -0.59f, 1.41f)
							reflectiveQuadTo(19f, 22f)
							horizontalLineTo(5f)
							close()
							moveTo(5f, 20f)
							horizontalLineTo(19f)
							verticalLineTo(6f)
							horizontalLineTo(5f)
							verticalLineTo(20f)
							close()
							moveToRelative(4f, 0f)
							verticalLineTo(18.2f)
							lineTo(7.2f, 16.1f)
							quadTo(6.93f, 15.83f, 6.71f, 15.35f)
							reflectiveQuadTo(6.5f, 14.25f)
							quadToRelative(0f, -0.33f, 0.06f, -0.64f)
							reflectiveQuadTo(6.78f, 13f)
							quadTo(6.65f, 12.73f, 6.58f, 12.41f)
							reflectiveQuadTo(6.5f, 11.75f)
							quadToRelative(0f, -0.63f, 0.21f, -1.1f)
							reflectiveQuadTo(7.2f, 9.9f)
							lineTo(9f, 7.8f)
							verticalLineTo(6f)
							horizontalLineToRelative(1.5f)
							verticalLineTo(8.07f)
							quadToRelative(0f, 0.13f, -0.17f, 0.47f)
							lineToRelative(-2f, 2.35f)
							quadTo(8.15f, 11.1f, 8.08f, 11.31f)
							quadTo(8f, 11.52f, 8f, 11.75f)
							quadToRelative(0f, 0.5f, 0.33f, 0.86f)
							quadToRelative(0.32f, 0.36f, 0.82f, 0.36f)
							quadToRelative(0.22f, 0f, 0.43f, -0.08f)
							quadToRelative(0.2f, -0.07f, 0.35f, -0.25f)
							quadTo(10.35f, 12.23f, 10.89f, 12f)
							quadTo(11.43f, 11.77f, 12f, 11.77f)
							reflectiveQuadTo(13.11f, 12f)
							reflectiveQuadToRelative(0.96f, 0.65f)
							quadToRelative(0.17f, 0.18f, 0.38f, 0.25f)
							quadToRelative(0.2f, 0.08f, 0.4f, 0.08f)
							quadToRelative(0.5f, 0f, 0.82f, -0.36f)
							reflectiveQuadTo(16f, 11.77f)
							quadToRelative(0f, -0.22f, -0.09f, -0.44f)
							quadTo(15.83f, 11.13f, 15.68f, 10.93f)
							lineToRelative(-2f, -2.38f)
							quadTo(13.58f, 8.45f, 13.54f, 8.32f)
							reflectiveQuadTo(13.5f, 8.07f)
							verticalLineTo(6f)
							horizontalLineTo(15f)
							verticalLineTo(7.8f)
							lineToRelative(1.82f, 2.15f)
							quadToRelative(0.35f, 0.4f, 0.51f, 0.86f)
							quadToRelative(0.16f, 0.46f, 0.16f, 0.96f)
							quadToRelative(0f, 0.33f, -0.09f, 0.64f)
							reflectiveQuadTo(17.2f, 13f)
							quadToRelative(0.15f, 0.3f, 0.23f, 0.61f)
							reflectiveQuadToRelative(0.07f, 0.64f)
							quadToRelative(0f, 0.63f, -0.21f, 1.1f)
							reflectiveQuadTo(16.8f, 16.1f)
							lineTo(15f, 18.2f)
							verticalLineTo(20f)
							horizontalLineTo(13.5f)
							verticalLineTo(17.93f)
							quadToRelative(0f, -0.15f, 0.18f, -0.48f)
							lineToRelative(2f, -2.35f)
							quadToRelative(0.18f, -0.2f, 0.25f, -0.43f)
							reflectiveQuadTo(16f, 14.23f)
							quadToRelative(-0.27f, 0.13f, -0.55f, 0.19f)
							reflectiveQuadToRelative(-0.58f, 0.06f)
							quadToRelative(-0.5f, 0f, -1f, -0.2f)
							quadTo(13.38f, 14.08f, 13f, 13.68f)
							quadToRelative(-0.17f, -0.2f, -0.44f, -0.3f)
							quadTo(12.3f, 13.27f, 12f, 13.27f)
							quadToRelative(-0.27f, 0f, -0.54f, 0.1f)
							reflectiveQuadTo(11f, 13.68f)
							quadToRelative(-0.38f, 0.4f, -0.86f, 0.6f)
							quadToRelative(-0.49f, 0.2f, -0.99f, 0.2f)
							quadToRelative(-0.3f, 0f, -0.59f, -0.06f)
							reflectiveQuadTo(8f, 14.23f)
							quadToRelative(0f, 0.22f, 0.08f, 0.45f)
							reflectiveQuadTo(8.33f, 15.1f)
							lineToRelative(2f, 2.35f)
							quadToRelative(0.07f, 0.13f, 0.13f, 0.24f)
							reflectiveQuadToRelative(0.05f, 0.24f)
							verticalLineTo(20f)
							horizontalLineTo(9f)
							close()
							moveTo(5f, 20f)
							verticalLineTo(6f)
							verticalLineTo(20f)
							close()
						}
					}
					.build()
			return _radiology!!
		}

	val usb: ImageVector
		get() {
			if (_usb != null) {
				return _usb!!
			}
			_usb =
				ImageVector.Builder(
					name = "usb",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(10.59f, 21.41f)
							quadTo(10f, 20.83f, 10f, 20f)
							quadToRelative(0f, -0.52f, 0.28f, -0.98f)
							quadTo(10.55f, 18.58f, 11f, 18.3f)
							verticalLineTo(16f)
							horizontalLineTo(8f)
							quadTo(7.18f, 16f, 6.59f, 15.41f)
							reflectiveQuadTo(6f, 14f)
							verticalLineTo(11.7f)
							quadTo(5.55f, 11.48f, 5.28f, 11.02f)
							quadTo(5f, 10.58f, 5f, 10f)
							quadTo(5f, 9.17f, 5.59f, 8.59f)
							reflectiveQuadTo(7f, 8f)
							quadTo(7.83f, 8f, 8.41f, 8.59f)
							reflectiveQuadTo(9f, 10f)
							quadToRelative(0f, 0.57f, -0.28f, 1f)
							reflectiveQuadTo(8f, 11.7f)
							verticalLineTo(14f)
							horizontalLineToRelative(3f)
							verticalLineTo(6f)
							horizontalLineTo(9f)
							lineTo(12f, 2f)
							lineToRelative(3f, 4f)
							horizontalLineTo(13f)
							verticalLineToRelative(8f)
							horizontalLineToRelative(3f)
							verticalLineTo(12f)
							horizontalLineTo(15f)
							verticalLineTo(8f)
							horizontalLineToRelative(4f)
							verticalLineToRelative(4f)
							horizontalLineTo(18f)
							verticalLineToRelative(2f)
							quadToRelative(0f, 0.82f, -0.59f, 1.41f)
							reflectiveQuadTo(16f, 16f)
							horizontalLineTo(13f)
							verticalLineToRelative(2.3f)
							quadToRelative(0.48f, 0.25f, 0.74f, 0.7f)
							reflectiveQuadTo(14f, 20f)
							quadToRelative(0f, 0.82f, -0.59f, 1.41f)
							reflectiveQuadTo(12f, 22f)
							reflectiveQuadTo(10.59f, 21.41f)
							close()
						}
					}
					.build()
			return _usb!!
		}

	val audioVideoReceiver: ImageVector
		get() {
			if (_audio_video_receiver != null) {
				return _audio_video_receiver!!
			}
			_audio_video_receiver =
				ImageVector.Builder(
					name = "audio_video_receiver",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(4f, 15f)
							horizontalLineTo(20f)
							verticalLineTo(7f)
							horizontalLineTo(4f)
							verticalLineToRelative(8f)
							close()
							moveToRelative(1f, 4f)
							quadTo(4.58f, 19f, 4.29f, 18.71f)
							quadTo(4f, 18.43f, 4f, 18f)
							verticalLineTo(17f)
							quadTo(3.18f, 17f, 2.59f, 16.41f)
							reflectiveQuadTo(2f, 15f)
							verticalLineTo(7f)
							quadTo(2f, 6.18f, 2.59f, 5.59f)
							reflectiveQuadTo(4f, 5f)
							horizontalLineTo(20f)
							quadToRelative(0.83f, 0f, 1.41f, 0.59f)
							quadTo(22f, 6.18f, 22f, 7f)
							verticalLineToRelative(8f)
							quadToRelative(0f, 0.82f, -0.59f, 1.41f)
							reflectiveQuadTo(20f, 17f)
							verticalLineToRelative(1f)
							quadToRelative(0f, 0.43f, -0.29f, 0.71f)
							reflectiveQuadTo(19f, 19f)
							reflectiveQuadTo(18.29f, 18.71f)
							quadTo(18f, 18.43f, 18f, 18f)
							verticalLineTo(17f)
							horizontalLineTo(6f)
							verticalLineToRelative(1f)
							quadToRelative(0f, 0.43f, -0.29f, 0.71f)
							reflectiveQuadTo(5f, 19f)
							close()
							moveTo(4f, 15f)
							verticalLineTo(7f)
							verticalLineToRelative(8f)
							close()
							moveTo(17.91f, 12.41f)
							quadTo(18.5f, 11.83f, 18.5f, 11f)
							quadToRelative(0f, -0.83f, -0.59f, -1.41f)
							reflectiveQuadTo(16.5f, 9f)
							reflectiveQuadTo(15.09f, 9.59f)
							reflectiveQuadTo(14.5f, 11f)
							reflectiveQuadToRelative(0.59f, 1.41f)
							reflectiveQuadTo(16.5f, 13f)
							reflectiveQuadToRelative(1.41f, -0.59f)
							close()
							moveTo(6f, 13f)
							horizontalLineToRelative(7f)
							verticalLineTo(9f)
							horizontalLineTo(6f)
							verticalLineToRelative(4f)
							close()
						}
					}
					.build()
			return _audio_video_receiver!!
		}

	val networkNode: ImageVector
		get() {
			if (_network_node != null) {
				return _network_node!!
			}
			_network_node =
				ImageVector.Builder(
					name = "network_node",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(3.03f, 20.98f)
							quadTo(2f, 19.95f, 2f, 18.5f)
							reflectiveQuadTo(3.03f, 16.02f)
							reflectiveQuadTo(5.5f, 15f)
							quadToRelative(0.45f, 0f, 0.88f, 0.11f)
							quadToRelative(0.43f, 0.11f, 0.8f, 0.31f)
							lineTo(11f, 11.6f)
							verticalLineTo(8.85f)
							quadTo(9.9f, 8.52f, 9.2f, 7.61f)
							reflectiveQuadTo(8.5f, 5.5f)
							quadTo(8.5f, 4.05f, 9.53f, 3.02f)
							reflectiveQuadTo(12f, 2f)
							reflectiveQuadToRelative(2.48f, 1.02f)
							reflectiveQuadTo(15.5f, 5.5f)
							quadToRelative(0f, 1.2f, -0.7f, 2.11f)
							quadTo(14.1f, 8.52f, 13f, 8.85f)
							verticalLineTo(11.6f)
							lineToRelative(3.85f, 3.82f)
							quadToRelative(0.38f, -0.2f, 0.79f, -0.31f)
							reflectiveQuadTo(18.5f, 15f)
							quadToRelative(1.45f, 0f, 2.48f, 1.02f)
							reflectiveQuadTo(22f, 18.5f)
							reflectiveQuadToRelative(-1.02f, 2.48f)
							reflectiveQuadTo(18.5f, 22f)
							reflectiveQuadTo(16.03f, 20.98f)
							reflectiveQuadTo(15f, 18.5f)
							quadToRelative(0f, -0.45f, 0.11f, -0.88f)
							reflectiveQuadToRelative(0.31f, -0.8f)
							lineTo(12f, 13.4f)
							lineTo(8.58f, 16.83f)
							quadToRelative(0.2f, 0.38f, 0.31f, 0.8f)
							reflectiveQuadTo(9f, 18.5f)
							quadToRelative(0f, 1.45f, -1.02f, 2.48f)
							reflectiveQuadTo(5.5f, 22f)
							reflectiveQuadTo(3.03f, 20.98f)
							close()
							moveTo(19.56f, 19.56f)
							quadTo(20f, 19.13f, 20f, 18.5f)
							reflectiveQuadTo(19.56f, 17.44f)
							reflectiveQuadTo(18.5f, 17f)
							reflectiveQuadToRelative(-1.06f, 0.44f)
							reflectiveQuadTo(17f, 18.5f)
							reflectiveQuadToRelative(0.44f, 1.06f)
							reflectiveQuadTo(18.5f, 20f)
							reflectiveQuadToRelative(1.06f, -0.44f)
							close()
							moveToRelative(-6.5f, -13f)
							quadTo(13.5f, 6.13f, 13.5f, 5.5f)
							reflectiveQuadTo(13.06f, 4.44f)
							reflectiveQuadTo(12f, 4f)
							reflectiveQuadTo(10.94f, 4.44f)
							reflectiveQuadTo(10.5f, 5.5f)
							reflectiveQuadToRelative(0.44f, 1.06f)
							reflectiveQuadTo(12f, 7f)
							reflectiveQuadTo(13.06f, 6.56f)
							close()
							moveToRelative(-6.5f, 13f)
							quadTo(7f, 19.13f, 7f, 18.5f)
							reflectiveQuadTo(6.56f, 17.44f)
							reflectiveQuadTo(5.5f, 17f)
							reflectiveQuadTo(4.44f, 17.44f)
							reflectiveQuadTo(4f, 18.5f)
							reflectiveQuadToRelative(0.44f, 1.06f)
							reflectiveQuadTo(5.5f, 20f)
							reflectiveQuadTo(6.56f, 19.56f)
							close()
						}
					}
					.build()
			return _network_node!!
		}

	val mobileHand: ImageVector
		get() {
			if (_mobile_hand != null) {
				return _mobile_hand!!
			}
			_mobile_hand =
				ImageVector.Builder(
					name = "mobile_hand",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(6f, 20f)
							verticalLineTo(4f)
							verticalLineTo(6f)
							verticalLineToRelative(7f)
							verticalLineToRelative(7f)
							close()
							moveToRelative(9.93f, 3f)
							quadTo(15.28f, 23f, 14.7f, 22.74f)
							reflectiveQuadTo(13.7f, 22f)
							lineTo(8.65f, 15.95f)
							lineTo(9.78f, 14.8f)
							quadToRelative(0.45f, -0.45f, 1.1f, -0.56f)
							quadToRelative(0.65f, -0.11f, 1.23f, 0.19f)
							lineTo(15f, 15.88f)
							verticalLineTo(7f)
							horizontalLineToRelative(2f)
							quadToRelative(1.65f, 0f, 2.83f, 1.17f)
							reflectiveQuadTo(21f, 11f)
							verticalLineToRelative(8f)
							quadToRelative(0f, 1.65f, -1.17f, 2.82f)
							reflectiveQuadTo(17f, 23f)
							horizontalLineTo(15.93f)
							close()
							moveTo(6f, 22f)
							quadTo(5.18f, 22f, 4.59f, 21.41f)
							reflectiveQuadTo(4f, 20f)
							verticalLineTo(4f)
							quadTo(4f, 3.17f, 4.59f, 2.59f)
							reflectiveQuadTo(6f, 2f)
							horizontalLineToRelative(9f)
							quadToRelative(0.83f, 0f, 1.41f, 0.59f)
							reflectiveQuadTo(17f, 4f)
							verticalLineTo(7f)
							horizontalLineTo(15f)
							verticalLineTo(4f)
							horizontalLineTo(6f)
							verticalLineTo(20f)
							horizontalLineToRelative(6.03f)
							lineToRelative(1.68f, 2f)
							horizontalLineTo(6f)
							close()
							moveTo(11.21f, 6.71f)
							quadTo(11.5f, 6.43f, 11.5f, 6f)
							reflectiveQuadTo(11.21f, 5.29f)
							reflectiveQuadTo(10.5f, 5f)
							quadTo(10.08f, 5f, 9.79f, 5.29f)
							reflectiveQuadTo(9.5f, 6f)
							reflectiveQuadTo(9.79f, 6.71f)
							reflectiveQuadTo(10.5f, 7f)
							reflectiveQuadTo(11.21f, 6.71f)
							close()
							moveTo(15.93f, 21f)
							horizontalLineTo(17f)
							quadToRelative(0.82f, 0f, 1.41f, -0.57f)
							reflectiveQuadTo(19f, 19f)
							verticalLineTo(11f)
							quadTo(19f, 10.17f, 18.41f, 9.59f)
							reflectiveQuadTo(17f, 9f)
							verticalLineTo(19.13f)
							lineTo(11.7f, 16.45f)
							lineToRelative(3.45f, 4.2f)
							quadToRelative(0.15f, 0.18f, 0.35f, 0.26f)
							reflectiveQuadTo(15.93f, 21f)
							close()
						}
					}
					.build()
			return _mobile_hand!!
		}

	val computerConnected: ImageVector
		get() {
			if (_computer_sound != null) {
				return _computer_sound!!
			}
			_computer_sound =
				ImageVector.Builder(
					name = "computer_sound",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(4f, 16f)
							verticalLineTo(5f)
							verticalLineToRelative(5.48f)
							verticalLineTo(16f)
							close()
							moveTo(1f, 21f)
							verticalLineTo(19f)
							horizontalLineTo(23f)
							verticalLineToRelative(2f)
							horizontalLineTo(1f)
							close()
							moveTo(20.18f, 18f)
							horizontalLineTo(4f)
							quadTo(3.18f, 18f, 2.59f, 17.41f)
							reflectiveQuadTo(2f, 16f)
							verticalLineTo(5f)
							quadTo(2f, 4.17f, 2.59f, 3.59f)
							reflectiveQuadTo(4f, 3f)
							horizontalLineTo(20.18f)
							lineToRelative(-2f, 2f)
							horizontalLineTo(4f)
							verticalLineTo(16f)
							horizontalLineTo(18.18f)
							lineToRelative(2f, 2f)
							close()
							moveTo(18.33f, 13.35f)
							lineTo(16.88f, 11.9f)
							quadToRelative(0.3f, -0.27f, 0.46f, -0.64f)
							reflectiveQuadTo(17.5f, 10.5f)
							reflectiveQuadTo(17.34f, 9.74f)
							quadTo(17.18f, 9.38f, 16.88f, 9.1f)
							lineTo(18.33f, 7.65f)
							quadTo(18.9f, 8.23f, 19.2f, 8.96f)
							reflectiveQuadToRelative(0.3f, 1.54f)
							reflectiveQuadToRelative(-0.3f, 1.54f)
							quadToRelative(-0.3f, 0.74f, -0.88f, 1.31f)
							close()
							moveToRelative(2.45f, 2.45f)
							lineToRelative(-1.4f, -1.4f)
							quadToRelative(0.8f, -0.8f, 1.21f, -1.8f)
							reflectiveQuadTo(21f, 10.5f)
							reflectiveQuadTo(20.59f, 8.4f)
							reflectiveQuadTo(19.38f, 6.6f)
							lineToRelative(1.4f, -1.4f)
							quadToRelative(1.07f, 1.05f, 1.65f, 2.43f)
							reflectiveQuadTo(23f, 10.5f)
							reflectiveQuadToRelative(-0.57f, 2.88f)
							reflectiveQuadTo(20.78f, 15.8f)
							close()
						}
					}
					.build()
			return _computer_sound!!
		}

	val homeStorageGear: ImageVector
		get() {
			if (_home_storage_gear != null) {
				return _home_storage_gear!!
			}
			_home_storage_gear =
				ImageVector.Builder(
					name = "home_storage_gear",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(6f, 6f)
							horizontalLineTo(18f)
							quadToRelative(0.43f, 0f, 0.71f, 0.29f)
							reflectiveQuadTo(19f, 7f)
							reflectiveQuadTo(18.71f, 7.71f)
							reflectiveQuadTo(18f, 8f)
							horizontalLineTo(6f)
							quadTo(5.58f, 8f, 5.29f, 7.71f)
							quadTo(5f, 7.43f, 5f, 7f)
							reflectiveQuadTo(5.29f, 6.29f)
							reflectiveQuadTo(6f, 6f)
							close()
							moveTo(8f, 3f)
							horizontalLineToRelative(8f)
							quadToRelative(0.43f, 0f, 0.71f, 0.29f)
							reflectiveQuadTo(17f, 4f)
							quadToRelative(0f, 0.42f, -0.29f, 0.71f)
							reflectiveQuadTo(16f, 5f)
							horizontalLineTo(8f)
							quadTo(7.58f, 5f, 7.29f, 4.71f)
							reflectiveQuadTo(7f, 4f)
							quadTo(7f, 3.57f, 7.29f, 3.29f)
							reflectiveQuadTo(8f, 3f)
							close()
							moveTo(5.4f, 11f)
							quadToRelative(0f, 0f, 0.35f, 2.15f)
							reflectiveQuadTo(6.68f, 19f)
							lineTo(5.4f, 11f)
							close()
							moveToRelative(6.43f, 10f)
							horizontalLineTo(5f)
							lineTo(3f, 9f)
							horizontalLineTo(21f)
							lineToRelative(-0.35f, 2f)
							quadToRelative(-0.07f, 0f, -0.84f, 0f)
							reflectiveQuadToRelative(-1.44f, 0f)
							horizontalLineTo(5.4f)
							lineToRelative(1.28f, 8f)
							horizontalLineToRelative(4.55f)
							quadToRelative(0.07f, 0.55f, 0.24f, 1.05f)
							reflectiveQuadTo(11.83f, 21f)
							close()
							moveToRelative(5.35f, 2.02f)
							lineToRelative(-0.3f, -1.5f)
							quadTo(16.58f, 21.4f, 16.31f, 21.26f)
							reflectiveQuadTo(15.78f, 20.93f)
							lineToRelative(-1.45f, 0.45f)
							lineToRelative(-1f, -1.7f)
							lineToRelative(1.15f, -1f)
							quadTo(14.43f, 18.35f, 14.43f, 18.02f)
							reflectiveQuadToRelative(0.05f, -0.65f)
							lineToRelative(-1.15f, -1f)
							lineToRelative(1f, -1.7f)
							lineToRelative(1.45f, 0.45f)
							quadToRelative(0.28f, -0.2f, 0.54f, -0.34f)
							reflectiveQuadToRelative(0.56f, -0.26f)
							lineToRelative(0.3f, -1.5f)
							horizontalLineToRelative(2f)
							lineToRelative(0.3f, 1.5f)
							quadToRelative(0.3f, 0.13f, 0.56f, 0.26f)
							reflectiveQuadToRelative(0.54f, 0.34f)
							lineToRelative(1.45f, -0.45f)
							lineToRelative(1f, 1.7f)
							lineToRelative(-1.15f, 1f)
							quadToRelative(0.05f, 0.32f, 0.05f, 0.65f)
							reflectiveQuadToRelative(-0.05f, 0.65f)
							lineToRelative(1.15f, 1f)
							lineToRelative(-1f, 1.7f)
							lineTo(20.58f, 20.93f)
							quadToRelative(-0.28f, 0.2f, -0.54f, 0.34f)
							reflectiveQuadToRelative(-0.56f, 0.26f)
							lineToRelative(-0.3f, 1.5f)
							horizontalLineToRelative(-2f)
							close()
							moveToRelative(2.41f, -3.59f)
							quadToRelative(0.59f, -0.59f, 0.59f, -1.41f)
							reflectiveQuadTo(19.59f, 16.61f)
							quadTo(19f, 16.02f, 18.18f, 16.02f)
							reflectiveQuadToRelative(-1.41f, 0.59f)
							quadToRelative(-0.59f, 0.59f, -0.59f, 1.41f)
							reflectiveQuadToRelative(0.59f, 1.41f)
							reflectiveQuadToRelative(1.41f, 0.59f)
							reflectiveQuadToRelative(1.41f, -0.59f)
							close()
						}
					}
					.build()
			return _home_storage_gear!!
		}

	val deviceUnknown: ImageVector
		get() {
			if (_question_mark != null) {
				return _question_mark!!
			}
			_question_mark =
				ImageVector.Builder(
					name = "question_mark",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(10.6f, 16f)
							quadToRelative(0f, -2.03f, 0.36f, -2.91f)
							reflectiveQuadTo(12.5f, 11.15f)
							quadToRelative(1.03f, -0.9f, 1.56f, -1.56f)
							reflectiveQuadTo(14.6f, 8.07f)
							quadToRelative(0f, -1.02f, -0.69f, -1.7f)
							reflectiveQuadTo(12f, 5.7f)
							quadToRelative(-1.27f, 0f, -1.94f, 0.77f)
							reflectiveQuadTo(9.13f, 8.05f)
							lineTo(6.55f, 6.95f)
							quadTo(7.08f, 5.35f, 8.48f, 4.17f)
							reflectiveQuadTo(12f, 3f)
							quadToRelative(2.63f, 0f, 4.04f, 1.46f)
							quadToRelative(1.41f, 1.46f, 1.41f, 3.51f)
							quadToRelative(0f, 1.25f, -0.54f, 2.14f)
							reflectiveQuadToRelative(-1.69f, 2.01f)
							quadTo(14f, 13.3f, 13.74f, 13.91f)
							reflectiveQuadTo(13.48f, 16f)
							horizontalLineTo(10.6f)
							close()
							moveTo(12f, 22f)
							quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
							reflectiveQuadTo(10f, 20f)
							reflectiveQuadToRelative(0.59f, -1.41f)
							reflectiveQuadTo(12f, 18f)
							reflectiveQuadToRelative(1.41f, 0.59f)
							quadTo(14f, 19.18f, 14f, 20f)
							reflectiveQuadToRelative(-0.59f, 1.41f)
							reflectiveQuadTo(12f, 22f)
							close()
						}
					}
					.build()
			return _question_mark!!
		}
	val rule: ImageVector
		get() {
			if (_rule != null) {
				return _rule!!
			}
			_rule =
				ImageVector.Builder(
					name = "rule",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(14.4f, 20f)
							lineTo(13f, 18.6f)
							lineTo(15.6f, 16f)
							lineTo(13f, 13.4f)
							lineTo(14.4f, 12f)
							lineTo(17f, 14.6f)
							lineTo(19.6f, 12f)
							lineTo(21f, 13.4f)
							lineTo(18.4f, 16f)
							lineTo(21f, 18.6f)
							lineTo(19.6f, 20f)
							lineTo(17f, 17.4f)
							lineTo(14.4f, 20f)
							close()
							moveToRelative(1.97f, -9f)
							lineTo(12.83f, 7.45f)
							lineToRelative(1.4f, -1.4f)
							lineToRelative(2.13f, 2.12f)
							lineTo(20.6f, 3.92f)
							lineTo(22f, 5.35f)
							lineTo(16.38f, 11f)
							close()
							moveTo(2f, 17f)
							verticalLineTo(15f)
							horizontalLineToRelative(9f)
							verticalLineToRelative(2f)
							horizontalLineTo(2f)
							close()
							moveTo(2f, 9f)
							verticalLineTo(7f)
							horizontalLineToRelative(9f)
							verticalLineTo(9f)
							horizontalLineTo(2f)
							close()
						}
					}
					.build()
			return _rule!!
		}

	val close: ImageVector
		get() {
			if (_close != null) {
				return _close!!
			}
			_close =
				ImageVector.Builder(
					name = "close",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(6.4f, 19f)
							lineTo(5f, 17.6f)
							lineTo(10.6f, 12f)
							lineTo(5f, 6.4f)
							lineTo(6.4f, 5f)
							lineTo(12f, 10.6f)
							lineTo(17.6f, 5f)
							lineTo(19f, 6.4f)
							lineTo(13.4f, 12f)
							lineTo(19f, 17.6f)
							lineTo(17.6f, 19f)
							lineTo(12f, 13.4f)
							lineTo(6.4f, 19f)
							close()
						}
					}
					.build()
			return _close!!
		}
	val email: ImageVector
		get() {
			if (_alternate_email != null) {
				return _alternate_email!!
			}
			_alternate_email =
				ImageVector.Builder(
					name = "alternate_email",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(12f, 22f)
							quadTo(9.93f, 22f, 8.1f, 21.21f)
							quadTo(6.28f, 20.43f, 4.93f, 19.08f)
							quadTo(3.58f, 17.73f, 2.79f, 15.9f)
							reflectiveQuadTo(2f, 12f)
							quadTo(2f, 9.92f, 2.79f, 8.1f)
							quadTo(3.58f, 6.27f, 4.93f, 4.93f)
							quadTo(6.28f, 3.57f, 8.1f, 2.79f)
							quadTo(9.93f, 2f, 12f, 2f)
							reflectiveQuadToRelative(3.9f, 0.79f)
							reflectiveQuadToRelative(3.17f, 2.14f)
							quadToRelative(1.35f, 1.35f, 2.14f, 3.17f)
							quadTo(22f, 9.92f, 22f, 12f)
							verticalLineToRelative(1.45f)
							quadToRelative(0f, 1.47f, -1.01f, 2.51f)
							reflectiveQuadTo(18.5f, 17f)
							quadToRelative(-0.88f, 0f, -1.65f, -0.38f)
							reflectiveQuadToRelative(-1.3f, -1.08f)
							quadToRelative(-0.72f, 0.72f, -1.64f, 1.09f)
							reflectiveQuadTo(12f, 17f)
							quadTo(9.93f, 17f, 8.46f, 15.54f)
							reflectiveQuadTo(7f, 12f)
							quadTo(7f, 9.92f, 8.46f, 8.46f)
							reflectiveQuadTo(12f, 7f)
							reflectiveQuadToRelative(3.54f, 1.46f)
							reflectiveQuadTo(17f, 12f)
							verticalLineToRelative(1.45f)
							quadToRelative(0f, 0.65f, 0.43f, 1.1f)
							reflectiveQuadTo(18.5f, 15f)
							reflectiveQuadToRelative(1.08f, -0.45f)
							reflectiveQuadTo(20f, 13.45f)
							verticalLineTo(12f)
							quadTo(20f, 8.65f, 17.68f, 6.32f)
							reflectiveQuadTo(12f, 4f)
							reflectiveQuadTo(6.33f, 6.32f)
							reflectiveQuadTo(4f, 12f)
							reflectiveQuadToRelative(2.33f, 5.68f)
							reflectiveQuadTo(12f, 20f)
							horizontalLineToRelative(5f)
							verticalLineToRelative(2f)
							horizontalLineTo(12f)
							close()
							moveToRelative(2.13f, -7.88f)
							quadTo(15f, 13.25f, 15f, 12f)
							reflectiveQuadTo(14.13f, 9.88f)
							reflectiveQuadTo(12f, 9f)
							reflectiveQuadTo(9.88f, 9.88f)
							reflectiveQuadTo(9f, 12f)
							reflectiveQuadToRelative(0.88f, 2.13f)
							reflectiveQuadTo(12f, 15f)
							reflectiveQuadToRelative(2.13f, -0.88f)
							close()
						}
					}
					.build()
			return _alternate_email!!
		}
	val numbers: ImageVector
		get() {
			if (_numbers != null) {
				return _numbers!!
			}
			_numbers =
				ImageVector.Builder(
					name = "numbers",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(6f, 20f)
							lineTo(7f, 16f)
							horizontalLineTo(3f)
							lineTo(3.5f, 14f)
							horizontalLineToRelative(4f)
							lineToRelative(1f, -4f)
							horizontalLineToRelative(-4f)
							lineTo(5f, 8f)
							horizontalLineTo(9f)
							lineTo(10f, 4f)
							horizontalLineToRelative(2f)
							lineTo(11f, 8f)
							horizontalLineToRelative(4f)
							lineTo(16f, 4f)
							horizontalLineToRelative(2f)
							lineTo(17f, 8f)
							horizontalLineToRelative(4f)
							lineToRelative(-0.5f, 2f)
							horizontalLineToRelative(-4f)
							lineToRelative(-1f, 4f)
							horizontalLineToRelative(4f)
							lineTo(19f, 16f)
							horizontalLineTo(15f)
							lineToRelative(-1f, 4f)
							horizontalLineTo(12f)
							lineToRelative(1f, -4f)
							horizontalLineTo(9f)
							lineTo(8f, 20f)
							horizontalLineTo(6f)
							close()
							moveTo(9.5f, 14f)
							horizontalLineToRelative(4f)
							lineToRelative(1f, -4f)
							horizontalLineToRelative(-4f)
							lineToRelative(-1f, 4f)
							close()
						}
					}
					.build()
			return _numbers!!
		}
	val speed_3: ImageVector
		get() {
			if (_speed_3 != null) {
				return _speed_3!!
			}
			_speed_3 =
				ImageVector.Builder(
					name = "speed_3",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(5.13f, 20f)
							quadTo(4.58f, 20f, 4.11f, 19.76f)
							reflectiveQuadTo(3.38f, 19.05f)
							quadTo(2.7f, 17.9f, 2.35f, 16.63f)
							reflectiveQuadTo(2.03f, 14f)
							quadToRelative(0f, -2.35f, 1.04f, -4.44f)
							quadTo(4.1f, 7.47f, 5.98f, 6.05f)
							lineTo(6.5f, 8.23f)
							quadTo(5.3f, 9.35f, 4.66f, 10.85f)
							reflectiveQuadTo(4.03f, 14f)
							quadToRelative(0f, 1.05f, 0.27f, 2.07f)
							quadTo(4.58f, 17.1f, 5.13f, 18f)
							horizontalLineTo(18.9f)
							quadToRelative(0.52f, -0.93f, 0.81f, -1.94f)
							reflectiveQuadTo(20f, 14f)
							quadTo(20f, 10.65f, 17.68f, 8.32f)
							reflectiveQuadTo(12f, 6f)
							quadTo(11.75f, 6f, 11.51f, 6.01f)
							reflectiveQuadTo(11.03f, 6.07f)
							lineTo(9.7f, 4.27f)
							quadToRelative(0.57f, -0.13f, 1.15f, -0.2f)
							reflectiveQuadTo(12f, 4f)
							quadToRelative(2.08f, 0f, 3.9f, 0.79f)
							reflectiveQuadToRelative(3.17f, 2.14f)
							quadToRelative(1.35f, 1.35f, 2.14f, 3.17f)
							reflectiveQuadTo(22f, 14f)
							quadToRelative(0f, 1.35f, -0.35f, 2.61f)
							reflectiveQuadToRelative(-1f, 2.44f)
							quadToRelative(-0.28f, 0.47f, -0.74f, 0.71f)
							reflectiveQuadTo(18.9f, 20f)
							horizontalLineTo(5.13f)
							close()
							moveToRelative(7.7f, -4.2f)
							quadToRelative(0.85f, -0.38f, 1.15f, -1.22f)
							reflectiveQuadTo(13.78f, 13f)
							quadTo(12.33f, 10.93f, 10.84f, 8.92f)
							quadTo(9.35f, 6.93f, 7.78f, 4.95f)
							quadToRelative(0.47f, 2.48f, 1.06f, 4.9f)
							reflectiveQuadToRelative(1.31f, 4.85f)
							quadToRelative(0.25f, 0.83f, 1.06f, 1.14f)
							reflectiveQuadTo(12.83f, 15.8f)
							close()
							moveToRelative(-0.8f, -1.82f)
							close()
						}
					}
					.build()
			return _speed_3!!
		}

	val menu: ImageVector
		get() {
			if (_menu != null) {
				return _menu!!
			}
			_menu =
				ImageVector.Builder(
					name = "menu",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(3f, 18f)
							verticalLineTo(16f)
							horizontalLineTo(21f)
							verticalLineToRelative(2f)
							horizontalLineTo(3f)
							close()
							moveTo(3f, 13f)
							verticalLineTo(11f)
							horizontalLineTo(21f)
							verticalLineToRelative(2f)
							horizontalLineTo(3f)
							close()
							moveTo(3f, 8f)
							verticalLineTo(6f)
							horizontalLineTo(21f)
							verticalLineTo(8f)
							horizontalLineTo(3f)
							close()
						}
					}
					.build()
			return _menu!!
		}

	val menu_open: ImageVector
		get() {
			if (_menu_open != null) {
				return _menu_open!!
			}
			_menu_open =
				ImageVector.Builder(
					name = "menu_open",
					defaultWidth = 24.dp,
					defaultHeight = 24.dp,
					viewportWidth = 24f,
					viewportHeight = 24f,
				)
					.apply {
						path(
							fill = SolidColor(Color.Black),
							fillAlpha = 1f,
							stroke = null,
							strokeAlpha = 1f,
							strokeLineWidth = 1f,
							strokeLineCap = StrokeCap.Butt,
							strokeLineJoin = StrokeJoin.Bevel,
							strokeLineMiter = 1f,
							pathFillType = PathFillType.Companion.NonZero,
						) {
							moveTo(3f, 18f)
							verticalLineTo(16f)
							horizontalLineTo(16f)
							verticalLineToRelative(2f)
							horizontalLineTo(3f)
							close()
							moveTo(19.6f, 17f)
							lineToRelative(-5f, -5f)
							lineToRelative(5f, -5f)
							lineTo(21f, 8.4f)
							lineTo(17.4f, 12f)
							lineTo(21f, 15.6f)
							lineTo(19.6f, 17f)
							close()
							moveTo(3f, 13f)
							verticalLineTo(11f)
							horizontalLineTo(13f)
							verticalLineToRelative(2f)
							horizontalLineTo(3f)
							close()
							moveTo(3f, 8f)
							verticalLineTo(6f)
							horizontalLineTo(16f)
							verticalLineTo(8f)
							horizontalLineTo(3f)
							close()
						}
					}
					.build()
			return _menu_open!!
		}

	private var _menu_open: ImageVector? = null
	private var _menu: ImageVector? = null
	private var _speed_3: ImageVector? = null
	private var _numbers: ImageVector? = null
	private var _alternate_email: ImageVector? = null
	private var _close: ImageVector? = null
	private var _rule: ImageVector? = null
	private var _question_mark: ImageVector? = null
	private var _home_storage_gear: ImageVector? = null
	private var _computer_sound: ImageVector? = null
	private var _mobile_hand: ImageVector? = null
	private var _network_node: ImageVector? = null
	private var _audio_video_receiver: ImageVector? = null
	private var _usb: ImageVector? = null
	private var _radiology: ImageVector? = null
	private var _watch: ImageVector? = null
	private var _toys: ImageVector? = null
	private var _ecg_heart: ImageVector? = null
	private var _cable: ImageVector? = null
	private var _more_vert: ImageVector? = null
	private var _bluetooth_disabled: ImageVector? = null
	private var _location_on: ImageVector? = null
	private var _bluetooth: ImageVector? = null
	private var _arrow_back: ImageVector? = null
}
