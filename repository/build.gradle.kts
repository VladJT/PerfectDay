plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = Config.compile_sdk
    namespace = Config.namespaceRepository

    defaultConfig {
        minSdk = Config.min_sdk

        buildConfigField("String", "VK_BASE_URL", "\"https://api.vk.com/\"")
        buildConfigField("Double", "VK_VERSION_API", "5.131")
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
    // PROJECTS
    implementation(project(Modules.utils))
    implementation(project(Modules.model))

    // Room
    implementation(Room.runtime)
    kapt(Room.compiler)
    implementation(Room.room_ktx)

    // Retrofit 2
    implementation(Retrofit.retrofit)
    implementation(Retrofit.converter_gson)
    implementation(Retrofit.logging_interceptor)
    implementation(Retrofit.adapter_coroutines)//КОРУТИНЫ для Retrofit

    // Koin for Android
    implementation(Koin.core)
    implementation(Koin.viewmodel)
    implementation(Koin.compat)
    testImplementation(Koin.test)
    testImplementation(Koin.junit4Test)
}