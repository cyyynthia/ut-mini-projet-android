plugins {
	alias(libs.plugins.android.application)

	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.kotlin.serialization)

	alias(libs.plugins.google.services)
}

android {
	namespace = "m2sdl.lacuillere"
	compileSdk = 35

	defaultConfig {
		applicationId = "m2sdl.lacuillere"
		minSdk = 26
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
	}
}

dependencies {
	/// IMAGE
	implementation(libs.coil.compose)
	implementation(libs.coil.network.okhttp)

	/// GOOGLE SPYWARE BLOAT
	implementation(libs.google.maps.compose)
	implementation(libs.google.services.location)
	implementation(libs.google.services.maps)

	/// NAVIGATION
	// Jetpack Compose Integration
	implementation(libs.androidx.navigation.compose)
	implementation(libs.androidx.navigation.runtime.ktx)

	// Views/Fragments Integration
	implementation(libs.androidx.navigation.fragment.ktx)
	implementation(libs.androidx.navigation.ui.ktx)

	// Serialization library
	implementation(libs.kotlinx.serialization)

	// Camera
	implementation(libs.androidx.camera.camera2)
	implementation(libs.androidx.camera.lifecycle)
	implementation(libs.androidx.camera.compose)

	/// CORE SHIT
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.tooling.preview)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.material.icons)

	debugImplementation(libs.androidx.compose.ui.tooling)
	debugImplementation(libs.androidx.compose.ui.test.manifest)
}
