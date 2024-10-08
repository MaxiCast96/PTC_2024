plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "ptc.proyecto.estrella.bella"
    compileSdk = 34

    defaultConfig {
        applicationId = "ptc.proyecto.estrella.bella"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("com.google.android.material:material:1.12.0")
    implementation("com.airbnb.android:lottie:6.4.1")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("com.oracle.database.jdbc:ojdbc6:11.2.0.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(files("libs\\activation.jar"))
    implementation(files("libs\\additionnal.jar"))
    implementation(files("libs\\mail.jar"))
    implementation(files("libs\\activation.jar"))
    implementation(files("libs\\additionnal.jar"))
    implementation(files("libs\\mail.jar"))
    implementation(libs.firebase.storage.ktx)
    implementation(libs.play.services.fitness)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.github.bumptech.glide:glide:4.16.0")


}