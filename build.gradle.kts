// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.jetbrainsKotlinAndroid) apply false
	alias(libs.plugins.google.devtools.ksp) apply false
	alias(libs.plugins.google.protobuf) apply false
	alias(libs.plugins.compose.compiler) apply false
}