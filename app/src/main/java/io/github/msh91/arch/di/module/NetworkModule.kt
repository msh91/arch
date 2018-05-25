package io.github.msh91.arch.di.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.*
import dagger.Lazy
import dagger.Module
import dagger.Provides
import io.github.msh91.arch.BuildConfig
import io.github.msh91.arch.data.local.AppPreferencesHelper
import io.github.msh91.arch.data.repository.BaseCloudRepository
import io.github.msh91.arch.data.repository.CloudMockRepository
import io.github.msh91.arch.data.repository.CloudRepository
import io.github.msh91.arch.data.restful.APIs
import io.github.msh91.arch.data.restful.APIsWithToken
import io.github.msh91.arch.di.qualifier.WithToken
import io.github.msh91.arch.di.qualifier.WithoutToken
import io.github.msh91.arch.di.qualifier.network.Cloud
import io.github.msh91.arch.di.qualifier.network.Mock
import io.github.msh91.arch.util.SecretFields
import io.github.msh91.arch.util.TokenAuthenticator
import okhttp3.Authenticator
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton



@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(Date::class.java, JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
                .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, typeOfSrc, context -> JsonPrimitive(src.time) })
                .create()
    }

    @Singleton
    @Provides
    fun provideSharedHeaders(): Headers{
        return Headers.Builder()
                .add("Accept","*/*")
                .add("User-Agent", "mobile")
                .build()
    }

    @Singleton
    @Provides
    fun provideAuthenticator(apis: Lazy<APIs>, appPref: Lazy<AppPreferencesHelper>): Authenticator{
        return TokenAuthenticator(apis, appPref)
    }

    @Singleton
    @Provides
    @WithToken
    fun provideOkHttpClientWithToken(preferencesHelper: AppPreferencesHelper, headers: Headers, authenticator: Authenticator): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)

            builder.addNetworkInterceptor(StethoInterceptor())
        }

        builder.interceptors().add(Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
                    .headers(headers)
                    .addHeader("Authorization",
                            preferencesHelper.tokenType + " " + preferencesHelper.token)
                    .method(request.method(), request.body())
            chain.proceed(requestBuilder.build())
        })

        builder.authenticator(authenticator)

        return builder.build()
    }

    @Singleton
    @Provides
    @WithoutToken
    fun provideOkHttpClient(headers: Headers): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)

            builder.addNetworkInterceptor(StethoInterceptor())
        }

        builder.interceptors().add(Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
                    .headers(headers)
                    //TODO it will temporary, we should find some solution
                    .addHeader("Authorization", SecretFields().authorizationKey())

            .method(request.method(), request.body())
            chain.proceed(requestBuilder.build())
        })

        return builder.build()

    }

    @Singleton
    @Provides
    @WithoutToken
    fun provideRetrofit(@WithoutToken okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(SecretFields().getBaseURI())
                .build()
    }

    @Singleton
    @Provides
    @WithToken
    fun provideRetrofitWithToken(@WithToken okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(SecretFields().getBaseURI())
                .build()
    }

    @Singleton
    @Provides
    fun provideService(@WithoutToken retrofit: Retrofit): APIs {
        return retrofit.create(APIs::class.java)
    }

    @Singleton
    @Provides
    fun provideServiceWithToken(@WithToken retrofit: Retrofit): APIsWithToken {
        return retrofit.create(APIsWithToken::class.java)
    }

    @Cloud
    @Provides
    fun provideCloudRepository(apIs: APIs, apIsWithToken: APIsWithToken): BaseCloudRepository {
        return CloudRepository(apIs, apIsWithToken)
    }

    @Mock
    @Provides
    fun provideCloudMockRepository(): BaseCloudRepository {
        return CloudMockRepository()
    }
}
