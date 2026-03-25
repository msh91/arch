package io.github.msh91.arcyto.core.data.remote

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

@ContributesTo(AppScope::class)
interface RemoteModule {
    @SingleIn(AppScope::class)
    @Provides
    fun provideDefaultHeaderInterceptor(): Interceptor =
        Interceptor { chain ->
            val request =
                chain
                    .request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("x-cg-demo-api-key", BuildConfig.API_KEY)
                    .build()
            chain.proceed(request)
        }

    @SingleIn(AppScope::class)
    @Provides
    fun provideOkHttpClientBuilder(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        defaultHeaderInterceptor: Interceptor,
    ): OkHttpClient.Builder =
        OkHttpClient
            .Builder()
            .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(defaultHeaderInterceptor)
            .addInterceptor(httpLoggingInterceptor)

    @Provides
    @SingleIn(AppScope::class)
    fun provideJsonConverterFactory(): Converter.Factory {
        val jsonFormat =
            Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        return jsonFormat.asConverterFactory("application/json; charset=UTF8".toMediaType())
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideRetrofitBuilder(
        jsonConverterFactory: Converter.Factory,
        errorHandlerCallAdapterFactory: ErrorHandlerCallAdapterFactory,
    ): Retrofit.Builder =
        Retrofit
            .Builder()
            .addConverterFactory(jsonConverterFactory)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(errorHandlerCallAdapterFactory)

    @SingleIn(AppScope::class)
    @Provides
    fun provideRetrofit(
        retrofitBuilder: Retrofit.Builder,
        okHttpClientBuilder: OkHttpClient.Builder,
    ): Retrofit =
        retrofitBuilder
            .client(okHttpClientBuilder.build())
            .build()

    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

    companion object {
        private const val BASE_URL = "https://api.coingecko.com/api/v3/"
        private const val DEFAULT_TIMEOUT_SECONDS = 30L
    }
}
