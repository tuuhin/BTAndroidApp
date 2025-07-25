import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.jetbrainsKotlinAndroid)
	alias(libs.plugins.google.devtools.ksp)
	alias(libs.plugins.kotlinx.serialization)
	alias(libs.plugins.google.protobuf)
	alias(libs.plugins.compose.compiler)
	id("kotlin-parcelize")
}

android {
	namespace = "com.eva.bluetoothterminalapp"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.eva.bluetoothterminalapp"
		minSdk = 29
		targetSdk = 36
		versionCode = 2
		versionName = "1.1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	signingConfigs {
		// find if there is a properties file
		val keySecretFile = rootProject.file("keystore.properties")
		if (!keySecretFile.exists()) return@signingConfigs

		// load the properties
		val properties = Properties()
		keySecretFile.inputStream().use { properties.load(it) }

		val userHome = System.getProperty("user.home")
		val storeFileName = properties.getProperty("STORE_FILE_NAME")

		val keyStoreFolder = File(userHome, "keystore")
		if (!keyStoreFolder.exists()) return@signingConfigs

		val keyStoreFile = File(keyStoreFolder, storeFileName)
		if (!keyStoreFile.exists()) return@signingConfigs

		create("release") {
			storeFile = keyStoreFile
			keyAlias = properties.getProperty("KEY_ALIAS")
			keyPassword = properties.getProperty("KEY_PASSWORD")
			storePassword = properties.getProperty("STORE_PASSWORD")
		}
	}

	buildTypes {

		debug {
			applicationIdSuffix = ".debug"
			isMinifyEnabled = false
			isShrinkResources = false
		}

		release {
			isMinifyEnabled = true
			isShrinkResources = true
			multiDexEnabled = true
			signingConfigs.findByName("release")?.let { config ->
				signingConfig = config
			}

			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	buildFeatures {
		compose = true
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_17
		optIn.add("kotlin.time.ExperimentalTime")
		optIn.add("kotlin.uuid.ExperimentalUuidApi")
	}
}

composeCompiler {
//	featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
	metricsDestination = layout.buildDirectory.dir("compose_compiler")
	reportsDestination = layout.buildDirectory.dir("compose_compiler")
	stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("stability_config.conf"))
}

dependencies {
	//core
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	//	compose
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	//lifecycle compose runtime
	implementation(libs.androidx.lifecycle.runtime.compose)
	//navigation
	implementation(libs.compose.destination.animation)
	implementation(libs.compose.destination.core)
	ksp(libs.compose.destination.ksp)
	//kotlin immutable
	implementation(libs.kotlinx.collections.immutable)
	//kotlinx datetime
	implementation(libs.kotlinx.datetime)
	//splash api
	implementation(libs.androidx.core.splashscreen)
	// koin
	implementation(platform(libs.koin.bom))
	implementation(libs.koin.core)
	implementation(libs.koin.android)
	implementation(libs.koin.compose)
	implementation(libs.koin.android.startup)
	// shapes
	implementation(libs.androidx.graphics.shapes)
	// kotlinx-serialization
	implementation(libs.kotlinx.serialization.json)
	//icons
	implementation(libs.androidx.material.icons.extended)
	//datastore
	implementation(libs.androidx.datastore)
	implementation(libs.protobuf.javalite)
	implementation(libs.protobuf.kotlin.lite)
	//tests
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	//debug
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:4.26.1"
	}
	plugins {
		create("java") {
			artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0"
		}
	}

	generateProtoTasks {
		all().forEach { task ->
			task.plugins {
				create("java") {
					option("lite")
				}
				create("kotlin") {
					option("lite")
				}
			}
		}
	}
}