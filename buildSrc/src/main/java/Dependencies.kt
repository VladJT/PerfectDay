import org.gradle.api.JavaVersion

object Config {
    const val application_id = "jt.projects.perfectday"
    const val namespaceApp = "jt.projects.perfectday"
    const val namespaceUtils = "jt.projects.utils"
    const val namespaceModel = "jt.projects.model"
    const val namespaceRepository = "jt.projects.repository"
    const val compile_sdk = 33
    const val min_sdk = 29
    const val target_sdk = 33
    val java_version = JavaVersion.VERSION_17
    const val jvmTarget_version = "17"
}

object Releases {
    const val version_code = 2
    const val version_name = "1.1"
}

object Modules {
    const val app = ":app"
    const val core = ":core"
    const val model = ":model"
    const val repository = ":repository"
    const val utils = ":utils"
    const val tests = ":tests"
}

object Versions {
    //Design
    const val appcompat = "1.6.1"
    const val material = "1.8.0"

    //Kotlin
    const val core = "1.8.0"
    const val stdlib = "1.5.21"
    const val coroutinesCore = "1.6.4"
    const val coroutinesAndroid = "1.6.4"

    //Retrofit
    const val retrofit = "2.9.0"
    const val converterGson = "2.9.0"
    const val interceptor = "4.9.2"
    const val adapterCoroutines = "0.9.2"
    const val adapterRxjava2 = "1.0.0"

    //Koin
    const val koin = "3.4.0"

    //Coil
    const val coil = "2.2.2"
    const val coilGif = "2.1.0"
    const val coilSvg = "2.1.0"

    //Room
    const val roomKtx = "2.5.0"
    const val runtime = "2.5.0"
    const val roomCompiler = "2.5.0"

    //Test
    const val jUnit = "4.13.2"
    const val runner = "1.2.0"
    const val espressoCore = "3.5.1"
    const val extjUnit = "1.1.5"
    const val uiAutomator = "2.2.0"

    // RxJava
    const val rxandroid = "3.0.0"
    const val rxjava = "3.0.0"

    //CrunchyCalendar
    const val crunchycalendar = "2.4.0"

    // work manager
    const val workversion = "2.8.1"
    const val wmktx = "2.8.1"
    const val livecycle = "2.6.1"
}

object Design {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
}

object Kotlin {
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.stdlib}"
    const val coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    const val coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.converterGson}"
    const val adapter_coroutines =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.adapterCoroutines}"
    const val adapter_rxjava2 =
        "com.jakewharton.retrofit:retrofit2-rxjava2-adapter:${Versions.adapterRxjava2}"
    const val logging_interceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
}

object Koin {
    const val core = "io.insert-koin:koin-core:${Versions.koin}"

    //Koin для поддержки Android (Scope,ViewModel ...)
    const val viewmodel = "io.insert-koin:koin-android:${Versions.koin}"

    //Для совместимости с Java
    const val compat = "io.insert-koin:koin-android-compat:${Versions.koin}"

    // При написании юнит-тестов можно создавать модули в рантайме и вызывать функцию startKoin  внутри тестов
    const val test = "io.insert-koin:koin-test:${Versions.koin}"

    // Needed JUnit version
    const val junit4Test = "io.insert-koin:koin-test-junit4:${Versions.koin}"
    const val junit5Test = "io.insert-koin:koin-test-junit5:${Versions.koin}"
}

object Coil {
    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val coilGif = "io.coil-kt:coil-gif:${Versions.coilGif}"
    const val coilSvg = "io.coil-kt:coil-svg:${Versions.coilSvg}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.runtime}"
    const val compiler = "androidx.room:room-compiler:${Versions.roomCompiler}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.roomKtx}"
}

object TestImpl {
    const val junit = "junit:junit:${Versions.jUnit}"
    const val extjunit = "androidx.test.ext:junit:${Versions.extjUnit}"
    const val runner = "androidx.test:runner:${Versions.runner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val uiAutomator = "androidx.test.uiautomator:uiautomator:${Versions.uiAutomator}"
}

object RxJava {
    const val rxandroid = "io.reactivex.rxjava3:rxandroid:${Versions.rxandroid}"
    const val rxjava = "io.reactivex.rxjava3:rxjava:${Versions.rxjava}"
}

object CrunchyCalendar {
    const val crunchycalendar = "ru.cleverpumpkin:crunchycalendar:${Versions.crunchycalendar}"
}

object WorkManager {
    const val workmanager = "androidx.work:work-runtime-ktx:${Versions.workversion}"
    const val livecycle = "androidx.lifecycle:lifecycle-livedata-core:${Versions.livecycle}"
    const val ktx = "androidx.work:work-runtime-ktx:${Versions.wmktx}"
}