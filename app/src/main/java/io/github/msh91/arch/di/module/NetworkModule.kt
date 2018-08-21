package io.github.msh91.arch.di.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.*
import dagger.Lazy
import dagger.Module
import dagger.Provides
import io.github.msh91.arch.BuildConfig
import io.github.msh91.arch.data.restful.APIs
import io.github.msh91.arch.data.restful.APIsWithToken
import io.github.msh91.arch.data.source.cloud.BaseCloudRepository
import io.github.msh91.arch.data.source.cloud.CloudMockRepository
import io.github.msh91.arch.data.source.cloud.CloudRepository
import io.github.msh91.arch.data.source.preference.AppPreferencesHelper
import io.github.msh91.arch.di.qualifier.WithToken
import io.github.msh91.arch.di.qualifier.WithoutToken
import io.github.msh91.arch.di.qualifier.network.Cloud
import io.github.msh91.arch.di.qualifier.network.Mock
import io.github.msh91.arch.util.SecretFields
import io.github.msh91.arch.util.TokenAuthenticator
import io.github.msh91.arch.util.providers.BaseResourceProvider
import io.github.msh91.arch.util.providers.file.BaseFileProvider
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


/**
 * The main [Module] for providing network-related classes
 */
@Module
class NetworkModule {

    /**
     * provides Gson with custom [Date] converter for [Long] epoch times
     */
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                // Deserializer to convert json long value into Date
                .registerTypeAdapter(Date::class.java, JsonDeserializer { json, typeOfT, context -> Date(json.asJsonPrimitive.asLong) })
                // Serializer to convert Date value into long json primitive
                .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, typeOfSrc, context -> JsonPrimitive(src.time) })
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
     * Provides [TokenAuthenticator] for refreshing tokens
     * @param apis api service instance for requesting refresh token api
     * @param appPref to save new token
     *
     * @return an instance of [TokenAuthenticator]
     */
    @Singleton
    @Provides
    fun provideAuthenticator(apis: Lazy<APIs>, appPref: Lazy<AppPreferencesHelper>): Authenticator {
        return TokenAuthenticator(apis, appPref)
    }

    /**
     * Provides [OkHttpClient] instance for token based api services
     *
     * @param preferencesHelper to access saved token, provided by [AppModule.provideAppPreferencesHelper]
     * @param headers default shared headers to be added in http request, provided by [provideSharedHeaders]
     * @param authenticator instance of [TokenAuthenticator] for handling UNAUTHORIZED errors, provided by [provideAuthenticator]
     *
     * @return an instance of [OkHttpClient]
     */
    @Singleton
    @Provides
    @WithToken
    fun provideOkHttpClientWithToken(preferencesHelper: AppPreferencesHelper, headers: Headers, authenticator: Authenticator): OkHttpClient {
        val builder = OkHttpClient.Builder()

        // if the app is in DEBUG mode OkHttp will show complete log in logcat and Stetho framework
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)

            // Stetho will be initialized here
            builder.addNetworkInterceptor(StethoInterceptor())
        }

        builder.interceptors().add(Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
                    // add default shared headers to every http request
                    .headers(headers)
                    // add tokenType and token to Authorization header of request
                    .addHeader("Authorization",
                            preferencesHelper.tokenType + " " + preferencesHelper.token)
                    .method(request.method(), request.body())
            chain.proceed(requestBuilder.build())
        })

        builder.authenticator(authenticator)

        return builder.build()
    }

    /**
     * provides instance of [OkHttpClient] for without-token api services
     *
     * @param headers default shared headers provided by [provideSharedHeaders]
     * @return an instance of [OkHttpClient]
     */
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

    /**
     * provide an instance of [Retrofit] for without-token api services
     *
     * @param okHttpClient an instance of without-token [okHttpClient] provided by [provideOkHttpClient]
     * @param gson an instance of gson provided by [provideGson] to use as retrofit converter factory
     *
     * @return an instance of [Retrofit] for without-token api calls
     */
    @Singleton
    @Provides
    @WithoutToken
    fun provideRetrofit(@WithoutToken okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
                // create gson converter factory
                .addConverterFactory(GsonConverterFactory.create(gson))
                // create call adapter factory for RxJava
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // get base url from SecretFields interface
                .baseUrl(SecretFields().getBaseURI())
                .build()
    }

    /**
     * provide an instance of [Retrofit] for with-token api services
     *
     * @param okHttpClient an instance of with-token [okHttpClient] provided by [provideOkHttpClientWithToken]
     * @param gson an instance of gson provided by [provideGson] to use as retrofit converter factory
     *
     * @return an instance of [Retrofit] for with-token api calls
     */
    @Singleton
    @Provides
    @WithToken
    fun provideRetrofitWithToken(@WithToken okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
                // create gson converter factory
                .addConverterFactory(GsonConverterFactory.create(gson))
                // create call adapter factory for RxJava
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // get base url from SecretFields interface
                .baseUrl(SecretFields().getBaseURI())
                .build()
    }

    /**
     * provides [APIs] service to use for without-token api calls
     *
     * @param retrofit an instance of without-token [Retrofit]
     *
     * @return returns an instance of [APIs]
     */
    @Singleton
    @Provides
    fun provideService(@WithoutToken retrofit: Retrofit): APIs {
        return retrofit.create(APIs::class.java)
    }


    /**
     * provides [APIsWithToken] service to use for with-token api calls
     *
     * @param retrofit an instance of with-token [Retrofit]
     *
     * @return returns an instance of [APIsWithToken]
     */
    @Singleton
    @Provides
    fun provideServiceWithToken(@WithToken retrofit: Retrofit): APIsWithToken {
        return retrofit.create(APIsWithToken::class.java)
    }

    /**
     * provides real implementation of [BaseCloudRepository] to access real api services
     *
     * @param apIs an instance of [APIs] to access all without-token apis
     * @param apIsWithToken an instance of [APIsWithToken] to access all with-token apis
     *
     * @return returns an instance of [CloudRepository]
     */
    @Cloud
    @Provides
    fun provideCloudRepository(apIs: APIs, apIsWithToken: APIsWithToken): BaseCloudRepository {
        return CloudRepository(apIs, apIsWithToken)
    }

    /**
     * provides mock implementation of [BaseCloudRepository] to access mock api services
     *
     * @return returns an instance of [CloudMockRepository]
     */
    @Mock
    @Provides
    fun provideCloudMockRepository(apIs: APIs, apIsWithToken: APIsWithToken, resourceProvider: BaseResourceProvider, gson: Gson, fileProvider: BaseFileProvider): BaseCloudRepository {
        return if (BuildConfig.DEBUG)
            CloudMockRepository(resourceProvider, gson, fileProvider)
        else
            CloudRepository(apIs, apIsWithToken)
    }
}
