plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services")
}

android {
    compileSdk = Config.compile_sdk
    namespace = Config.namespaceApp

    buildToolsVersion = "30.0.3"

    buildFeatures {
        viewBinding = true
    }

    lint {
        // Turns off checks for the issue IDs you specify.
        disable += "Instantiatable"
    }

    defaultConfig {
        applicationId = Config.application_id
        minSdk = Config.min_sdk
        targetSdk = Config.target_sdk
        versionCode = Releases.version_code
        versionName = Releases.version_name
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
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
    implementation(project(Modules.model))
    implementation(project(Modules.utils))
    implementation(project(Modules.repository))
    implementation(project(Modules.model))
    implementation(project(Modules.utils))
    implementation(project(Modules.repository))

    // AndroidX
    implementation(Design.appcompat)

    // Design
    implementation(Design.material)

    // Kotlin
    implementation(Kotlin.core)
    implementation(Kotlin.stdlib)

    // КОРУТИНЫ
    implementation(Kotlin.coroutines_core)
    implementation(Kotlin.coroutines_android)

    // Koin for Android
    implementation(Koin.core)
    implementation(Koin.viewmodel)
    implementation(Koin.compat)
    testImplementation(Koin.test)
    testImplementation(Koin.junit4Test)


    // firebase CM
    implementation("com.google.firebase:firebase-messaging-ktx:23.1.2")
    // firebase perfomance monitoring
    implementation("com.google.firebase:firebase-perf-ktx:20.3.1")
    // firebase in-app messaging
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx:20.3.1")

    // ML-KIT translation
    implementation("com.google.mlkit:translate:17.0.1")
    
    // VK
    implementation("com.vk:android-sdk-core:3.5.1")
    implementation("com.vk:android-sdk-api:3.5.1")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // circleindicator (https://github.com/ongakuer/CircleIndicator)
    implementation("me.relex:circleindicator:2.1.6")

    // Coil
    implementation(Coil.coil)

    // Room
    implementation(Room.runtime)
    kapt(Room.compiler)
    implementation(Room.room_ktx)

    // ** TESTS **
    androidTestImplementation(TestImpl.runner)
    androidTestImplementation(TestImpl.espresso)
    androidTestImplementation(TestImpl.extjunit)
    androidTestImplementation(TestImpl.uiAutomator)

    //CrunchyCalendar
    implementation(CrunchyCalendar.crunchycalendar)

    //WorkManager
    implementation(WorkManager.workmanager)
    implementation(WorkManager.livecycle)
    implementation(WorkManager.ktx)
}