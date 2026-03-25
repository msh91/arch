package io.github.msh91.arcyto.history.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.msh91.arcyto.history.data.remote.HistoricalRemoteDataSource
import retrofit2.Retrofit
import retrofit2.create

@ContributesTo(AppScope::class)
interface HistoryModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideHistoricalRemoteDataSource(retrofit: Retrofit): HistoricalRemoteDataSource = retrofit.create()
}
