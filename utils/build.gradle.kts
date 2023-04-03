plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Config.compile_sdk
    namespace = Config.namespaceUtils

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

    // Koin for Android
    implementation(Koin.core)

    // RxJava
    implementation(RxJava.rxjava)
    implementation(RxJava.rxandroid)

    // Coil
    implementation(Coil.coil)
    implementation(Coil.coilGif)
    implementation(Coil.coilSvg)
}