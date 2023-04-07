package jt.projects.repository.network

import android.util.Log
import jt.projects.repository.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { GsonConverterFactory.create() }

    single { HttpLoggingInterceptor() { message: String? -> Log.i("LOG_BODY", "$message") }
        .setLevel(HttpLoggingInterceptor.Level.BODY) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.VK_BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    single<VkNetworkRepository> {
        VkNetworkRepositoryImpl(retrofit = get())
    }
}