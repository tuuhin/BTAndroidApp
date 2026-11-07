import com.android.build.api.dsl.ApkSigningConfig
import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlinx.serialization)
	alias(libs.plugins.google.protobuf)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.koin.compiler)
}

android {
	namespace = "com.eva.bluetoothterminalapp"
	compileSdk = libs.versions.android.compilesdk.get().toInt()

	defaultConfig {
		applicationId = "com.eva.bluetoothterminalapp"
		minSdk = libs.versions.android.minsdk.get().toInt()
		targetSdk = libs.versions.android.targetsdk.get().toInt()
		versionCode = 6
		versionName = "1.2.2"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	signingConfigs {
		// find if there is a properties file
		createReleaseConfig()
	}

	buildTypes {
		debug {
			applicationIdSuffix = ".debug"
		}
		release {
			optimization {
				enable = true
			}
			// using the release config if not configured unsigned apk
			signingConfig = signingConfigs.findByName("release")
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

koinCompiler {
	userLogs = true
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
	implementation(libs.bundles.compose)
	//lifecycle runtime
	implementation(libs.androidx.lifecycle.service)
	implementation(libs.androidx.lifecycle.runtime.compose)
	//navigation and adaptive layout
	implementation(libs.bundles.navigation3)
	implementation(libs.androidx.lifecycle.navigation3)
	implementation(libs.androidx.adaptive.navigation3)
	implementation(libs.androidx.material3.adaptive.navigation.suite)
	implementation(libs.androidx.window)
	//kotlinx
	implementation(libs.bundles.kotlinx)
	//splash api
	implementation(libs.androidx.core.splashscreen)
	// koin
	implementation(platform(libs.koin.bom))
	implementation(libs.bundles.koin)
	// color
	implementation(libs.materialKolor.material3)
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

kotlin {
	compilerOptions {
		optIn.add("androidx.compose.material3.ExperimentalMaterial3ExpressiveApi")
	}
}

protobuf {
	protoc {
		artifact = libs.protobuf.protoc.compiler.get().toString()
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

fun NamedDomainObjectContainer<out ApkSigningConfig>.createReleaseConfig() {
	val keySecretFile = project.file("keystore.properties")

	var storeFileName: String? = null
	var keyAlias: String? = null
	var keyPassword: String? = null
	var storePassword: String? = null

	// first check if keystore exists in the root project
	if (keySecretFile.exists()) {
		val properties = Properties()
		keySecretFile.inputStream().use { properties.load(it) }
		storeFileName = properties.getProperty("STORE_FILE_NAME")
		keyAlias = properties.getProperty("KEY_ALIAS")
		keyPassword = properties.getProperty("KEY_PASSWORD")
		storePassword = properties.getProperty("STORE_PASSWORD")
	}

	// if not found fallback to env
	if (storeFileName.isNullOrBlank()) storeFileName = System.getenv("STORE_FILE_NAME")
	if (keyAlias.isNullOrBlank()) keyAlias = System.getenv("KEY_ALIAS")
	if (keyPassword.isNullOrBlank()) keyPassword = System.getenv("KEY_PASSWORD")
	if (storePassword.isNullOrBlank()) storePassword = System.getenv("STORE_PASSWORD")

	// finally search for the jks file
	val jksFile = storeFileName?.takeIf { it.isNotBlank() }?.let(project::file)

	val propertiesOk =
		!keyAlias.isNullOrBlank() && !keyPassword.isNullOrBlank() && !storePassword.isNullOrBlank()
	val isJskFileOk = jksFile?.exists() == true

	if (!propertiesOk || !isJskFileOk) return

	// Create the release config
	create("release") {
		storeFile = jksFile
		this.keyAlias = keyAlias
		this.keyPassword = keyPassword
		this.storePassword = storePassword
	}
}