plugins {
	id("com.android.application")
	id("com.google.gms.google-services")

	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.compose")

	kotlin("plugin.serialization")
}

android {
	namespace = "m2sdl.lacuillere"
	compileSdk = 35

	defaultConfig {
		applicationId = "m2sdl.lacuillere"
		minSdk = 27
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
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

dependencies {
	/// IMAGE
	implementation("io.coil-kt.coil3:coil-compose:3.1.0")
	implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")

	/// GOOGLE SPYWARE BLOAT
	implementation("com.google.maps.android:maps-compose:6.4.0")
	implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
	implementation("com.google.android.gms:play-services-location:21.3.0")

	/// NAVIGATION
	val navVersion = "2.8.8"
	// Jetpack Compose Integration
	implementation("androidx.navigation:navigation-compose:$navVersion")
	implementation("androidx.navigation:navigation-runtime-ktx:$navVersion")

	// Views/Fragments Integration
	implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
	implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

	// Feature module support for Fragments
	implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")

	// Testing Navigation
	androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

	// JSON serialization library, works with the Kotlin serialization plugin.
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

	/// CORE SHIT
	implementation("androidx.core:core-ktx:1.15.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
	implementation("androidx.activity:activity-compose:1.10.1")
	implementation(platform("androidx.compose:compose-bom:2025.02.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3:1.3.1")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.2.1")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2025.02.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}
