plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    compileSdk = Config.compile_sdk
    namespace = Config.namespaceModel

    defaultConfig {
        minSdk = Config.min_sdk
    }

    buildTypes {
        release {
            isMinifyEnabled = Config.isMinifyEnabled
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = Config.java_version
        targetCompatibility = Config.java_version

    }

    kotlinOptions {
        jvmTarget = Config.jvmTarget_version
    }
}

dependencies {
    // AndroidX
    implementation(Design.appcompat)

    // Design
    implementation(Design.material)

    // Kotlin
    implementation(Kotlin.core)
    implementation(Kotlin.stdlib)
}