package io.github.msh91.arcyto.history.di

import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.history.data.remote.HistoricalRemoteDataSource
import retrofit2.Retrofit
import retrofit2.create

@Module
@ContributesTo(AppScope::class)
class HistoryModule {

    @Provides
    fun provideHistoricalRemoteDataSource(retrofit: Retrofit): HistoricalRemoteDataSource {
        return retrofit.create()
    }
}