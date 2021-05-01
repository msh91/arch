package io.github.msh91.arch.di.module

import com.google.gson.*
import dagger.Module
import dagger.Provides
import io.github.msh91.arch.BuildConfig
import io.github.msh91.arch.data.di.qualifier.Concrete
import io.github.msh91.arch.data.di.qualifier.Stub
import io.github.msh91.arch.data.source.local.file.BaseFileProvider
import io.github.msh91.arch.data.source.remote.CryptoDataSource
import io.github.msh91.arch.data.source.remote.StubCryptoDataSource
import io.github.msh91.arch.util.SecretFields
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

/**
 * The main [Module] for providing network-related classes
 */
@Module
object NetworkModule {

    /**
     * provides Gson with custom [Date] converter for [Long] epoch times
     */
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            // Deserializer to convert json long value into Date
            .registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, _, _ ->
                    Date(json.asJsonPrimitive.asLong)
                }
            )
            // Serializer to convert Date value into long json primitive
            .registerTypeAdapter(
                Date::class.java,
                JsonSerializer<Date> { src, _, _ ->
                    JsonPrimitive(src.time)
                }
            )
            .create()
    }

    /**
     * provides shared [Headers] to be added into [OkHttpClient] instances
     */
    @Singleton
    @Provides
    fun provideSharedHeaders(): Headers {
        return Headers.Builder()
            .add("Accept", "*/*")
            .add("User-Agent", "mobile")
            .build()
    }

    /**
     * provides instance of [OkHttpClient] for api services
     *
     * @param headers default shared headers provided by [provideSharedHeaders]
     * @return an instance of [OkHttpClient]
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(headers: Headers, secretFields: SecretFields): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        builder.interceptors().add(
            Interceptor { chain ->
                val request = chain.request()
                val requestBuilder = request.newBuilder()
                    .headers(headers)
                    .addHeader("X-CMC_PRO_API_KEY", secretFields.apiKey)
                    .method(request.method(), request.body())
                chain.proceed(requestBuilder.build())
            }
        )

        return builder.build()
    }

    /**
     * provide an instance of [Retrofit] api services
     *
     * @param okHttpClient an instance of [okHttpClient] provided by [provideOkHttpClient]
     * @param gson an instance of gson provided by [provideGson] to use as retrofit converter factory
     *
     * @return an instance of [Retrofit] for api calls
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson, secretFields: SecretFields): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            // create gson converter factory
            .addConverterFactory(GsonConverterFactory.create(gson))
            // get base url from SecretFields interface
            .baseUrl(secretFields.getBaseUrl())
            .build()
    }

    @Provides
    @Concrete
    fun provideConcreteCryptoDataSource(retrofit: Retrofit): CryptoDataSource {
        return retrofit.create(CryptoDataSource::class.java)
    }

    @Provides
    @Stub
    fun provideStubCryptoDataSource(gson: Gson, fileProvider: BaseFileProvider): CryptoDataSource {
        return StubCryptoDataSource(gson, fileProvider)
    }
}
