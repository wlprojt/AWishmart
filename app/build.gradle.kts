plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.wishmart"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.wishmart"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation ("com.google.android.gms:play-services-auth:21.5.0")

    // Firebase Auth
    implementation("com.google.firebase:firebase-auth-ktx:23.2.1")

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("io.coil-kt:coil-compose:2.7.0")

    // Compose dependencies
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    //Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.59.1")
    ksp ("com.google.dagger:hilt-compiler:2.59.1")
    implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.3.0")
    ksp ("androidx.hilt:hilt-compiler:1.3.0")
    ksp("com.google.dagger:hilt-compiler:2.59.1")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation ("com.squareup.retrofit2:converter-moshi:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    implementation("com.auth0.android:jwtdecode:2.0.2")

    implementation("com.razorpay:checkout:1.6.20")

    // Retrofit
//    implementation("com.squareup.retrofit2:retrofit:2.11.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

// Coroutines
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}