package io.github.msh91.arcyto.details.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.github.msh91.arcyto.details.data.remote.CoinDetailsDataSource
import retrofit2.Retrofit
import retrofit2.create

@ContributesTo(AppScope::class)
interface DetailsRemoteModule {
    @Provides
    fun provideDetailsRemoteDataSource(retrofit: Retrofit): CoinDetailsDataSource = retrofit.create()
}
