package io.github.msh91.arcyto.core.data.remote

import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import io.github.msh91.arcyto.core.di.scope.AppScope
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@ContributesTo(AppScope::class)
class RemoteModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
    }

    @Provides
    @Singleton
    fun provideJsonConverterFactory(): Converter.Factory {
        val jsonFormat = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true

        }
        return jsonFormat.asConverterFactory("application/json; charset=UTF8".toMediaType())
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        jsonConverterFactory: Converter.Factory, errorHandlerCallAdapterFactory:
        ErrorHandlerCallAdapterFactory
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(jsonConverterFactory)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(errorHandlerCallAdapterFactory)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        retrofitBuilder: Retrofit.Builder,
        okHttpClientBuilder: OkHttpClient.Builder
    ): Retrofit {
        return retrofitBuilder
            .client(okHttpClientBuilder.build())
            .build()
    }

    companion object {
        private const val BASE_URL = "https://api.coingecko.com/api/v3/"
        private const val DEFAULT_TIMEOUT_SECONDS = 30L
    }
}